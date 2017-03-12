package kapil.voiceassistedweatherapp.component;

import javax.inject.Singleton;

import dagger.Component;
import kapil.voiceassistedweatherapp.WeatherPresenter;
import kapil.voiceassistedweatherapp.modules.SpeechModule;

/**
 * Created by Kapil on 12/03/17.
 */

@Singleton
@Component(modules = SpeechModule.class)
public interface AppComponent {
    void inject(WeatherPresenter weatherPresenter);
}
