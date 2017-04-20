package kapil.voiceassistedweatherapp;

import android.app.ProgressDialog;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import javax.inject.Inject;

import kapil.voiceassistedweatherapp.customviews.VoiceListeningView;
import kapil.voiceassistedweatherapp.weather.models.weather.WeatherData;

/**
 * MainActivity (View) sends appropriate callbacks regarding user interaction with the screen to
 * {@link WeatherPresenter} (Presenter) and displays information sent by it.
 */

public class MainActivity extends AppCompatActivity implements WeatherContract.View, View.OnClickListener {
    private static final String TAG = MainActivity.class.getSimpleName();

    @Inject WeatherPresenter weatherPresenter;

    private FloatingActionButton voiceButton;
    private TextView voiceOutput;
    private TextView suggestionText;
    private VoiceListeningView voiceListeningView;

    private TextView place;
    private ImageView weatherIcon;
    private TextView celsiusSymbol;
    private TextView temperature;
    private TextView minTemp;
    private TextView maxTemp;
    private TextView humidity;
    private ImageView humidityIcon;
    private TextView pressure;
    private ImageView pressureIcon;
    private TextView windSpeed;
    private ImageView windSpeedIcon;

    private Snackbar noInternetSnackbar;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ((VoiceAssistedWeatherApp) getApplication()).getAppComponent().inject(this);

        weatherPresenter.setView(this);

        initializeViews();
        showViews(false);
        setClickListeners();

        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage(getString(R.string.weather_progress_message));
        progressDialog.setCancelable(false);
    }

    private void initializeViews() {
        voiceButton = (FloatingActionButton) findViewById(R.id.voice_button);
        voiceOutput = (TextView) findViewById(R.id.voice_output);
        suggestionText = (TextView) findViewById(R.id.suggestion_text);
        voiceListeningView = (VoiceListeningView) findViewById(R.id.voice_listening_view);

        place = (TextView) findViewById(R.id.place);
        weatherIcon = (ImageView) findViewById(R.id.weather_icon);
        celsiusSymbol = (TextView) findViewById(R.id.celsius_symbol);
        temperature = (TextView) findViewById(R.id.temp);
        minTemp = (TextView) findViewById(R.id.temp_min);
        maxTemp = (TextView) findViewById(R.id.temp_max);
        humidity = (TextView) findViewById(R.id.humidity);
        humidityIcon = (ImageView) findViewById(R.id.humidity_icon);
        pressure = (TextView) findViewById(R.id.pressure);
        pressureIcon = (ImageView) findViewById(R.id.pressure_icon);
        windSpeed = (TextView) findViewById(R.id.wind_speed);
        windSpeedIcon = (ImageView) findViewById(R.id.wind_speed_icon);
    }

    private void showViews(boolean show) {
        int visibility = show ? View.VISIBLE : View.GONE;
        place.setVisibility(visibility);
        weatherIcon.setVisibility(visibility);
        celsiusSymbol.setVisibility(visibility);
        temperature.setVisibility(visibility);
        minTemp.setVisibility(visibility);
        maxTemp.setVisibility(visibility);
        humidity.setVisibility(visibility);
        humidityIcon.setVisibility(visibility);
        pressure.setVisibility(visibility);
        pressureIcon.setVisibility(visibility);
        windSpeed.setVisibility(visibility);
        windSpeedIcon.setVisibility(visibility);

        suggestionText.setVisibility(show ? View.GONE : View.VISIBLE);
    }

    private void setClickListeners() {
        voiceButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.voice_button:
                weatherPresenter.triggerSpeechRecognizer();
                break;
        }
    }

    @Override
    public void setVoiceString(String string) {
        voiceOutput.setText(string);
    }

    @Override
    public void setVoiceListeningAnimationEnabled(boolean enabled) {
        if (enabled) {
            voiceListeningView.setVisibility(View.VISIBLE);
            voiceListeningView.startAnim();
        } else {
            voiceListeningView.setVisibility(View.GONE);
            voiceListeningView.endAnim();
        }
    }

    @Override
    public void showWeatherDataViewContainer(boolean show) {
        showViews(show);
    }

    @Override
    public void showLoader(boolean show) {
        if (show) {
            if (!progressDialog.isShowing()) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.show();
                    }
                });
            }
        } else {
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        }
    }

    @Override
    public void showToastErrorMessage(int errorResId) {
        final String errorMsg = getApplicationContext().getString(errorResId);
        Log.i(TAG, errorMsg);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void showWeatherData(WeatherData weatherData) {
        if (weatherData != null) {
            displayWeatherData(weatherData);
        }
    }

    @Override
    public void showNoInternetSnackbar(boolean show) {
        if (show) {
            noInternetSnackbar = Snackbar.make(voiceButton, R.string.no_internet, Snackbar.LENGTH_INDEFINITE);
            noInternetSnackbar.setAction("RETRY", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    weatherPresenter.retryDataFetch();
                }
            });
            noInternetSnackbar.show();
        } else {
            if (noInternetSnackbar != null) {
                if (noInternetSnackbar.isShown()) {
                    noInternetSnackbar.dismiss();
                }
            }
        }
    }

    private void displayWeatherData(WeatherData weatherData) {
        place.setText(weatherData.getName() + ", " + weatherData.getSys().getCountry());
        setWeatherIcon(weatherData.getWeather().get(0).getIcon());
        temperature.setText(String.format(String.valueOf(Math.round(weatherData.getMain().getTemp())) + "%c", '°'));
        minTemp.setText(String.format("%s" + String.valueOf(Math.round(weatherData.getMain().getTempMin())) + "%c", "min: ", '°'));
        maxTemp.setText(String.format("%s" + String.valueOf(Math.round(weatherData.getMain().getTempMax())) + "%c", "max: ", '°'));
        humidity.setText(String.format(weatherData.getMain().getHumidity() + "%s", " %"));
        pressure.setText(String.format(weatherData.getMain().getPressure() + "%s", " hPa"));
        windSpeed.setText(String.format(weatherData.getWind().getSpeed() + "%s", " mps"));
    }

    private void setWeatherIcon(String icon) {
        int iconResourceId;
        switch (icon) {
            case "01d":
                iconResourceId = R.drawable._01d;
                break;
            case "01n":
                iconResourceId = R.drawable._01n;
                break;
            case "02d":
                iconResourceId = R.drawable._02d;
                break;
            case "02n":
                iconResourceId = R.drawable._02n;
                break;
            case "03d":
            case "03n":
                iconResourceId = R.drawable._03dn;
                break;
            case "04d":
            case "04n":
                iconResourceId = R.drawable._04dn;
                break;
            case "09d":
            case "09n":
                iconResourceId = R.drawable._09dn;
                break;
            case "10d":
                iconResourceId = R.drawable._10d;
                break;
            case "10n":
                iconResourceId = R.drawable._10n;
                break;
            case "11d":
            case "11n":
                iconResourceId = R.drawable._11dn;
                break;
            case "13d":
            case "13n":
                iconResourceId = R.drawable._13dn;
                break;
            case "50d":
            case "50n":
                iconResourceId = R.drawable._50dn;
                break;
            default:
                iconResourceId = 0;
                break;
        }
        weatherIcon.setImageResource(iconResourceId);
    }

    @Override
    protected void onDestroy() {
        weatherPresenter.destroy();
        super.onDestroy();
    }
}
