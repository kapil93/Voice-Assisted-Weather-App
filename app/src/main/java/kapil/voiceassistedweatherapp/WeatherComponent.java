package kapil.voiceassistedweatherapp;

import dagger.Component;
import kapil.voiceassistedweatherapp.app.AppComponent;
import kapil.voiceassistedweatherapp.weather.WeatherDataProviderModule;

/**
 * This component encapsulates the functionality of taking voice input, retrieving location data,
 * retrieving weather data and displaying the data.
 */

@ActivityScope
@Component(dependencies = AppComponent.class, modules = {WeatherPresenterModule.class, WeatherDataProviderModule.class})
interface WeatherComponent {
    void inject(WeatherActivity weatherActivity);
}
