package com.tunzy.app.service

import android.app.*
import android.content.Intent
import android.os.Bundle
import android.os.IBinder
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import androidx.core.app.NotificationCompat
import com.tunzy.app.MainActivity

class WakeWordService : Service() {

    companion object {
        private const val CHANNEL_ID = "tunzy_wake_channel"
        private const val NOTIFICATION_ID = 1
        private const val WAKE_PHRASE = "hey tunzy"
    }

    private var speechRecognizer: SpeechRecognizer? = null
    private var isRunning = false

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        startForeground(NOTIFICATION_ID, buildNotification())
        startListeningLoop()
    }

    private fun startListeningLoop() {
        isRunning = true
        listenOnce()
    }

    private fun listenOnce() {
        if (!isRunning) return

        speechRecognizer?.destroy()
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this)

        speechRecognizer?.setRecognitionListener(object : RecognitionListener {
            override fun onResults(results: Bundle?) {
                val matches = results
                    ?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                    ?: emptyList()

                val heard = matches.any {
                    it.contains(WAKE_PHRASE, ignoreCase = true)
                }

                if (heard) {
                    onWakeWordDetected()
                } else {
                    // Keep looping — listen again
                    listenOnce()
                }
            }

            override fun onError(error: Int) {
                // Restart on any error to keep looping
                if (isRunning) listenOnce()
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
            putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US")
            putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 3)
            putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, packageName)
        }

        speechRecognizer?.startListening(intent)
    }

    private fun onWakeWordDetected() {
        TunzyStateHolder.setState(TunzyState.WAKE)

        // Bring app to front
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP
        }
        startActivity(intent)

        // Start Gemini conversation
        startService(Intent(this, GeminiVoiceService::class.java))

        // Wait a moment then resume wake word loop
        android.os.Handler(mainLooper).postDelayed({
            if (isRunning) listenOnce()
        }, 5000)
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            CHANNEL_ID,
            "TUNZY Wake Word",
            NotificationManager.IMPORTANCE_LOW
        ).apply {
            description = "Listening for Hey Tunzy"
            setShowBadge(false)
        }
        getSystemService(NotificationManager::class.java)
            .createNotificationChannel(channel)
    }

    private fun buildNotification(): Notification {
        val pendingIntent = PendingIntent.getActivity(
            this, 0,
            Intent(this, MainActivity::class.java),
            PendingIntent.FLAG_IMMUTABLE
        )
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("TUNZY")
            .setContentText("Listening for \"Hey Tunzy\"...")
            .setSmallIcon(android.R.drawable.ic_btn_speak_now)
            .setContentIntent(pendingIntent)
            .setSilent(true)
            .setOngoing(true)
            .build()
    }

    override fun onDestroy() {
        isRunning = false
        speechRecognizer?.destroy()
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null
}