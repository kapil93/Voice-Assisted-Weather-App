package kapil.voiceassistedweatherapp.weather;

import android.content.Context;
import android.util.Log;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import kapil.voiceassistedweatherapp.R;
import kapil.voiceassistedweatherapp.weather.models.weather.WeatherData;
import kapil.voiceassistedweatherapp.weather.models.witai.WitAiResponse;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * WeatherDataProvider takes the voice string as an input, detects weather intent and location from
 * the string and provides weather data through {@link OnWeatherDataReceivedListener}.
 */

public class WeatherDataProvider {
    private static final String TAG = "WeatherDataProvider";

    private static final String WEATHER_API_KEY = "50c8597b2c9f17117947019b4bf354cc";
    private static final String WIT_AI_ACCESS_TOKEN = "NNXGXARQPAM2V2Q6TNK6NOA3OHPQJJ57";

    private final Context context;

    @Inject
    @Named("WIT_AI_SERVICE")
    Retrofit witAiRetrofit;
    @Inject
    @Named("WEATHER_SERVICE")
    Retrofit weatherRetrofit;

    private ApiCallService.WitAiService witAiService;
    private ApiCallService.WeatherService weatherService;

    @Inject
    LocationProvider locationProvider;

    private OnWeatherDataReceivedListener onWeatherDataReceivedListener;
    private Disposable disposable;

    @Inject
    WeatherDataProvider(Context context) {
        this.context = context;

        initializeWitAiService();
        initializeWeatherService();
    }

    @Inject
    void initializeWitAiService() {
        if (witAiRetrofit != null) {
            witAiService = witAiRetrofit.create(ApiCallService.WitAiService.class);
        }
    }

    @Inject
    void initializeWeatherService() {
        if (weatherRetrofit != null) {
            weatherService = weatherRetrofit.create(ApiCallService.WeatherService.class);
        }
    }

    public void setOnWeatherDataReceivedListener(OnWeatherDataReceivedListener onWeatherDataReceivedListener) {
        this.onWeatherDataReceivedListener = onWeatherDataReceivedListener;
    }

    /**
     * This method initiates a chain of network calls which either terminates with successful
     * retrieval of {@link WeatherData} or an appropriate error message which will in turn be
     * displayed on the screen.
     *
     * @param voiceString Voice string obtained from {@link android.speech.SpeechRecognizer}.
     */

    public void requestWeatherData(String voiceString) {
        if (onWeatherDataReceivedListener == null) {
            return;
        }

        disposable = witAiService.fetchWitAiIntent(voiceString, WIT_AI_ACCESS_TOKEN)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .filter(this::analyzeWitAiResponse)
                .observeOn(Schedulers.io())
                .flatMap(this::getWeatherDataObservable)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::analyzeWeatherData, throwable -> {
                    throwable.printStackTrace();
                    if (throwable.getMessage().equals(context.getString(R.string.gps_unavailable))) {
                        onWeatherDataReceivedListener.onFailure(OnWeatherDataReceivedListener.GPS_UNAVAILABLE);
                    } else {
                        onWeatherDataReceivedListener.onFailure(OnWeatherDataReceivedListener.NO_INTERNET);
                    }
                }, () -> disposable.dispose());
    }

    /**
     * This Method analyzes the response from wit.ai, checks for null values and sends the
     * appropriate error message to {@link kapil.voiceassistedweatherapp.WeatherPresenter} in case
     * of errors.
     *
     * @param witAiResponse Response body obtained from wit.ai service.
     *
     * @return true if there is weather intent in the wit.ai response, else false.
     */

    private boolean analyzeWitAiResponse(WitAiResponse witAiResponse) {
        if (witAiResponse == null) {
            onWeatherDataReceivedListener.onFailure(OnWeatherDataReceivedListener.WIT_AI_NULL_RESPONSE);
        } else {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                Log.i(TAG, "onWitAiResponse: " + objectMapper.writeValueAsString(witAiResponse));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }

            if (witAiResponse.getEntities().getIntent() == null) {
                onWeatherDataReceivedListener.onFailure(OnWeatherDataReceivedListener.WEATHER_INTENT_NOT_FOUND);
            } else if (witAiResponse.getEntities().getIntent().get(0).getValue().equals("weather")) {
                return true;
            }
        }
        return false;
    }

    /**
     * Resolves wit.ai response and returns appropriate weather data observable.
     *
     * If there is location data in wit.ai response it uses that location data to get an observable,
     * else gets device location from {@link LocationProvider} to return an observable.
     *
     * @param witAiResponse Response body obtained from wit.ai service.
     *
     * @return Weather Data Observable.
     */

    private Observable<WeatherData> getWeatherDataObservable(WitAiResponse witAiResponse) {
        if (witAiResponse.getEntities().getLocation() == null) {
            return locationProvider.getLocationObservable()
                    .observeOn(Schedulers.io())
                    .flatMap(location -> weatherService.fetchWeatherData(String.valueOf(location.getLatitude()), String.valueOf(location.getLongitude()), WEATHER_API_KEY, "metric"));
        } else {
            String location = witAiResponse.getEntities().getLocation().get(0).getValue();
            return weatherService.fetchWeatherData(location, WEATHER_API_KEY, "metric");
        }
    }

    /**
     * This method analyzes the response from the {@link ApiCallService.WeatherService}, checks for
     * null values and sends the appropriate error message to
     * {@link kapil.voiceassistedweatherapp.WeatherPresenter} in case of errors.
     *
     * If there are no errors, it feeds the WeatherData to the WeatherPresenter which in turn sends
     * the data to {@link kapil.voiceassistedweatherapp.WeatherActivity} to get displayed on the
     * screen.
     *
     * @param weatherData Response body obtained from open weather api.
     */

    private void analyzeWeatherData(WeatherData weatherData) {
        if (weatherData == null) {
            onWeatherDataReceivedListener.onFailure(OnWeatherDataReceivedListener.PLACE_UNRECOGNIZED);
        } else {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                Log.i(TAG, "onWeatherResponse: " + objectMapper.writeValueAsString(weatherData));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }

            onWeatherDataReceivedListener.onWeatherDataReceived(weatherData);
        }
    }

    private interface ApiCallService {
        interface WeatherService {
            @GET("data/2.5/weather")
            Observable<WeatherData> fetchWeatherData(@Query("q") String place, @Query("APPID") String appId, @Query("units") String units);

            @GET("data/2.5/weather")
            Observable<WeatherData> fetchWeatherData(@Query("lat") String latitude, @Query("lon") String longitude, @Query("APPID") String appId, @Query("units") String units);
        }

        interface WitAiService {
            @GET("message")
            Observable<WitAiResponse> fetchWitAiIntent(@Query("q") String string, @Query("access_token") String access_token);
        }
    }
}
