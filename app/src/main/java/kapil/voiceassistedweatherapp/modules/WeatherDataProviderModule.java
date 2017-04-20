package kapil.voiceassistedweatherapp.modules;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

/**
 * Created by witworks on 18/04/17.
 */

@Module
public class WeatherDataProviderModule {

    @Provides
    @Named("WIT_AI_SERVICE")
    @Singleton
    Retrofit provideWitAiRetrofit() {
        return new Retrofit.Builder()
                .baseUrl("https://api.wit.ai/")
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
    }

    @Provides
    @Named("WEATHER_SERVICE")
    @Singleton
    Retrofit provideWeatherRetrofit() {
        return new Retrofit.Builder()
                .baseUrl("http://api.openweathermap.org/")
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
    }
}
