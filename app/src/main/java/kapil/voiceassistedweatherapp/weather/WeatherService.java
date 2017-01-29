package kapil.voiceassistedweatherapp.weather;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import kapil.voiceassistedweatherapp.weather.models.WeatherData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by kapil on 08-01-2017.
 */
public class WeatherService extends Service implements WeatherServiceInputProvider {
    private static final String WEATHER_API_KEY = "50c8597b2c9f17117947019b4bf354cc";

    private OnWeatherDataReceivedListener onWeatherDataReceivedListener;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return WeatherServiceBinder.newInstance(this);
    }

    public void setOnWeatherDataReceivedListener(OnWeatherDataReceivedListener onWeatherDataReceivedListener) {
        this.onWeatherDataReceivedListener = onWeatherDataReceivedListener;
    }

    @Override
    public void getWeatherInfo(final String place) {
        ApiCallService apiCallService = callService().create(ApiCallService.class);
        Call<WeatherData> call = apiCallService.weatherData(place, WEATHER_API_KEY, "metric");
        call.enqueue(new Callback<WeatherData>() {
            @Override
            public void onResponse(Call<WeatherData> call, Response<WeatherData> response) {
                if (onWeatherDataReceivedListener != null) {
                    onWeatherDataReceivedListener.onWeatherDataReceived(response.body(), OnWeatherDataReceivedListener.SUCCESS);
                }
            }

            @Override
            public void onFailure(Call<WeatherData> call, Throwable t) {
                if (onWeatherDataReceivedListener != null) {
                    onWeatherDataReceivedListener.onWeatherDataReceived(null, OnWeatherDataReceivedListener.FAILURE);
                }
            }
        });
    }

    public interface ApiCallService {
        @GET("data/2.5/weather")
        Call<WeatherData> weatherData(@Query("q") String place, @Query("APPID") String appId, @Query("units") String units);
    }

    public static Retrofit callService() {
        return new Retrofit.Builder()
                .baseUrl("http://api.openweathermap.org/")
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
    }
}
