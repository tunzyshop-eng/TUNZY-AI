package com.tunzy.app.service

import android.app.*
import android.content.Intent
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.tunzy.app.MainActivity
import kotlinx.coroutines.*
import org.vosk.Model
import org.vosk.Recognizer
import org.vosk.android.StorageService

class WakeWordService : Service() {

    companion object {
        private const val TAG = "WakeWordService"
        private const val CHANNEL_ID = "tunzy_wake_channel"
        private const val NOTIFICATION_ID = 1
        private const val SAMPLE_RATE = 16000
        private const val WAKE_PHRASE = "hey tunzy"
    }

    private var model: Model? = null
    private var recognizer: Recognizer? = null
    private var audioRecord: AudioRecord? = null
    private var isListening = false
    private val serviceScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        startForeground(NOTIFICATION_ID, buildNotification())
        initVosk()
    }

    private fun initVosk() {
        StorageService.unpack(this, "vosk-model-small-en-us-0.15", "model",
            { unpackedModel ->
                model = unpackedModel
                recognizer = Recognizer(model, SAMPLE_RATE.toFloat())
                startWakeWordLoop()
            },
            { exception ->
                Log.e(TAG, "Vosk model load failed: ${exception.message}")
            }
        )
    }

    private fun startWakeWordLoop() {
        val bufferSize = AudioRecord.getMinBufferSize(
            SAMPLE_RATE,
            AudioFormat.CHANNEL_IN_MONO,
            AudioFormat.ENCODING_PCM_16BIT
        )

        audioRecord = AudioRecord(
            MediaRecorder.AudioSource.MIC,
            SAMPLE_RATE,
            AudioFormat.CHANNEL_IN_MONO,
            AudioFormat.ENCODING_PCM_16BIT,
            bufferSize
        )

        isListening = true
        audioRecord?.startRecording()

        serviceScope.launch {
            val buffer = ShortArray(bufferSize)
            while (isListening) {
                val read = audioRecord?.read(buffer, 0, buffer.size) ?: 0
                if (read > 0) {
                    val bytes = ByteArray(read * 2)
                    for (i in 0 until read) {
                        bytes[i * 2] = (buffer[i].toInt() and 0xFF).toByte()
                        bytes[i * 2 + 1] = (buffer[i].toInt() shr 8 and 0xFF).toByte()
                    }
                    if (recognizer?.acceptWaveForm(bytes, bytes.size) == true) {
                        val result = recognizer?.result ?: ""
                        if (result.contains(WAKE_PHRASE, ignoreCase = true)) {
                            onWakeWordDetected()
                        }
                    }
                }
            }
        }
    }

    private fun onWakeWordDetected() {
        TunzyStateHolder.setState(TunzyState.WAKE)
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP
        }
        startActivity(intent)
        val geminiIntent = Intent(this, GeminiVoiceService::class.java)
        startService(geminiIntent)
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            CHANNEL_ID, "TUNZY Wake Word", NotificationManager.IMPORTANCE_LOW
        ).apply {
            description = "Listening for Hey Tunzy"
            setShowBadge(false)
        }
        getSystemService(NotificationManager::class.java).createNotificationChannel(channel)
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
        isListening = false
        audioRecord?.stop()
        audioRecord?.release()
        recognizer?.close()
        model?.close()
        serviceScope.cancel()
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null
}