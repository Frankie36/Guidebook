package com.mystique.guidebook.di.module;

import android.content.Context;

import com.mystique.guidebook.adapter.GuideAdapter;
import com.mystique.guidebook.di.scopes.ActivityScope;

import dagger.Module;
import dagger.Provides;

@Module(includes = {MainActivityContextModule.class})
public class AdapterModule {

    @Provides
    @ActivityScope
    public GuideAdapter getData() {
        return new GuideAdapter();
    }
}
