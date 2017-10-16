package com.tvs.comet.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tvs.comet.R;
import com.tvs.comet.models.DashboardModel;

import java.util.ArrayList;

/**
 * Created by UITOUX10 on 10/12/17.
 */

public class DashboardAdapter extends RecyclerView.Adapter<DashboardAdapter.ViewHolder> {

    Context context;
    ArrayList<DashboardModel> arraylist;
    public static DashboardIntentInterface listener;


    public DashboardAdapter(Context contex, ArrayList<DashboardModel> arrayList) {
        context = contex;
        arraylist = arrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_dashboard, null);
        return new ItemHolder(itemLayoutView);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        ItemHolder itemHolder = (ItemHolder) holder;

        DashboardModel dashboardModel = arraylist.get(position);

        itemHolder.tv.setText(dashboardModel.getCategoryName());
        itemHolder.iv.setImageResource(dashboardModel.getCategoryIcon());

    }

    @Override
    public int getItemCount() {
        return arraylist.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ViewHolder(View v) {
            super(v);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int pos = getAdapterPosition();
            DashboardModel dashboardModel = arraylist.get(pos);

            listener.getIntentString(dashboardModel.getCategoryName());

        }
    }

    public class ItemHolder extends ViewHolder implements View.OnClickListener {
        TextView tv;
        ImageView iv;

        public ItemHolder(View v) {
            super(v);
            tv = (TextView) v.findViewById(R.id.tv);
            iv = (ImageView) v.findViewById(R.id.iv);

        }
    }

    public static void onBindListener(DashboardIntentInterface dashboardIntentInterface){
        listener = dashboardIntentInterface;
    }

    public interface DashboardIntentInterface{
        void getIntentString(String s);
    }


}
