package kapil.voiceassistedweatherapp.app;

import android.app.Application;

/**
 * This application performs the following functionalities:
 *
 * -> Takes a voice command from the user.
 *
 * -> Detects weather intent and location from the string and gets last known location from the
 *    device in absence of location in the command string.
 *
 * -> Fetches weather data for the location detected and displays the same.
 */

public class VoiceAssistedWeatherApp extends Application {
    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        appComponent = DaggerAppComponent
                .builder()
                .applicationModule(new ApplicationModule(getApplicationContext()))
                .build();
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }
}
