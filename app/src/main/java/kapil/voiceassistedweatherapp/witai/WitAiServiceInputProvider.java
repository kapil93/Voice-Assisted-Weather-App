package kapil.voiceassistedweatherapp.witai;

/**
 * Created by kapil on 08-01-2017.
 */
public interface WitAiServiceInputProvider {
    void setOnWitAiResponseListener(OnWitAiResponseListener onWitAiResponseListener);
    void requestIntentInfo(String string);
}
