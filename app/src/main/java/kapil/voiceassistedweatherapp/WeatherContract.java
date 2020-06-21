package kapil.voiceassistedweatherapp;

import androidx.annotation.IntDef;

import kapil.voiceassistedweatherapp.weather.models.weather.WeatherData;

/**
 * WeatherContract lays down the contract between the View {@link WeatherActivity} and the Presenter {@link WeatherPresenter}.
 */

interface WeatherContract {

    interface View {

        int MICROPHONE_PERMISSION_REQUEST = 4522;
        int LOCATION_PERMISSION_REQUEST = 3876;

        @IntDef({MICROPHONE_PERMISSION_REQUEST, LOCATION_PERMISSION_REQUEST})
        @interface Permission {

        }

        void setVoiceString(String string);

        void setVoiceListeningAnimationEnabled(boolean enabled);

        void showWeatherDataViewContainer(boolean show);

        void showLoader(boolean show);

        void showToastErrorMessage(int errorResId);

        void setWeatherData(WeatherData weatherData);

        void showNoInternetSnackbar(boolean show);

        void setVoiceListeningCircleAction(float rmsDb);

        void requestPermission(@Permission int permissionType);
    }

    interface Presenter {

        void triggerSpeechRecognizer();

        void retryDataFetch();
    }
}
