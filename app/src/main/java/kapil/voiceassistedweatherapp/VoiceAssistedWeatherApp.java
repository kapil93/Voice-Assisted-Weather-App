package kapil.voiceassistedweatherapp;

import android.app.Application;

import kapil.voiceassistedweatherapp.components.AppComponent;
import kapil.voiceassistedweatherapp.components.DaggerAppComponent;
import kapil.voiceassistedweatherapp.modules.ApplicationModule;
import kapil.voiceassistedweatherapp.modules.SpeechModule;
import kapil.voiceassistedweatherapp.modules.WeatherDataProviderModule;

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
                .applicationModule(new ApplicationModule(getApplicationContext()))
                .speechModule(new SpeechModule(this))
                .weatherDataProviderModule(new WeatherDataProviderModule())
                .build();
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }
}
