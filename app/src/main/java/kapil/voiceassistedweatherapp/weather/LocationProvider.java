package kapil.voiceassistedweatherapp.weather;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import kapil.voiceassistedweatherapp.R;

/**
 * This class provides device location to {@link kapil.voiceassistedweatherapp.weather.WeatherDataProvider}
 * in case there is no location data in wit.ai response.
 */

public class LocationProvider implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private static final String TAG = LocationProvider.class.getSimpleName();

    private final Context context;

    private ObservableEmitter<Location> locationEmitter;

    @Inject
    GoogleApiClient googleApiClient;

    @Inject
    LocationProvider(Context context) {
        this.context = context;

        registerLocationCallbacks();
    }

    @Inject
    void registerLocationCallbacks() {
        if (googleApiClient != null) {
            googleApiClient.registerConnectionCallbacks(this);
            googleApiClient.registerConnectionFailedListener(this);
        }
    }

    /**
     * Connects google api client to retrieve device location.
     *
     * @return An observable for device location.
     */

    Observable<Location> getLocationObservable() {
        googleApiClient.connect();
        return Observable.create(emitter -> locationEmitter = emitter);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.i(TAG, "onConnected");

        if (ActivityCompat.checkSelfPermission(googleApiClient.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.e(TAG, "Location Permission Denied");
            locationEmitter.onError(new Exception(context.getString(R.string.location_permission_denied)));
        } else {
            Location location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);

            if (location != null) {
                locationEmitter.onNext(location);
                locationEmitter.onComplete();
            } else {
                Log.e(TAG, "Null Location");
                locationEmitter.onError(new Exception(context.getString(R.string.gps_unavailable)));
            }
        }

        googleApiClient.disconnect();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "onConnectionSuspended");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i(TAG, "onConnectionFailed");
    }
}
