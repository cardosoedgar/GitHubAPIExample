package com.cardosoedgar.githubapiexample;

import android.app.Application;

/**
 * Created by edgarcardoso on 2/24/16.
 */
public class CustomApplication extends Application{

    private NetComponent netComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        String baseUrl = getString(R.string.base_url);
        String token = getString(R.string.token);

        netComponent = DaggerNetComponent.builder()
                .appModule(new AppModule(this))
                .netModule(new NetModule(baseUrl,token))
                .build();
    }

    public NetComponent getNetComponent() {
        return netComponent;
    }
}
