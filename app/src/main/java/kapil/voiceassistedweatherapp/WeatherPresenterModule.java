package kapil.voiceassistedweatherapp;

import android.content.Context;
import android.speech.SpeechRecognizer;

import dagger.Module;
import dagger.Provides;

/**
 * This module is used to pass dependencies of {@link WeatherPresenter}.
 */

@Module
class WeatherPresenterModule {

    private WeatherContract.View view;

    WeatherPresenterModule(WeatherContract.View view) {
        this.view = view;
    }

    @Provides
    WeatherContract.View provideWeatherContractView() {
        return view;
    }

    @Provides
    SpeechRecognizer provideSpeechRecognizer(Context context) {
        return SpeechRecognizer.createSpeechRecognizer(context);
    }
}
