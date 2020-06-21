package kapil.voiceassistedweatherapp.weather;

import androidx.annotation.IntDef;

import kapil.voiceassistedweatherapp.weather.models.weather.WeatherData;

/**
 * This interface provides appropriate callback related to the response obtained from
 * {@link WeatherDataProvider}.
 */

public interface OnWeatherDataReceivedListener {
    @IntDef({NO_INTERNET, GPS_UNAVAILABLE, WIT_AI_NULL_RESPONSE, WEATHER_INTENT_NOT_FOUND, PLACE_UNRECOGNIZED, LOCATION_PERMISSION_DENIED})
    @interface WeatherResponseFailureType {

    }

    int NO_INTERNET = 0;
    int GPS_UNAVAILABLE = 1;
    int WIT_AI_NULL_RESPONSE = 2;
    int WEATHER_INTENT_NOT_FOUND = 3;
    int PLACE_UNRECOGNIZED = 4;
    int LOCATION_PERMISSION_DENIED = 5;

    void onWeatherDataReceived(WeatherData weatherData);

    void onFailure(@WeatherResponseFailureType int failureType);
}
