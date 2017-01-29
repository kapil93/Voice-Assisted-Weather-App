package kapil.voiceassistedweatherapp.witai;

import android.os.Binder;

/**
 * Created by kapil on 08-01-2017.
 */
public class WitAiServiceBinder extends Binder {
    private WitAiServiceInputProvider witAiServiceInputProvider;

    public WitAiServiceBinder(WitAiServiceInputProvider witAiServiceInputProvider) {
        this.witAiServiceInputProvider = witAiServiceInputProvider;
    }

    public static WitAiServiceBinder newInstance(WitAiServiceInputProvider witAiServiceInputProvider) {
        return new WitAiServiceBinder(witAiServiceInputProvider);
    }

    public WitAiServiceInputProvider getWitAiServiceInputProvider() {
        return witAiServiceInputProvider;
    }
}
