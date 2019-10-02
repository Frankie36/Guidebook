package com.mystique.guidebook.di.component;

import android.content.Context;

import com.mystique.guidebook.di.module.AdapterModule;
import com.mystique.guidebook.di.qualifier.ActivityContext;
import com.mystique.guidebook.di.scopes.ActivityScope;
import com.mystique.guidebook.ui.activity.MainActivity;

import dagger.Component;


@ActivityScope
@Component(modules = AdapterModule.class, dependencies = ApplicationComponent.class)
public interface MainActivityComponent {

    @ActivityContext
    Context getContext();

    void injectMainActivity(MainActivity mainActivity);
}
