# TUNZY - Personal AI Assistant

## Before Building

1. Get FREE Gemini API key: https://aistudio.google.com/apikey
2. Paste it in `GeminiVoiceService.kt` replacing `YOUR_GEMINI_API_KEY_HERE`

## Vosk Wake Word Model

1. Download: https://alphacephei.com/vosk/models/vosk-model-small-en-us-0.15.zip
2. Unzip it
3. Upload the folder to: `app/src/main/assets/vosk-model-small-en-us-0.15/`

## How It Works

Say **"Hey Tunzy"** → orb activates → speak → Gemini answers → TTS speaks it back

## Orb Colors

| Color | Meaning |
|-------|---------|
| Purple slow pulse | Idle |
| Bright cyan | Wake detected |
| Deep purple fast | Listening |
| Blue | Thinking |
| Cyan rapid | Speaking |