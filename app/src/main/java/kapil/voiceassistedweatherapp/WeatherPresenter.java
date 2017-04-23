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
    private WeatherContract.View view;

    @Inject SpeechRecognizer speechRecognizer;
    private Intent speechIntent;

    @Inject WeatherDataProvider weatherDataProvider;

    private String latestRequestedString;

    @Inject WeatherPresenter(Context context, WeatherContract.View view) {
        this.context = context;
        this.view = view;

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
            view.showLoader(true);
            weatherDataProvider.requestWeatherData(latestRequestedString);
        }
    }

    @Override
    public void onReadyForSpeech(Bundle params) {
        Log.i(TAG, "onReady");
        if (context != null) {
            view.setVoiceString(context.getString(R.string.voice_listening));
        }
        view.setVoiceListeningAnimationEnabled(true);
    }

    @Override
    public void onBeginningOfSpeech() {
        Log.i(TAG, "onBegin");
    }

    @Override
    public void onRmsChanged(float rmsdB) {
        Log.i(TAG, "onRmsChanged: " + rmsdB);
    }

    @Override
    public void onBufferReceived(byte[] buffer) {
        Log.i(TAG, "onBufferReceived");
    }

    @Override
    public void onEndOfSpeech() {
        Log.i(TAG, "onEnd");
        view.setVoiceListeningAnimationEnabled(false);
    }

    @Override
    public void onError(int error) {
        Log.i(TAG, "showToastErrorMessage: " + error);
        if (context != null) {
            view.setVoiceString(context.getString(R.string.voice_button_promt));
        }
        view.setVoiceListeningAnimationEnabled(false);
    }

    @Override
    public void onResults(Bundle results) {
        ArrayList resultList = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        String bestResult = (String) (resultList != null ? resultList.get(0) : null);
        Log.i(TAG, "onResult: " + bestResult);
        view.setVoiceString(bestResult);
        latestRequestedString = bestResult;

        if (weatherDataProvider != null) {
            view.showLoader(true);
            weatherDataProvider.requestWeatherData(bestResult);
        }
    }

    @Override
    public void onPartialResults(Bundle partialResults) {
        ArrayList resultList = partialResults.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        String bestResult = (String) (resultList != null ? resultList.get(0) : null);
        Log.i(TAG, "onPartialResult: " + bestResult);
        view.setVoiceString(String.format(bestResult + "%s", "..."));
    }

    @Override
    public void onEvent(int eventType, Bundle params) {
        Log.i(TAG, "onEvent");
    }

    @Override
    public void onWeatherDataReceived(WeatherData weatherData) {
        Log.i(TAG, "onWeatherDataReceived");
        view.showLoader(false);
        view.showNoInternetSnackbar(false);
        view.setWeatherData(weatherData);
        view.showWeatherDataViewContainer(true);
    }

    @Override
    public void onFailure(@WeatherResponseFailureType int failureType) {
        Log.e(TAG, String.valueOf(failureType));
        view.showLoader(false);
        int resId = 0;
        switch (failureType) {
            case NO_INTERNET:
                resId = R.string.no_internet;
                view.showNoInternetSnackbar(true);
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
        }
        if (resId != R.string.no_internet) {
            view.showToastErrorMessage(resId);
            view.showWeatherDataViewContainer(false);
        }
    }

    void destroy() {
        if (speechRecognizer != null) {
            speechRecognizer.destroy();
        }
    }
}
