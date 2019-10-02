package com.mystique.guidebook.di.module;

import android.content.Context;


import com.mystique.guidebook.di.qualifier.ApplicationContext;
import com.mystique.guidebook.di.scopes.ApplicationScope;

import dagger.Module;
import dagger.Provides;

@Module
public class ContextModule {
    private Context context;

    public ContextModule(Context context) {
        this.context = context;
    }

    @Provides
    @ApplicationScope
    @ApplicationContext
    public Context provideContext() {
        return context;
    }
}
