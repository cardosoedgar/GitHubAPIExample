package com.cardosoedgar.githubapiexample;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by edgarcardoso on 2/24/16.
 */
@Singleton
@Component(modules={AppModule.class, NetModule.class})
public interface NetComponent {
    void inject(MainActivity activity);
}
