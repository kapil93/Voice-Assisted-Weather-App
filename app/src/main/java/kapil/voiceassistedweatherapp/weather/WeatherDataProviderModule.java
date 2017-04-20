package kapil.voiceassistedweatherapp.weather;

import android.content.Context;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

/**
 * This module is used to pass dependencies of {@link WeatherDataProvider}.
 */

@Module
public class WeatherDataProviderModule {

    @Provides
    @Named("WIT_AI_SERVICE")
    Retrofit provideWitAiRetrofit() {
        return new Retrofit.Builder()
                .baseUrl("https://api.wit.ai/")
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
    }

    @Provides
    @Named("WEATHER_SERVICE")
    Retrofit provideWeatherRetrofit() {
        return new Retrofit.Builder()
                .baseUrl("http://api.openweathermap.org/")
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
    }

    @Provides
    GoogleApiClient provideGoogleApiClient(Context context) {
        return new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API)
                .build();
    }
}
