package kapil.voiceassistedweatherapp.witai;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import kapil.voiceassistedweatherapp.witai.models.WitAiResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by kapil on 08-01-2017.
 */
public class WitAiService extends Service implements WitAiServiceInputProvider {
    private static final String WIT_AI_ACCESS_TOKEN = "NNXGXARQPAM2V2Q6TNK6NOA3OHPQJJ57";

    private OnWitAiResponseListener onWitAiResponseListener;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return WitAiServiceBinder.newInstance(this);
    }

    public void setOnWitAiResponseListener(OnWitAiResponseListener onWitAiResponseListener) {
        this.onWitAiResponseListener = onWitAiResponseListener;
    }

    @Override
    public void requestIntentInfo(final String string) {
        WitApiCallService witApiCallService = witCallService().create(WitApiCallService.class);
        Call<WitAiResponse> call = witApiCallService.witAiData(string, WIT_AI_ACCESS_TOKEN);
        call.enqueue(new Callback<WitAiResponse>() {
            @Override
            public void onResponse(Call<WitAiResponse> call, Response<WitAiResponse> response) {
                if (onWitAiResponseListener != null) {
                    onWitAiResponseListener.onWitAiResponse(response.body(), OnWitAiResponseListener.SUCCESS);
                }
            }

            @Override
            public void onFailure(Call<WitAiResponse> call, Throwable t) {
                if (onWitAiResponseListener != null) {
                    onWitAiResponseListener.onWitAiResponse(null, OnWitAiResponseListener.FAILURE);
                }
            }
        });
    }

    public interface WitApiCallService {
        @GET("message")
        Call<WitAiResponse> witAiData(@Query("q") String string, @Query("access_token") String access_token);
    }

    public static Retrofit witCallService() {
        return new Retrofit.Builder()
                .baseUrl("https://api.wit.ai/")
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
    }
}
