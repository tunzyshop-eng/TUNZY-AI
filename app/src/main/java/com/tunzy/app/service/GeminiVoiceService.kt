package com.tunzy.app.service

import android.app.Service
import android.content.Intent
import android.os.Bundle
import android.os.IBinder
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.util.Log
import kotlinx.coroutines.*
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.util.Locale

class GeminiVoiceService : Service(), TextToSpeech.OnInitListener {

    companion object {
        private const val TAG = "GeminiVoiceService"
        // Get your FREE key at https://aistudio.google.com/apikey
        private const val GEMINI_API_KEY = "YOUR_GEMINI_API_KEY_HERE"
        private const val GEMINI_URL =
            "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash-001:generateContent?key=$GEMINI_API_KEY"
    }

    private var tts: TextToSpeech? = null
    private var speechRecognizer: SpeechRecognizer? = null
    private val serviceScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private val client = OkHttpClient()
    private val conversationHistory = mutableListOf<JSONObject>()

    override fun onCreate() {
        super.onCreate()
        tts = TextToSpeech(this, this)
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            tts?.language = Locale.US
            tts?.setSpeechRate(1.0f)
            tts?.setPitch(0.95f)
            startListening()
        }
    }

    private fun startListening() {
        TunzyStateHolder.setState(TunzyState.LISTENING)
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this)
        speechRecognizer?.setRecognitionListener(object : RecognitionListener {
            override fun onResults(results: Bundle?) {
                val text = results
                    ?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                    ?.firstOrNull() ?: return
                TunzyStateHolder.setState(TunzyState.THINKING)
                serviceScope.launch { sendToGemini(text) }
            }
            override fun onError(error: Int) {
                TunzyStateHolder.setState(TunzyState.IDLE)
                stopSelf()
            }
            override fun onReadyForSpeech(params: Bundle?) {}
            override fun onBeginningOfSpeech() {}
            override fun onRmsChanged(rmsdB: Float) {}
            override fun onBufferReceived(buffer: ByteArray?) {}
            override fun onEndOfSpeech() {}
            override fun onPartialResults(partialResults: Bundle?) {}
            override fun onEvent(eventType: Int, params: Bundle?) {}
        })

        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US")
            putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1)
            putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS, 1500L)
        }
        speechRecognizer?.startListening(intent)
    }

    private suspend fun sendToGemini(userText: String) {
        conversationHistory.add(JSONObject().apply {
            put("role", "user")
            put("parts", JSONArray().put(JSONObject().put("text", userText)))
        })

        val systemInstruction = JSONObject().apply {
            put("parts", JSONArray().put(JSONObject().put("text",
                "You are TUNZY, a personal AI assistant. Be concise, friendly, and helpful. " +
                "You speak via voice so keep responses short and conversational. " +
                "Never use markdown, bullet points, or special characters."
            )))
        }

        val body = JSONObject().apply {
            put("system_instruction", systemInstruction)
            put("contents", JSONArray(conversationHistory))
        }.toString()

        val request = Request.Builder()
            .url(GEMINI_URL)
            .post(body.toRequestBody("application/json".toMediaType()))
            .build()

        try {
            val response = withContext(Dispatchers.IO) { client.newCall(request).execute() }
            val json = JSONObject(response.body?.string() ?: "")
            val reply = json
                .getJSONArray("candidates")
                .getJSONObject(0)
                .getJSONObject("content")
                .getJSONArray("parts")
                .getJSONObject(0)
                .getString("text")

            conversationHistory.add(JSONObject().apply {
                put("role", "model")
                put("parts", JSONArray().put(JSONObject().put("text", reply)))
            })

            withContext(Dispatchers.Main) { speakOut(reply) }

        } catch (e: IOException) {
            withContext(Dispatchers.Main) {
                speakOut("Sorry, I couldn't reach my brain. Try again.")
            }
        }
    }

    private fun speakOut(text: String) {
        TunzyStateHolder.setState(TunzyState.SPEAKING)
        tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "tunzy_utterance")
        tts?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
            override fun onStart(utteranceId: String?) {}
            override fun onDone(utteranceId: String?) {
                TunzyStateHolder.setState(TunzyState.IDLE)
                stopSelf()
            }
            override fun onError(utteranceId: String?) {
                TunzyStateHolder.setState(TunzyState.IDLE)
                stopSelf()
            }
        })
    }

    override fun onDestroy() {
        speechRecognizer?.destroy()
        tts?.stop()
        tts?.shutdown()
        serviceScope.cancel()
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null
}