package kapil.voiceassistedweatherapp.witai;

import android.support.annotation.IntDef;

import kapil.voiceassistedweatherapp.witai.models.WitAiResponse;

/**
 * Created by kapil on 08-01-2017.
 */
public interface OnWitAiResponseListener {
    @IntDef({SUCCESS, FAILURE})
    @interface WitAiResponseType {

    }

    int SUCCESS = 0;
    int FAILURE = 1;

    void onWitAiResponse(WitAiResponse witAiResponse, @WitAiResponseType int responseType);
}
