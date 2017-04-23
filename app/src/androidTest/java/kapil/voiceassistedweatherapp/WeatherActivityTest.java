package kapil.voiceassistedweatherapp;

import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
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
 * This class contains instrumentation tests for {@link WeatherActivity}.
 */
public class WeatherActivityTest {
    @Rule
    public ActivityTestRule<WeatherActivity> mainActivityTestRule = new ActivityTestRule<>(
            WeatherActivity.class);

    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void checkVoiceListeningAnimationAndSettingOfVoiceTextOnVoiceButtonPress() throws Exception {
        onView(withId(R.id.voice_button)).perform(click());
        onView(withId(R.id.voice_listening_view)).check(matches(isDisplayed()));
        onView(withId(R.id.voice_output)).check(matches(withText(R.string.voice_listening)));
    }

    @Test
    public void checkSnackbarDisplayedOnNoInternet() throws Exception {
        mainActivityTestRule.getActivity().showNoInternetSnackbar(true);
        Thread.sleep(50);
        onView(withText(R.string.no_internet)).check(matches(isDisplayed()));
    }

    @Test
    public void checkToastDisplayedWhenOnError() throws Exception {
        mainActivityTestRule.getActivity().showToastErrorMessage(R.string.gps_unavailable);
        onView(withText(R.string.gps_unavailable)).inRoot(withDecorView(not(is(mainActivityTestRule.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()));
    }

    @Test (expected = NoMatchingViewException.class)
    public void checkSnackbarHideOnResponse() throws Exception {
        mainActivityTestRule.getActivity().setWeatherData(null);
        onView(withText(R.string.weather_progress_message)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
    }

    @Test (expected = NoMatchingViewException.class)
    public void checkProgressDialogHideOnResponse() throws Exception {
        mainActivityTestRule.getActivity().setWeatherData(null);
        onView(withText(R.string.weather_progress_message)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
    }

    @Test (expected = NoMatchingViewException.class)
    public void checkProgressDialogHideOnError() throws Exception {
        mainActivityTestRule.getActivity().showToastErrorMessage(R.string.weather_intent_not_found);
        onView(withText(R.string.weather_progress_message)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
    }

    @Test (expected = NoMatchingViewException.class)
    public void checkProgressDialogHideOnNoInternet() throws Exception {
        mainActivityTestRule.getActivity().showNoInternetSnackbar(true);
        onView(withText(R.string.weather_progress_message)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
    }

    @After
    public void tearDown() throws Exception {

    }
}