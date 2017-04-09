package kapil.voiceassistedweatherapp.modules;

import android.speech.SpeechRecognizer;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import kapil.voiceassistedweatherapp.VoiceAssistedWeatherApp;
import kapil.voiceassistedweatherapp.WeatherPresenter;

/**
 * Created by Kapil on 10/04/17.
 */

@Module
public class WeatherPresenterModule {
    private VoiceAssistedWeatherApp voiceAssistedWeatherApp;

    public WeatherPresenterModule(VoiceAssistedWeatherApp voiceAssistedWeatherApp) {
        this.voiceAssistedWeatherApp = voiceAssistedWeatherApp;
    }

    @Singleton
    @Provides
    WeatherPresenter provideWeatherPresenter(SpeechRecognizer speechRecognizer) {
        return new WeatherPresenter(voiceAssistedWeatherApp, speechRecognizer);
    }
}
