package com.mystique.guidebook.ui.activity;

import android.content.Context;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mystique.guidebook.App;
import com.mystique.guidebook.R;
import com.mystique.guidebook.adapter.GuideAdapter;
import com.mystique.guidebook.di.component.ApplicationComponent;
import com.mystique.guidebook.di.component.DaggerMainActivityComponent;
import com.mystique.guidebook.di.component.MainActivityComponent;
import com.mystique.guidebook.di.module.MainActivityContextModule;
import com.mystique.guidebook.di.qualifier.ActivityContext;
import com.mystique.guidebook.di.qualifier.ApplicationContext;
import com.mystique.guidebook.io.ApiInterface;
import com.mystique.guidebook.model.Guide;
import com.mystique.guidebook.model.GuideResponse;
import com.santalu.emptyview.EmptyView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;

import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    MainActivityComponent mainActivityComponent;

    @Inject
    public ApiInterface apiInterface;

    @Inject
    @ApplicationContext
    public Context mContext;

    @Inject
    @ActivityContext
    public Context activityContext;

    @Inject
    public GuideAdapter recyclerViewAdapter;

    private EmptyView emptMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Initialize our views
        emptMain = findViewById(R.id.emptMain);
        RecyclerView recyclerView = findViewById(R.id.rvGuide);
        FloatingActionButton fab = findViewById(R.id.fab);

        //Refresh data on Floating Action Button clicked
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fetchData();
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        ApplicationComponent applicationComponent = App.get(this).getApplicationComponent();
        mainActivityComponent = DaggerMainActivityComponent.builder()
                .mainActivityContextModule(new MainActivityContextModule(this))
                .applicationComponent(applicationComponent)
                .build();
        mainActivityComponent.injectMainActivity(MainActivity.this);
        recyclerView.setAdapter(recyclerViewAdapter);

        fetchData();
    }

    private void fetchData() {
        //Show loading state when fetching data
        emptMain.showLoading();

        apiInterface.getData().enqueue(new Callback<GuideResponse>() {
            @Override
            public void onResponse(Call<GuideResponse> call, Response<GuideResponse> response) {
                if (response.body() != null) {
                    if (response.body().guideList.size() > 0) {
                        populateRecyclerView(response.body().guideList);
                        //show content if data isn't empty
                        emptMain.showContent();
                    } else {
                        //show empty state of there's nothing to show
                        emptMain.showEmpty();
                    }
                } else {
                    showErrorView();
                }
            }

            @Override
            public void onFailure(Call<GuideResponse> call, Throwable t) {
                //show error view if error occurs when calling endpoint
                showErrorView();
            }
        });
    }


    private void showErrorView() {
        //show error view if error occurs when calling endpoint
        //Also try and fetch data when 'Try again' button is clicked
        emptMain.error().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fetchData();
            }
        }).show();
    }

    private void populateRecyclerView(List<Guide> response) {
        //add data to the recycler adapter
        recyclerViewAdapter.setData(response);
    }

}
