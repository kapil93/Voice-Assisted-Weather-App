package kapil.voiceassistedweatherapp.app;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Component;

/**
 * This component is used to provide Context throughout the application.
 */

@Singleton
@Component(modules = ApplicationModule.class)
public interface AppComponent {
    Context getContext();
}
