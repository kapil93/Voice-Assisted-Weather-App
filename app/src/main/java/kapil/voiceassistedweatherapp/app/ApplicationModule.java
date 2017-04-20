package kapil.voiceassistedweatherapp.app;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * This module is used to provide Context throughout the application.
 */

@Module
class ApplicationModule {
    private final Context context;

    ApplicationModule(Context context) {
        this.context = context;
    }

    @Provides
    @Singleton
    Context provideApplicationContext() {
        return context;
    }
}
