package com.mystique.guidebook;

import android.app.Activity;
import android.app.Application;
import com.mystique.guidebook.di.component.ApplicationComponent;
import com.mystique.guidebook.di.component.DaggerApplicationComponent;
import com.mystique.guidebook.di.module.ContextModule;


public class App extends Application {

    ApplicationComponent applicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        applicationComponent = DaggerApplicationComponent.builder().contextModule(new ContextModule(this)).build();
        applicationComponent.injectApplication(this);
    }

    public static App get(Activity activity) {
        return (App) activity.getApplication();
    }

    public ApplicationComponent getApplicationComponent() {
        return applicationComponent;
    }
}

