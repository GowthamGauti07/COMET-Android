package com.tvs.comet.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.tvs.comet.R;
import com.tvs.comet.adapters.DashboardAdapter;
import com.tvs.comet.models.DashboardModel;

import java.util.ArrayList;

public class DashboardActivity extends AppCompatActivity implements DashboardAdapter.DashboardIntentInterface{

    ImageView iv_profile, iv_notification;
    RecyclerView rv;

    ArrayList<DashboardModel> arrayListDashboard;
    DashboardModel dashboardModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        init();

    }

    @Override
    protected void onResume() {
        super.onResume();
        DashboardAdapter.onBindListener(this);
    }

    private void init() {

        arrayListDashboard = new ArrayList<>();

        dashboardModel = new DashboardModel();
        dashboardModel.setCategoryName("Act-wise Coverage");
        dashboardModel.setCategoryIcon(R.drawable.notepad);
        arrayListDashboard.add(dashboardModel);
        dashboardModel = new DashboardModel();
        dashboardModel.setCategoryName("State-wise Outlet Ranking");
        dashboardModel.setCategoryIcon(R.drawable.worldwide);
        arrayListDashboard.add(dashboardModel);
        dashboardModel = new DashboardModel();
        dashboardModel.setCategoryName("State-wise Ranking");
        dashboardModel.setCategoryIcon(R.drawable.rank);
        arrayListDashboard.add(dashboardModel);
        dashboardModel = new DashboardModel();
        dashboardModel.setCategoryName("Task-wise State Ranking");
        dashboardModel.setCategoryIcon(R.drawable.calendar_1);
        arrayListDashboard.add(dashboardModel);
        dashboardModel = new DashboardModel();
        dashboardModel.setCategoryName("Single Outlet Ranking");
        dashboardModel.setCategoryIcon(R.drawable.price_tag);
        arrayListDashboard.add(dashboardModel);
        dashboardModel = new DashboardModel();
        dashboardModel.setCategoryName("LOB-wise overall Ranking");
        dashboardModel.setCategoryIcon(R.drawable.rank_1);
        arrayListDashboard.add(dashboardModel);
        dashboardModel = new DashboardModel();
        dashboardModel.setCategoryName("LOB-wise Ranking");
        dashboardModel.setCategoryIcon(R.drawable.lob_rank);
        arrayListDashboard.add(dashboardModel);

        iv_profile = (ImageView) findViewById(R.id.iv_profile);
        iv_notification = (ImageView) findViewById(R.id.iv_notification);
        rv = (RecyclerView) findViewById(R.id.rv);
        rv.setLayoutManager(new GridLayoutManager(this, 2));
        rv.setItemAnimator(new DefaultItemAnimator());
        DashboardAdapter dashboardAdapter = new DashboardAdapter(DashboardActivity.this, arrayListDashboard);
        rv.setAdapter(dashboardAdapter);

    }

    @Override
    public void getIntentString(String s) {

        Intent intent;
        switch (s){
            case "Act-wise Coverage":
                intent = new Intent(DashboardActivity.this, StateWiseRankingActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.push_left_enter, R.anim.push_left_exit);
                break;

            case  "State-wise Outlet Ranking":
                intent = new Intent(DashboardActivity.this, StateWiseRankingActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.push_left_enter, R.anim.push_left_exit);
                break;

            case "State-wise Ranking":
                intent = new Intent(DashboardActivity.this, StateWiseRankingActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.push_left_enter, R.anim.push_left_exit);
                break;

            case "Task-wise State Ranking":
                intent = new Intent(DashboardActivity.this, StateWiseRankingActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.push_left_enter, R.anim.push_left_exit);
                break;

            case "Single Outlet Ranking":
                intent = new Intent(DashboardActivity.this, StateWiseRankingActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.push_left_enter, R.anim.push_left_exit);
                break;

            case "LOB-wise overall Ranking":
                intent = new Intent(DashboardActivity.this, StateWiseRankingActivity.class);
                startActivity(intent);
                break;

            case "LOB-wise Ranking":
                intent = new Intent(DashboardActivity.this, StateWiseRankingActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.push_left_enter, R.anim.push_left_exit);
                break;
        }

    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.push_right_enter, R.anim.push_right_exit);
    }
}
