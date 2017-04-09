package kapil.voiceassistedweatherapp;

import android.app.Application;

import kapil.voiceassistedweatherapp.component.AppComponent;
import kapil.voiceassistedweatherapp.component.DaggerAppComponent;
import kapil.voiceassistedweatherapp.modules.SpeechModule;
import kapil.voiceassistedweatherapp.modules.WeatherPresenterModule;

/**
 * Created by Kapil on 12/03/17.
 */

public class VoiceAssistedWeatherApp extends Application {
    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        appComponent = DaggerAppComponent
                .builder()
                .weatherPresenterModule(new WeatherPresenterModule(this))
                .speechModule(new SpeechModule(this))
                .build();
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }
}
