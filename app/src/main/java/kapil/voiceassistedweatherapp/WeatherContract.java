package kapil.voiceassistedweatherapp;

import kapil.voiceassistedweatherapp.weather.models.weather.WeatherData;

/**
 * WeatherContract lays down the contract between the View {@link WeatherActivity} and the Presenter {@link WeatherPresenter}.
 */

interface WeatherContract {

    interface View {

        void setVoiceString(String string);

        void setVoiceListeningAnimationEnabled(boolean enabled);

        void showWeatherDataViewContainer(boolean show);

        void showLoader(boolean show);

        void showToastErrorMessage(int errorResId);

        void setWeatherData(WeatherData weatherData);

        void showNoInternetSnackbar(boolean show);

        void setVoiceListeningCircleAction(float rmsDb);
    }

    interface Presenter {

        void triggerSpeechRecognizer();

        void retryDataFetch();
    }
}
