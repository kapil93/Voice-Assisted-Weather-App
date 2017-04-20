package kapil.voiceassistedweatherapp;

import kapil.voiceassistedweatherapp.weather.models.weather.WeatherData;

/**
 * Created by witworks on 14/04/17.
 */

public interface WeatherContract {

    interface View {

        void setVoiceString(String string);

        void setVoiceListeningAnimationEnabled(boolean enabled);

        void showWeatherDataViewContainer(boolean show);

        void showLoader(boolean show);

        void showToastErrorMessage(int errorResId);

        void showWeatherData(WeatherData weatherData);

        void showNoInternetSnackbar(boolean show);
    }

    interface Presenter {

        void triggerSpeechRecognizer();

        void retryDataFetch();
    }
}
