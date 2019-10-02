package com.mystique.guidebook.di.component;

import android.content.Context;

import com.mystique.guidebook.App;
import com.mystique.guidebook.di.module.ContextModule;
import com.mystique.guidebook.di.module.RetrofitModule;
import com.mystique.guidebook.di.qualifier.ApplicationContext;
import com.mystique.guidebook.di.scopes.ApplicationScope;
import com.mystique.guidebook.io.ApiInterface;

import dagger.Component;

@ApplicationScope
@Component(modules = {ContextModule.class, RetrofitModule.class})
public interface ApplicationComponent {

    public ApiInterface getApiInterface();

    @ApplicationContext
    public Context getContext();

    public void injectApplication(App app);
}
