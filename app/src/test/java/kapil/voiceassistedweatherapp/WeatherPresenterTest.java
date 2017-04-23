package kapil.voiceassistedweatherapp;

import android.content.Context;
import android.os.Bundle;
import android.speech.SpeechRecognizer;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;

import kapil.voiceassistedweatherapp.weather.OnWeatherDataReceivedListener;
import kapil.voiceassistedweatherapp.weather.WeatherDataProvider;
import kapil.voiceassistedweatherapp.weather.models.weather.WeatherData;

import static org.mockito.Mockito.verify;

/**
 * This class contains unit tests for {@link WeatherPresenter}.
 */

@RunWith(MockitoJUnitRunner.class)
public class WeatherPresenterTest {
    @Mock
    private WeatherContract.View view;

    @Mock
    private Context context;

    @Mock
    private Bundle voiceResult;

    @Mock
    private WeatherDataProvider weatherDataProvider;

    @Mock
    private OnWeatherDataReceivedListener onWeatherDataReceivedListener;

    @Mock
    private WeatherData weatherData;

    private WeatherPresenter weatherPresenter;

    @Before
    public void setUp() throws Exception {
        weatherPresenter = new WeatherPresenter(context, view);

        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("Voice Output String");

        voiceResult = new Bundle();
        voiceResult.putStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION, arrayList);

        weatherDataProvider = Mockito.mock(WeatherDataProvider.class);
        weatherDataProvider.setOnWeatherDataReceivedListener(onWeatherDataReceivedListener);

        weatherPresenter.weatherDataProvider = weatherDataProvider;
    }

    @Test()
    public void checkSettingOfVoiceStringAndVoiceListeningAnimationStartWhenSpeechRecognizerIsReady() throws Exception {
        weatherPresenter.onReadyForSpeech(Bundle.EMPTY);
        verify(view).setVoiceString(context.getString(R.string.voice_listening));
        verify(view).setVoiceListeningAnimationEnabled(true);
    }

    @Test()
    public void checkVoiceListeningAnimationEndWhenSpeechRecognitionHasEnded() throws Exception {
        weatherPresenter.onEndOfSpeech();
        verify(view).setVoiceListeningAnimationEnabled(false);
    }

    @Test()
    public void checkSettingOfVoiceStringAndVoiceListeningAnimationStartWhenSpeechRecognitionHasEndedWithError() throws Exception {
        weatherPresenter.onError(0);
        verify(view).setVoiceString(context.getString(R.string.voice_button_promt));
        verify(view).setVoiceListeningAnimationEnabled(false);
    }

    @Test()
    public void checkSettingOfVoiceStringOnPartialResultsOfSpeechRecognition() throws Exception {
        weatherPresenter.onPartialResults(voiceResult);
        ArrayList resultList = voiceResult.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        String bestResult = (String) (resultList != null ? resultList.get(0) : null);
        verify(view).setVoiceString(String.format(bestResult + "%s", "..."));
    }

    @Test()
    public void checkSettingOfVoiceStringAndShowingLoaderAndRequestingWeatherDataOnResultsOfSpeechRecognition() throws Exception {
        weatherPresenter.onResults(voiceResult);
        ArrayList resultList = voiceResult.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        String bestResult = (String) (resultList != null ? resultList.get(0) : null);
        verify(view).setVoiceString(bestResult);
        verify(view).showLoader(true);
        verify(weatherDataProvider).requestWeatherData(bestResult);
    }

    @Test
    public void checkForHidingLoaderAndSnackbarAndDisplayWeatherDataOnWeatherDataReceived() throws Exception {
        weatherPresenter.onWeatherDataReceived(weatherData);
        verify(view).showLoader(false);
        verify(view).showNoInternetSnackbar(false);
        verify(view).setWeatherData(weatherData);
        verify(view).showWeatherDataViewContainer(true);
    }

    @Test
    public void checkForNoInternetErrorMessageOnFailure() throws Exception {
        weatherPresenter.onFailure(OnWeatherDataReceivedListener.NO_INTERNET);
        verify(view).showNoInternetSnackbar(true);
    }

    @Test
    public void checkForGpsUnavailableErrorMessageOnFailure() throws Exception {
        weatherPresenter.onFailure(OnWeatherDataReceivedListener.GPS_UNAVAILABLE);
        verify(view).showToastErrorMessage(R.string.gps_unavailable);
    }

    @Test
    public void checkForWitAiNullResponseErrorMessageOnFailure() throws Exception {
        weatherPresenter.onFailure(OnWeatherDataReceivedListener.WIT_AI_NULL_RESPONSE);
        verify(view).showToastErrorMessage(R.string.null_wit_ai_response);
    }

    @Test
    public void checkForWeatherIntentNotFoundErrorMessageOnFailure() throws Exception {
        weatherPresenter.onFailure(OnWeatherDataReceivedListener.WEATHER_INTENT_NOT_FOUND);
        verify(view).showToastErrorMessage(R.string.weather_intent_not_found);
    }

    @Test
    public void checkForPlaceUnrecognizedErrorMessageOnFailure() throws Exception {
        weatherPresenter.onFailure(OnWeatherDataReceivedListener.PLACE_UNRECOGNIZED);
        verify(view).showToastErrorMessage(R.string.place_unrecognized);
    }

    @After
    public void tearDown() throws Exception {
        weatherPresenter = null;
        voiceResult = null;
    }
}