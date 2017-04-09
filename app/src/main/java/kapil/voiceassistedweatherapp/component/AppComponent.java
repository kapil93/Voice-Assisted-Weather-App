package kapil.voiceassistedweatherapp.component;

import android.app.Activity;

import javax.inject.Singleton;

import dagger.Component;
import kapil.voiceassistedweatherapp.MainActivity;
import kapil.voiceassistedweatherapp.WeatherPresenter;
import kapil.voiceassistedweatherapp.modules.SpeechModule;
import kapil.voiceassistedweatherapp.modules.WeatherPresenterModule;

/**
 * Created by Kapil on 12/03/17.
 */

@Singleton
@Component(modules = {WeatherPresenterModule.class, SpeechModule.class})
public interface AppComponent {
    void inject(MainActivity mainActivity);
}
