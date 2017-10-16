package com.tvs.comet.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tvs.comet.R;
import com.tvs.comet.models.StateWiseRankingModel;

import java.util.ArrayList;

/**
 * Created by UITOUX5 on 12/10/17.
 */

public class StateWiseRankingAdapter extends RecyclerView.Adapter<StateWiseRankingAdapter.MyViewHolder> {
    Context context;
    ArrayList<StateWiseRankingModel> arrayList;
    Typeface font_raleway_semi_bold;

    public StateWiseRankingAdapter(Context context, ArrayList<StateWiseRankingModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_state_wise_ranking, null);

        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;


    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        final StateWiseRankingModel model = arrayList.get(position);

        font_raleway_semi_bold = Typeface.createFromAsset(context.getAssets(), "fonts/Raleway-SemiBold.ttf");

        if(position == 0){
            holder.row_tv1.setTypeface(font_raleway_semi_bold);
            holder.row_tv2.setTypeface(font_raleway_semi_bold);
        }

        holder.row_tv1.setText(model.getOutlets());
        holder.row_tv2.setText(model.getTasks());

    }


    @Override
    public int getItemCount() {
        return arrayList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
       TextView row_tv1,row_tv2;


        private MyViewHolder(final View view) {
            super(view);

            row_tv1=(TextView) view.findViewById(R.id.row_tv1);
            row_tv2=(TextView) view.findViewById(R.id.row_tv2);

//            Typeface tf_regular = Typeface.createFromAsset(itemView.getContext().getAssets(), "font/TitilliumWeb-Regular.ttf");
//            this.row_asp_service_form_list_chechBox.setTypeface(tf_regular);


        }


    }
}
