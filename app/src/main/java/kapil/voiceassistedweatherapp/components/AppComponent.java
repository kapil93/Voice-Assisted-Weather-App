package kapil.voiceassistedweatherapp.components;

import javax.inject.Singleton;

import dagger.Component;
import kapil.voiceassistedweatherapp.MainActivity;
import kapil.voiceassistedweatherapp.modules.ApplicationModule;
import kapil.voiceassistedweatherapp.modules.SpeechModule;
import kapil.voiceassistedweatherapp.modules.WeatherDataProviderModule;

/**
 * Created by Kapil on 12/03/17.
 */

@Singleton
@Component(modules = {ApplicationModule.class, SpeechModule.class, WeatherDataProviderModule.class})
public interface AppComponent {
    void inject(MainActivity mainActivity);
}
