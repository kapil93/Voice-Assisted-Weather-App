package kapil.voiceassistedweatherapp.weather;

import android.support.annotation.IntDef;

import kapil.voiceassistedweatherapp.weather.models.WeatherData;

/**
 * Created by kapil on 08-01-2017.
 */
public interface OnWeatherDataReceivedListener {
    @IntDef({SUCCESS, FAILURE})
    @interface WeatherApiResponseType {

    }

    int SUCCESS = 0;
    int FAILURE = 1;

    void onWeatherDataReceived(WeatherData weatherData, @WeatherApiResponseType int responseType);
}
