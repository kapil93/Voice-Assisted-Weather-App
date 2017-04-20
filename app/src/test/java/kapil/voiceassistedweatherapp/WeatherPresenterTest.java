package kapil.voiceassistedweatherapp;

import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Created by Kapil on 29/01/17.
 */
@RunWith(MockitoJUnitRunner.class)
public class WeatherPresenterTest {
    /*@Mock
    private ViewDataProvider viewDataProvider;

    @Mock
    private Context context;

    private WeatherPresenter weatherPresenter;
    private WitAiResponse witAiResponse;
    private WeatherData weatherData;

    @Before
    public void setUp() throws Exception {
        weatherPresenter = new WeatherPresenter(context, null);
        weatherPresenter.setViewDataProvider(viewDataProvider);
    }

    @Test()
    public void listeningStateShouldBeTrueWhenSpeechRecognizerIsReady() throws Exception {
        weatherPresenter.onReadyForSpeech(Bundle.EMPTY);
        verify(viewDataProvider).onListeningStateChange(true);
    }

    @Test()
    public void listeningStateShouldBeFalseWhenSpeechRecognitionHasEnded() throws Exception {
        weatherPresenter.onEndOfSpeech();
        verify(viewDataProvider).onListeningStateChange(false);
    }

    @Test()
    public void listeningStateShouldBeFalseWhenSpeechRecognitionHasEndedWithError() throws Exception {
        weatherPresenter.onError(0);
        verify(viewDataProvider).onListeningStateChange(false);
    }

    @Test()
    public void shouldGiveErrorWhenNoInternetDuringWitAiResponse() throws Exception {
        weatherPresenter.onWitAiResponse(witAiResponse, OnWitAiResponseListener.FAILURE);
        verify(viewDataProvider).onError(R.string.no_internet);
    }

    @Test
    public void shouldGiveErrorWhenNoInternetDuringWeatherApiResponse() throws Exception {
        weatherPresenter.onWeatherDataReceived(weatherData, OnWeatherDataReceivedListener.FAILURE);
        verify(viewDataProvider).onError(R.string.no_internet);
    }

    @Test
    public void shouldGiveErrorOnNullWitAiResponse() throws Exception {
        weatherPresenter.onWitAiResponse(witAiResponse, OnWitAiResponseListener.SUCCESS);
        verify(viewDataProvider).onError(R.string.null_wit_ai_response);
    }

    @Test
    public void shouldGiveErrorWhenWeatherIntentNotFound() throws Exception {
        setUpWitAiResponseForWeatherIntentNotFound();
        weatherPresenter.onWitAiResponse(witAiResponse, OnWitAiResponseListener.SUCCESS);
        verify(viewDataProvider).onError(R.string.weather_intent_not_found);
    }

    @Test
    public void shouldGiveErrorWhenLocationNotFound() throws Exception {
        setUpWitAiResponseForLocationNotFound();
        weatherPresenter.onWitAiResponse(witAiResponse, OnWitAiResponseListener.SUCCESS);
        verify(viewDataProvider).onError(R.string.location_not_found);
    }

    @Test
    public void shouldGiveErrorWhenLocationNotRecognized() throws Exception {
        weatherPresenter.onWeatherDataReceived(weatherData, OnWeatherDataReceivedListener.SUCCESS);
        verify(viewDataProvider).onError(R.string.place_unrecognized);
    }

    @Test
    public void shouldPassWeatherDataWhenPlaceIsRecognized() throws Exception {
        setUpWeatherDataForPlaceRecognized();
        weatherPresenter.onWeatherDataReceived(weatherData, OnWeatherDataReceivedListener.SUCCESS);
        verify(viewDataProvider).onWeatherDataReceived(weatherData);
    }

    @After
    public void tearDown() throws Exception {

    }

    private void setUpWitAiResponseForWeatherIntentNotFound() {
        witAiResponse = new WitAiResponse();
        Entities entities = new Entities();
        entities.setIntent(null);
        witAiResponse.setEntities(entities);
    }

    private void setUpWitAiResponseForLocationNotFound() {
        witAiResponse = new WitAiResponse();
        Entities entities = new Entities();
        List<Intent> intents = new ArrayList<>();
        Intent intent = new Intent();
        intent.setValue("weather");
        intents.add(intent);
        entities.setIntent(intents);
        entities.setLocation(null);
        witAiResponse.setEntities(entities);
    }

    private void setUpWeatherDataForPlaceRecognized() {
        weatherData = new WeatherData();
    }*/
}