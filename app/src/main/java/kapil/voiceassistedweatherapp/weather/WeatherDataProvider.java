package kapil.voiceassistedweatherapp.weather;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import javax.inject.Inject;

import kapil.voiceassistedweatherapp.weather.models.weather.WeatherData;
import kapil.voiceassistedweatherapp.weather.models.witai.WitAiResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by witworks on 18/04/17.
 */

public class WeatherDataProvider implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private static final String TAG = "WeatherDataProvider";

    private static final String WEATHER_API_KEY = "50c8597b2c9f17117947019b4bf354cc";
    private static final String WIT_AI_ACCESS_TOKEN = "NNXGXARQPAM2V2Q6TNK6NOA3OHPQJJ57";

    private static final int WEATHER_SERVICE = 0;
    private static final int WIT_AI_SERVICE = 1;

    //@Inject @Named("WIT_AI_SERVICE") Retrofit witAiRetrofit;
    //@Inject @Named("WEATHER_SERVICE") Retrofit weatherRetrofit;

    private final ApiCallService.WitAiService witAiService;
    private final ApiCallService.WeatherService weatherService;

    private GoogleApiClient googleApiClient;

    private OnWeatherDataReceivedListener onWeatherDataReceivedListener;

    @IntDef({WEATHER_SERVICE, WIT_AI_SERVICE})
    @interface ServiceType {

    }

    @Inject
    public WeatherDataProvider(Context context) {
        witAiService = call(WIT_AI_SERVICE).create(ApiCallService.WitAiService.class);
        weatherService = call(WEATHER_SERVICE).create(ApiCallService.WeatherService.class);

        initializeGoogleApiClient(context);
    }

    private void initializeGoogleApiClient(Context context) {
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(context)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Location location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);

        if (location != null) {
            getWeatherInfoForLocation(String.valueOf(location.getLatitude()), String.valueOf(location.getLongitude()));
        } else {
            onWeatherDataReceivedListener.onFailure(OnWeatherDataReceivedListener.GPS_UNAVAILABLE);
        }

        googleApiClient.disconnect();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public void setOnWeatherDataReceivedListener(OnWeatherDataReceivedListener onWeatherDataReceivedListener) {
        this.onWeatherDataReceivedListener = onWeatherDataReceivedListener;
    }

    /**
     * This method hits wit.ai api to make relevance out of the string obtained from voice request.
     *
     * Weather Intent and Location are detected and returned by the wit.ai service.
     *
     * @param string
     */

    public void requestWeatherData(String string) {
        Call<WitAiResponse> call = witAiService.fetchWitAiIntent(string, WIT_AI_ACCESS_TOKEN);
        call.enqueue(new Callback<WitAiResponse>() {
            @Override
            public void onResponse(Call<WitAiResponse> call, Response<WitAiResponse> response) {
                if (onWeatherDataReceivedListener != null) {
                    analyzeWitAiResponse(response.body());
                    Log.i(TAG, response.body().toString());
                }
            }

            @Override
            public void onFailure(Call<WitAiResponse> call, Throwable t) {
                if (onWeatherDataReceivedListener != null) {
                    onWeatherDataReceivedListener.onFailure(OnWeatherDataReceivedListener.NO_INTERNET);
                }
            }
        });
    }

    /**
     * This Method analyzes the response from wit.ai, checks for null values and sends the
     * appropriate error message to WeatherPresenter in case of errors.
     *
     * If there are no errors, it takes the location from wit.ai and feeds it to WeatherService
     * which in turn tells the weather.
     *
     * @param witAiResponse
     */

    private void analyzeWitAiResponse(WitAiResponse witAiResponse) {
        if (witAiResponse == null) {
            onWeatherDataReceivedListener.onFailure(OnWeatherDataReceivedListener.WITAI_NULL_RESPONSE);
        } else {
            if (witAiResponse.getEntities().getIntent() == null) {
                onWeatherDataReceivedListener.onFailure(OnWeatherDataReceivedListener.WEATHER_INTENT_NOT_FOUND);
            } else if (witAiResponse.getEntities().getIntent().get(0).getValue().equals("weather")) {
                if (witAiResponse.getEntities().getLocation() == null) {
                    googleApiClient.connect();
                } else {
                    String location = witAiResponse.getEntities().getLocation().get(0).getValue();
                    getWeatherInfoForLocation(location);
                }
            }
        }
    }

    /**
     * This method fetches the weather info from openweather api.
     *
     * @param location
     */

    private void getWeatherInfoForLocation(String location) {
        Call<WeatherData> call = weatherService.fetchWeatherData(location, WEATHER_API_KEY, "metric");
        call.enqueue(new Callback<WeatherData>() {
            @Override
            public void onResponse(Call<WeatherData> call, Response<WeatherData> response) {
                if (onWeatherDataReceivedListener != null) {
                    analyzeWeatherData(response.body());
                }
            }

            @Override
            public void onFailure(Call<WeatherData> call, Throwable t) {
                if (onWeatherDataReceivedListener != null) {
                    onWeatherDataReceivedListener.onFailure(OnWeatherDataReceivedListener.NO_INTERNET);
                }
            }
        });
    }

    /**
     * This method fetches the weather info from openweather api.
     *
     * @param latitude
     * @param longitude
     */

    private void getWeatherInfoForLocation(String latitude, String longitude) {
        Call<WeatherData> call = weatherService.fetchWeatherData(latitude, longitude, WEATHER_API_KEY, "metric");
        call.enqueue(new Callback<WeatherData>() {
            @Override
            public void onResponse(Call<WeatherData> call, Response<WeatherData> response) {
                if (onWeatherDataReceivedListener != null) {
                    analyzeWeatherData(response.body());
                }
            }

            @Override
            public void onFailure(Call<WeatherData> call, Throwable t) {
                if (onWeatherDataReceivedListener != null) {
                    onWeatherDataReceivedListener.onFailure(OnWeatherDataReceivedListener.NO_INTERNET);
                }
            }
        });
    }

    /**
     * This method analyzes the response from the WeatherService, checks for null values and sends
     * the appropriate error message to WeatherPresenter in case of errors.
     *
     * If there are no errors, it feeds the WeatherData to the WeatherPresenter which in turn sends
     * the data to MainActivity to get displayed on the screen.
     *
     * @param weatherData
     */

    private void analyzeWeatherData(WeatherData weatherData) {
        if (weatherData == null) {
            onWeatherDataReceivedListener.onFailure(OnWeatherDataReceivedListener.PLACE_UNRECOGNIZED);
        } else {
            onWeatherDataReceivedListener.onWeatherDataReceived(weatherData);
        }
    }

    private interface ApiCallService {
        interface WeatherService {
            @GET("data/2.5/weather")
            Call<WeatherData> fetchWeatherData(@Query("q") String place, @Query("APPID") String appId, @Query("units") String units);

            @GET("data/2.5/weather")
            Call<WeatherData> fetchWeatherData(@Query("lat") String latitude, @Query("lon") String longitude, @Query("APPID") String appId, @Query("units") String units);
        }

        interface WitAiService {
            @GET("message")
            Call<WitAiResponse> fetchWitAiIntent(@Query("q") String string, @Query("access_token") String access_token);
        }
    }

    private static Retrofit call(@ServiceType int serviceType) {
        switch (serviceType) {
            case WEATHER_SERVICE:
                return new Retrofit.Builder()
                        .baseUrl("http://api.openweathermap.org/")
                        .addConverterFactory(JacksonConverterFactory.create())
                        .build();
            case WIT_AI_SERVICE:
                return new Retrofit.Builder()
                        .baseUrl("https://api.wit.ai/")
                        .addConverterFactory(JacksonConverterFactory.create())
                        .build();
            default:
                return null;
        }
    }
}
