package kapil.voiceassistedweatherapp;

import android.content.Intent;
import android.content.ServiceConnection;

import kapil.voiceassistedweatherapp.weather.models.WeatherData;

/**
 * Created by Kapil on 29/01/17.
 */

public interface ViewDataProvider {
    void onWitAiServiceInitialized(Intent witAiServiceIntent, ServiceConnection witAiServiceConnection);

    void onWeatherServiceInitialized(Intent weatherServiceIntent, ServiceConnection weatherServiceConnection);

    void onVoiceStringUpdate(String string);

    void onListeningStateChange(boolean isListening);

    void onRequest();

    void onError(int errorResId);

    void onWeatherDataReceived(WeatherData weatherData);
}
