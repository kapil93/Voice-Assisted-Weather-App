package kapil.voiceassistedweatherapp;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;

import java.util.ArrayList;
import java.util.Locale;

import kapil.voiceassistedweatherapp.weather.OnWeatherDataReceivedListener;
import kapil.voiceassistedweatherapp.weather.WeatherService;
import kapil.voiceassistedweatherapp.weather.WeatherServiceBinder;
import kapil.voiceassistedweatherapp.weather.WeatherServiceInputProvider;
import kapil.voiceassistedweatherapp.weather.models.WeatherData;
import kapil.voiceassistedweatherapp.witai.OnWitAiResponseListener;
import kapil.voiceassistedweatherapp.witai.WitAiService;
import kapil.voiceassistedweatherapp.witai.WitAiServiceBinder;
import kapil.voiceassistedweatherapp.witai.WitAiServiceInputProvider;
import kapil.voiceassistedweatherapp.witai.models.WitAiResponse;

/**
 * Created by Kapil on 29/01/17.
 */

public class WeatherPresenter implements RecognitionListener, OnWitAiResponseListener, OnWeatherDataReceivedListener {
    private static final String SPEECH_TAG = "Speech Recognition";

    private Context context;
    private ViewDataProvider viewDataProvider;

    private SpeechRecognizer speechRecognizer;
    private Intent speechIntent;

    private Intent witAiServiceIntent;
    private WitAiServiceInputProvider witAiServiceInputProvider;
    private ServiceConnection witAiServiceConnection;

    private Intent weatherServiceIntent;
    private WeatherServiceInputProvider weatherServiceInputProvider;
    private ServiceConnection weatherServiceConnection;

    private String latestRequestedString;

    public WeatherPresenter(Context context, ViewDataProvider viewDataProvider) {
        this.context = context;
        this.viewDataProvider = viewDataProvider;

        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context);
        speechRecognizer.setRecognitionListener(this);

        createSpeechIntent();
        createWitAiServiceConnection();
        createWeatherServiceConnection();

        latestRequestedString = "";
    }

    private void createSpeechIntent() {
        speechIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        speechIntent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true);
    }

    private void createWitAiServiceConnection() {
        witAiServiceIntent = new Intent(context, WitAiService.class);
        witAiServiceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                WitAiServiceBinder binder = (WitAiServiceBinder) iBinder;
                witAiServiceInputProvider = binder.getWitAiServiceInputProvider();
                witAiServiceInputProvider.setOnWitAiResponseListener(WeatherPresenter.this);
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {

            }
        };
        viewDataProvider.onWitAiServiceInitialized(witAiServiceIntent, witAiServiceConnection);
    }

    private void createWeatherServiceConnection() {
        weatherServiceIntent = new Intent(context, WeatherService.class);
        weatherServiceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                WeatherServiceBinder binder = (WeatherServiceBinder) iBinder;
                weatherServiceInputProvider = binder.getWeatherServiceInputProvider();
                weatherServiceInputProvider.setOnWeatherDataReceivedListener(WeatherPresenter.this);
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {

            }
        };
        viewDataProvider.onWeatherServiceInitialized(weatherServiceIntent, weatherServiceConnection);
    }

    public void onVoiceButtonClick() {
        speechRecognizer.startListening(speechIntent);
    }

    public void onRetryButtonClick() {
        if (witAiServiceInputProvider != null) {
            viewDataProvider.onRequest();
            witAiServiceInputProvider.requestIntentInfo(latestRequestedString);
        }
    }

    @Override
    public void onReadyForSpeech(Bundle params) {
        Log.i(SPEECH_TAG, "onReady");
        viewDataProvider.onVoiceStringUpdate(context.getString(R.string.voice_listening));
        viewDataProvider.onListeningStateChange(true);
    }

    @Override
    public void onBeginningOfSpeech() {
        Log.i(SPEECH_TAG, "onBegin");
    }

    @Override
    public void onRmsChanged(float rmsdB) {
        Log.i(SPEECH_TAG, "onRmsChanged: " + rmsdB);
    }

    @Override
    public void onBufferReceived(byte[] buffer) {
        Log.i(SPEECH_TAG, "onBufferReceived");
    }

    @Override
    public void onEndOfSpeech() {
        Log.i(SPEECH_TAG, "onEnd");
        viewDataProvider.onListeningStateChange(false);
    }

    @Override
    public void onError(int error) {
        Log.i(SPEECH_TAG, "onError: " + error);
        viewDataProvider.onVoiceStringUpdate(context.getString(R.string.voice_button_promt));
        viewDataProvider.onListeningStateChange(false);
    }

    @Override
    public void onResults(Bundle results) {
        ArrayList resultList = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        String bestResult = (String) resultList.get(0);
        Log.i(SPEECH_TAG, "onResult: " + bestResult);
        viewDataProvider.onVoiceStringUpdate(bestResult);
        latestRequestedString = bestResult;

        if (witAiServiceInputProvider != null) {
            viewDataProvider.onRequest();
            witAiServiceInputProvider.requestIntentInfo(bestResult);
        }
    }

    @Override
    public void onPartialResults(Bundle partialResults) {
        ArrayList resultList = partialResults.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        String bestResult = (String) resultList.get(0);
        Log.i(SPEECH_TAG, "onPartialResult: " + bestResult);
        viewDataProvider.onVoiceStringUpdate(String.format(bestResult + "%s", "..."));
    }

    @Override
    public void onEvent(int eventType, Bundle params) {
        Log.i(SPEECH_TAG, "onEvent");
    }

    @Override
    public void onWitAiResponse(WitAiResponse witAiResponse, @WitAiResponseType int responseType) {
        switch (responseType) {
            case OnWitAiResponseListener.SUCCESS:
                analyzeWitAiResponse(witAiResponse);
                break;
            case OnWitAiResponseListener.FAILURE:
                viewDataProvider.onError(context.getString(R.string.no_internet));
                break;
        }
    }

    @Override
    public void onWeatherDataReceived(WeatherData weatherData, @WeatherApiResponseType int responseType) {
        switch (responseType) {
            case OnWitAiResponseListener.SUCCESS:
                analyzeWeatherData(weatherData);
                break;
            case OnWitAiResponseListener.FAILURE:
                viewDataProvider.onError(context.getString(R.string.no_internet));
                break;
        }
    }

    private void analyzeWitAiResponse(WitAiResponse witAiResponse) {
        if (witAiResponse == null) {
            viewDataProvider.onError(context.getString(R.string.null_wit_ai_response));
        } else {
            if (witAiResponse.getEntities().getIntent() == null) {
                viewDataProvider.onError(context.getString(R.string.weather_intent_not_found));
            } else if (witAiResponse.getEntities().getIntent().get(0).getValue().equals("weather")) {
                if (witAiResponse.getEntities().getLocation() == null) {
                    // TODO: Show weather in current location if location not found in the string
                    viewDataProvider.onError(context.getString(R.string.location_not_found));
                } else {
                    String location = witAiResponse.getEntities().getLocation().get(0).getValue();
                    if (weatherServiceInputProvider != null) {
                        weatherServiceInputProvider.getWeatherInfo(location);
                    }
                }
            }
        }
    }

    private void analyzeWeatherData(WeatherData weatherData) {
        if (weatherData == null) {
            viewDataProvider.onError(context.getString(R.string.place_unrecognized));
        } else {
            viewDataProvider.onWeatherDataReceived(weatherData);
        }
    }

    public void destroy() {
        speechRecognizer.destroy();
    }
}
