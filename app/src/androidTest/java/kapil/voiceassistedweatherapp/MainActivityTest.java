package kapil.voiceassistedweatherapp;

import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

/**
 * Created by Kapil on 29/01/17.
 */
public class MainActivityTest {
    @Rule
    public ActivityTestRule<MainActivity> mainActivityTestRule = new ActivityTestRule<>(
            MainActivity.class);

    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void voiceListeningAnimationShouldBeTriggeredOnVoiceButtonPress() throws Exception {
        onView(withId(R.id.voice_button)).perform(click());
        onView(withId(R.id.voice_listening_view)).check(matches(isDisplayed()));
    }

    @Test
    public void voiceTextShouldChangeOnVoiceButtonPress() throws Exception {
        onView(withId(R.id.voice_button)).perform(click());
        Thread.sleep(500);
        onView(withId(R.id.voice_output)).check(matches(withText(R.string.voice_listening)));
    }

    @Test
    public void snackbarShouldBeDisplayedWhenNoInternetWhileHittingApi() throws Exception {
        mainActivityTestRule.getActivity().onError(R.string.no_internet);
        Thread.sleep(250);
        onView(withText(R.string.no_internet)).check(matches(isDisplayed()));
    }

    @Test
    @Ignore     // This test is passing when run alone, but failing when all tests are run together
    public void toastShouldBeDisplayedWhenPlaceUnrecognized() throws Exception {
        mainActivityTestRule.getActivity().onError(R.string.place_unrecognized);
        Thread.sleep(250);
        onView(withText(R.string.place_unrecognized)).inRoot(withDecorView(not(is(mainActivityTestRule.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()));
    }

    @Test
    public void toastShouldBeDisplayedWhenLocationNotFound() throws Exception {
        mainActivityTestRule.getActivity().onError(R.string.location_not_found);
        Thread.sleep(250);
        onView(withText(R.string.location_not_found)).inRoot(withDecorView(not(is(mainActivityTestRule.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()));
    }

    @Test
    public void toastShouldBeDisplayedOnNullWitAiResponse() throws Exception {
        mainActivityTestRule.getActivity().onError(R.string.null_wit_ai_response);
        Thread.sleep(250);
        onView(withText(R.string.null_wit_ai_response)).inRoot(withDecorView(not(is(mainActivityTestRule.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()));
    }

    @Test
    public void toastShouldBeDisplayedWhenWeatherIntentNotFound() throws Exception {
        mainActivityTestRule.getActivity().onError(R.string.weather_intent_not_found);
        Thread.sleep(250);
        onView(withText(R.string.weather_intent_not_found)).inRoot(withDecorView(not(is(mainActivityTestRule.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()));
    }

    @Test (expected = NoMatchingViewException.class)
    public void snackbarShouldHideOnResponse() throws Exception {
        mainActivityTestRule.getActivity().onWeatherDataReceived(null);
        Thread.sleep(250);
        onView(withText(R.string.no_internet)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
    }

    @Test (expected = NoMatchingViewException.class)
    public void progressDialogShouldHideOnResponse() throws Exception {
        mainActivityTestRule.getActivity().onWeatherDataReceived(null);
        Thread.sleep(250);
        onView(withText(R.string.weather_progress_message)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
    }

    @Test (expected = NoMatchingViewException.class)
    public void progressDialogShouldHideOnError() throws Exception {
        mainActivityTestRule.getActivity().onError(R.string.weather_intent_not_found);
        Thread.sleep(250);
        onView(withText(R.string.weather_progress_message)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
    }

    @Test
    public void ProgressDialogShouldBeDisplayedOnRequest() throws Exception {
        mainActivityTestRule.getActivity().onRequest();
        Thread.sleep(250);
        onView(withText(R.string.weather_progress_message)).check(matches(isDisplayed()));
    }

    @Test
    public void ProgressDialogShouldBeDisplayedOnRetryButtonPress() throws Exception {
        mainActivityTestRule.getActivity().onError(R.string.no_internet);
        Thread.sleep(250);
        onView(withText("RETRY")).perform(click());
        Thread.sleep(250);
        onView(withText(R.string.weather_progress_message)).check(matches(isDisplayed()));
    }

    @After
    public void tearDown() throws Exception {

    }
}