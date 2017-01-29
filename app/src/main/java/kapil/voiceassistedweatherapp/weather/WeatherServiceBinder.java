package kapil.voiceassistedweatherapp.weather;

import android.os.Binder;

/**
 * Created by kapil on 08-01-2017.
 */
public class WeatherServiceBinder extends Binder {
    private WeatherServiceInputProvider weatherServiceInputProvider;

    public WeatherServiceBinder(WeatherServiceInputProvider weatherServiceInputProvider) {
        this.weatherServiceInputProvider = weatherServiceInputProvider;
    }

    public static WeatherServiceBinder newInstance(WeatherServiceInputProvider weatherServiceInputProvider) {
        return new WeatherServiceBinder(weatherServiceInputProvider);
    }

    public WeatherServiceInputProvider getWeatherServiceInputProvider() {
        return weatherServiceInputProvider;
    }
}
