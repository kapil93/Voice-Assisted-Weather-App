package kapil.voiceassistedweatherapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;

import java.util.ArrayList;
import java.util.Locale;

import javax.inject.Inject;

import kapil.voiceassistedweatherapp.weather.OnWeatherDataReceivedListener;
import kapil.voiceassistedweatherapp.weather.WeatherDataProvider;
import kapil.voiceassistedweatherapp.weather.models.weather.WeatherData;

/**
 * WeatherPresenter (Presenter) sends appropriate request to {@link WeatherDataProvider} using the input
 * provided by {@link WeatherActivity} (View) and sends back the result obtained from WeatherDataProvider.
 */

public class WeatherPresenter implements WeatherContract.Presenter, RecognitionListener, OnWeatherDataReceivedListener {
    private static final String TAG = "WeatherPresenter";

    private Context context;
    private WeatherContract.View weatherView;

    @Inject SpeechRecognizer speechRecognizer;
    private Intent speechIntent;

    @Inject WeatherDataProvider weatherDataProvider;

    private String latestRequestedString;

    @Inject WeatherPresenter(Context context, WeatherContract.View weatherView) {
        this.context = context;
        this.weatherView = weatherView;

        setUpSpeechRecognizer();
        initializeWeatherService();

        latestRequestedString = "";
    }

    @Inject void setUpSpeechRecognizer() {
        if (speechRecognizer != null) {
            speechRecognizer.setRecognitionListener(this);

            speechIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
            speechIntent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true);
        }
    }

    @Inject void initializeWeatherService() {
        if (weatherDataProvider != null) {
            weatherDataProvider.setOnWeatherDataReceivedListener(this);
        }
    }

    @Override
    public void triggerSpeechRecognizer() {
        if (speechRecognizer != null) {
            speechRecognizer.startListening(speechIntent);
        }
    }

    @Override
    public void retryDataFetch() {
        if (weatherDataProvider != null) {
            weatherView.showLoader(true);
            weatherDataProvider.requestWeatherData(latestRequestedString);
        }
    }

    @Override
    public void onReadyForSpeech(Bundle params) {
        Log.i(TAG, "onReadyForSpeech");
        if (context != null) {
            weatherView.setVoiceString(context.getString(R.string.voice_listening));
        }
        weatherView.setVoiceListeningAnimationEnabled(true);
    }

    @Override
    public void onBeginningOfSpeech() {
        Log.i(TAG, "onBeginningOfSpeech");
    }

    @Override
    public void onRmsChanged(float rmsDb) {
        Log.i(TAG, "onRmsChanged: " + rmsDb);
        weatherView.setVoiceListeningCircleAction(rmsDb);
    }

    @Override
    public void onBufferReceived(byte[] buffer) {
        Log.i(TAG, "onBufferReceived");
    }

    @Override
    public void onEndOfSpeech() {
        Log.i(TAG, "onEnd");
        weatherView.setVoiceListeningAnimationEnabled(false);
    }

    @Override
    public void onError(int error) {
        Log.e(TAG, "onSpeechError: " + error);
        if (error == SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS) {
            weatherView.requestPermission(WeatherContract.View.MICROPHONE_PERMISSION_REQUEST);
        }
        if (context != null) {
            weatherView.setVoiceString(context.getString(R.string.voice_button_promt));
        }
        weatherView.setVoiceListeningAnimationEnabled(false);
    }

    @Override
    public void onResults(Bundle results) {
        ArrayList resultList = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        String bestResult = (String) (resultList != null ? resultList.get(0) : null);
        Log.i(TAG, "onResult: " + bestResult);
        weatherView.setVoiceString(bestResult);
        latestRequestedString = bestResult;

        if (weatherDataProvider != null) {
            weatherView.showLoader(true);
            weatherDataProvider.requestWeatherData(bestResult);
        }
    }

    @Override
    public void onPartialResults(Bundle partialResults) {
        ArrayList resultList = partialResults.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        String bestResult = (String) (resultList != null ? resultList.get(0) : null);
        Log.i(TAG, "onPartialResult: " + bestResult);
        weatherView.setVoiceString(String.format(bestResult + "%s", "..."));
    }

    @Override
    public void onEvent(int eventType, Bundle params) {
        Log.i(TAG, "onSpeechEvent");
    }

    @Override
    public void onWeatherDataReceived(WeatherData weatherData) {
        Log.i(TAG, "onWeatherDataReceived");
        weatherView.showLoader(false);
        weatherView.showNoInternetSnackbar(false);
        weatherView.setWeatherData(weatherData);
        weatherView.showWeatherDataViewContainer(true);
    }

    @Override
    public void onFailure(@WeatherResponseFailureType int failureType) {
        Log.e(TAG, String.valueOf(failureType));
        weatherView.showLoader(false);
        weatherView.showWeatherDataViewContainer(false);
        int resId = 0;
        switch (failureType) {
            case NO_INTERNET:
                resId = R.string.no_internet;
                weatherView.showNoInternetSnackbar(true);
                break;
            case GPS_UNAVAILABLE:
                resId = R.string.gps_unavailable;
                break;
            case WIT_AI_NULL_RESPONSE:
                resId = R.string.null_wit_ai_response;
                break;
            case WEATHER_INTENT_NOT_FOUND:
                resId = R.string.weather_intent_not_found;
                break;
            case PLACE_UNRECOGNIZED:
                resId = R.string.place_unrecognized;
                break;
            case LOCATION_PERMISSION_DENIED:
                resId = R.string.location_permission_denied;
                weatherView.requestPermission(WeatherContract.View.LOCATION_PERMISSION_REQUEST);
                break;
        }
        if ((resId != R.string.no_internet) && (resId != R.string.location_permission_denied)) {
            weatherView.showToastErrorMessage(resId);
        }
    }

    void destroy() {
        if (speechRecognizer != null) {
            speechRecognizer.destroy();
        }
    }
}
