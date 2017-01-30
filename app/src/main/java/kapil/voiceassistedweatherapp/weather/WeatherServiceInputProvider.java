package kapil.voiceassistedweatherapp.weather;

/**
 * Created by kapil on 08-01-2017.
 */
public interface WeatherServiceInputProvider {
    void setOnWeatherDataReceivedListener(OnWeatherDataReceivedListener onWeatherDataReceivedListener);
    void getWeatherInfo(String place);
}
