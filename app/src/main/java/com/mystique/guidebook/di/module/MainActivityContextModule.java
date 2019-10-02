package com.mystique.guidebook.di.module;

import android.content.Context;


import com.mystique.guidebook.di.qualifier.ActivityContext;
import com.mystique.guidebook.di.scopes.ActivityScope;
import com.mystique.guidebook.ui.activity.MainActivity;

import dagger.Module;
import dagger.Provides;

@Module
public class MainActivityContextModule {
    private MainActivity mainActivity;

    public Context context;

    public MainActivityContextModule(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        context = mainActivity;
    }

    @Provides
    @ActivityScope
    public MainActivity providesMainActivity() {
        return mainActivity;
    }

    @Provides
    @ActivityScope
    @ActivityContext
    public Context provideContext() {
        return context;
    }

}
