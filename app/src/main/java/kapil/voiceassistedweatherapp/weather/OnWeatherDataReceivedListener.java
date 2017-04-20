package kapil.voiceassistedweatherapp.weather;

import android.support.annotation.IntDef;

import kapil.voiceassistedweatherapp.weather.models.weather.WeatherData;

/**
 * This interface provides appropriate callback related to the response obtained from
 * {@link WeatherDataProvider}.
 */

public interface OnWeatherDataReceivedListener {
    @IntDef({NO_INTERNET, GPS_UNAVAILABLE, WIT_AI_NULL_RESPONSE, WEATHER_INTENT_NOT_FOUND, PLACE_UNRECOGNIZED})
    @interface WeatherResponseFailureType {

    }

    int NO_INTERNET = 0;
    int GPS_UNAVAILABLE = 1;
    int WIT_AI_NULL_RESPONSE = 2;
    int WEATHER_INTENT_NOT_FOUND = 3;
    int PLACE_UNRECOGNIZED = 4;

    void onWeatherDataReceived(WeatherData weatherData);

    void onFailure(@WeatherResponseFailureType int failureType);
}
