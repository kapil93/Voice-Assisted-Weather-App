package kapil.voiceassistedweatherapp.modules;

import android.content.Context;
import android.speech.SpeechRecognizer;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import kapil.voiceassistedweatherapp.VoiceAssistedWeatherApp;

/**
 * Created by Kapil on 12/03/17.
 */

@Module
public class SpeechModule {
    private VoiceAssistedWeatherApp voiceAssistedWeatherApp;

    public SpeechModule(VoiceAssistedWeatherApp voiceAssistedWeatherApp) {
        this.voiceAssistedWeatherApp = voiceAssistedWeatherApp;
    }

    @Singleton
    @Provides
    SpeechRecognizer provideSpeechRecognizer() {
        return SpeechRecognizer.createSpeechRecognizer(voiceAssistedWeatherApp);
    }
}