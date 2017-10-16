package com.tvs.comet.activities;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineRadarDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;
import com.tvs.comet.R;
import com.tvs.comet.adapters.StateWiseRankingAdapter;
import com.tvs.comet.models.GetStatewiseRankingGraph;
import com.tvs.comet.models.StateWiseRankingModel;
import com.tvs.comet.utils.JsonServiceHandlerPost;
import com.tvs.comet.utils.Util;

import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by UITOUX10 on 10/12/17.
 */

public class StateWiseRankingActivity extends AppCompatActivity /*implements OnChartValueSelectedListener*/ {
    StateWiseRankingModel model;
    RecyclerView rv;
    PieChart pieChart;
    ImageView activity_asp_finance_form_iv;

    Typeface font_raleway_regular;
    ArrayList<StateWiseRankingModel> modelArrayList;
    ArrayList<String> xVals;
    ArrayList<Entry> yvalues;

    ProgressDialog progressDialog;
    ArrayList<GetStatewiseRankingGraph> arrayListGetStatewiseRankingGraph;
    float green=0, yellow=0, red=0;
    String stateName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_state_wise_ranking);

        init();

        activity_asp_finance_form_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.push_right_enter, R.anim.push_right_exit);
            }
        });

        new GetCustomerDetails().execute();


    }

    private void init() {

        modelArrayList = new ArrayList<>();

        model = new StateWiseRankingModel();
        model.setOutlets("Outlets");
        model.setTasks("Tasks to be done");
        modelArrayList.add(model);

        model = new StateWiseRankingModel();
        model.setOutlets("Madurai");
        model.setTasks("FA License");
        modelArrayList.add(model);

        model = new StateWiseRankingModel();
        model.setOutlets("Salem");
        model.setTasks("CLRA RC");
        modelArrayList.add(model);

        model = new StateWiseRankingModel();
        model.setOutlets("Neelambur");
        model.setTasks("Timecard Exemption");
        modelArrayList.add(model);

        model = new StateWiseRankingModel();
        model.setOutlets("Trichy");
        model.setTasks("Fire License");
        modelArrayList.add(model);

        model = new StateWiseRankingModel();
        model.setOutlets("Padappai");
        model.setTasks("Trade License");
        modelArrayList.add(model);



        font_raleway_regular = Typeface.createFromAsset(getAssets(), "fonts/Raleway-Regular.ttf");

        activity_asp_finance_form_iv = (ImageView) findViewById(R.id.activity_asp_finance_form_iv);
        rv = (RecyclerView) findViewById(R.id.rv);
        rv.setNestedScrollingEnabled(false);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(StateWiseRankingActivity.this);
        rv.setLayoutManager(mLayoutManager);
        StateWiseRankingAdapter recyclerViewAdapter = new StateWiseRankingAdapter(StateWiseRankingActivity.this, modelArrayList);
        rv.setAdapter(recyclerViewAdapter);
        pieChart = (PieChart) findViewById(R.id.piechart);
        pieChart.setUsePercentValues(true);
        pieChart.setCenterTextSize(12);
        pieChart.setCenterTextTypeface(font_raleway_regular);
        pieChart.invalidate();
        pieChart.setDrawHoleEnabled(true);
        pieChart.setTransparentCircleRadius(70f);
        pieChart.setHoleRadius(70f);
        pieChart.setRotationAngle(0);
        pieChart.getDescription().setEnabled(false);
        // enable rotation of the chart by touch
        pieChart.setRotationEnabled(false);
        pieChart.setHighlightPerTapEnabled(false);
        pieChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);

        Legend l = pieChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.CENTER);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setTextSize(11f);
        l.setDrawInside(false);
        l.setXEntrySpace(0f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);

//        Legend l = pieChart.getLegend();
//        l.setPosition(Legend.LegendPosition.RIGHT_OF_CHART_CENTER);
//        l.setXEntrySpace(17f);
//        l.setYEntrySpace(15f);
//        l.setYOffset(0f);
    }

//    @Override
//    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
//
//        if (e == null)
//            return;
//        Log.i("VAL SELECTED",
//                "Value: " + e.getVal() + ", xIndex: " + e.getXIndex()
//                        + ", DataSet index: " + dataSetIndex);
//    }



//    @Override
//    public void onNothingSelected() {
//        Log.i("PieChart", "nothing selected");
//    }


    private class GetCustomerDetails extends AsyncTask<Void, Void, SoapObject> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(StateWiseRankingActivity.this);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("loading");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }


        @Override
        protected SoapObject doInBackground(Void... params) {

            HashMap<String, String> stringStringHashMap = new HashMap<>();
            stringStringHashMap.put("State", "TML0002334");

            JsonServiceHandlerPost jsonServiceHandlerObject = new JsonServiceHandlerPost(Util.URL_TVS, stringStringHashMap,
                    StateWiseRankingActivity.this, Util.NAMESPACE, Util.METHOD_NAME_GET_STATEWISE_RANKING,
                    Util.SOAP_ACTION_GET_STATEWISE_RANKING);
            SoapObject soapObjectResult = jsonServiceHandlerObject.ServiceSoap();

            if (soapObjectResult != null) {
                try {
                    SoapObject soapObject = (SoapObject) soapObjectResult.getProperty(0);
                    int customerCount = soapObject.getPropertyCount();

                    arrayListGetStatewiseRankingGraph = new ArrayList<>();
                    green = 0;
                    yellow = 0;
                    red = 0;
                    for (int i = 0; i < customerCount; i++) {
                        GetStatewiseRankingGraph getStatewiseRankingGraph = new GetStatewiseRankingGraph();
                        SoapObject soapObject1 = (SoapObject) soapObject.getProperty(i);

                        if (!(soapObject1.getProperty("ActID").toString().equals("anyType{}"))) {
                            getStatewiseRankingGraph.setActID(soapObject1.getProperty("ActID").toString());
                        }
                        if (!(soapObject1.getProperty("StateID").toString().equals("anyType{}"))) {
                            getStatewiseRankingGraph.setStateID(soapObject1.getProperty("StateID").toString());
                        }
                        if (!(soapObject1.getProperty("StateDescr").toString().equals("anyType{}"))) {
                            getStatewiseRankingGraph.setStateDescr(soapObject1.getProperty("StateDescr").toString());
                            stateName = soapObject1.getProperty("StateDescr").toString();
                        }
                        if (!(soapObject1.getProperty("ActDescr").toString().equals("anyType{}"))) {
                            getStatewiseRankingGraph.setActDescr(soapObject1.getProperty("ActDescr").toString());
                        }
                        if (!(soapObject1.getProperty("Green").toString().equals("anyType{}"))) {
                            try {
                                green = green + Integer.parseInt(soapObject1.getProperty("Green").toString());
                                getStatewiseRankingGraph.setGreen(Integer.parseInt(soapObject1.getProperty("Green").toString()));
                            } catch (NumberFormatException e) {
                                e.printStackTrace();
                            }
                        }
                        if (!(soapObject1.getProperty("Yellow").toString().equals("anyType{}"))) {
                            try {
                                yellow = yellow + Integer.parseInt(soapObject1.getProperty("Yellow").toString());
                                getStatewiseRankingGraph.setYellow(Integer.parseInt(soapObject1.getProperty("Yellow").toString()));
                            } catch (NumberFormatException e) {
                                e.printStackTrace();
                            }
                        }
                        if (!(soapObject1.getProperty("Red").toString().equals("anyType{}"))) {
                            try {
                                red = red + Integer.parseInt(soapObject1.getProperty("Red").toString());
                                getStatewiseRankingGraph.setRed(Integer.parseInt(soapObject1.getProperty("Red").toString()));
                            } catch (NumberFormatException e) {
                                e.printStackTrace();
                            }
                        }

                        arrayListGetStatewiseRankingGraph.add(getStatewiseRankingGraph);

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


            return soapObjectResult;
        }

        @Override
        protected void onPostExecute(SoapObject soapObject) {
            super.onPostExecute(soapObject);
            progressDialog.dismiss();

            // IMPORTANT: In a PieChart, no values (Entry) should have the same
            // xIndex (even if from different DataSets), since no values can be
            // drawn above each other.

            ArrayList<PieEntry> entries = new ArrayList<PieEntry>();

            // NOTE: The order of the entries when being added to the entries array determines their position around the center of
            // the chart.
            entries.add(new PieEntry(green,
                    (int)green + " Compliance Verified",
                    null));

            entries.add(new PieEntry(yellow,
                    (int)yellow + " Compliance Processing",
                    null));

            entries.add(new PieEntry(red,
                    (int)red + " Pending Tasks",
                    null));

            PieDataSet dataSet = new PieDataSet(entries, "");
            dataSet.setDrawIcons(false);

            dataSet.setSliceSpace(0f);
            dataSet.setIconsOffset(new MPPointF(0, 40));
            dataSet.setSelectionShift(5f);

            final int[] VORDIPLOM_COLORS = {
                    Color.parseColor("#29ffc6"), Color.parseColor("#ff5f6d"), Color.parseColor("#ffb971")
            };
            dataSet.setColors(VORDIPLOM_COLORS);
            dataSet.setDrawValues(false);   //  disable drawing the y-values
            pieChart.setDrawSliceText(false);   //  disable drawing the x-labels for the chart-slices

            PieData data = new PieData(dataSet);
            pieChart.setData(data);
            pieChart.setCenterText(stateName + " \nOutlets Ranking");
            // undo all highlights
            pieChart.highlightValues(null);
            pieChart.notifyDataSetChanged();
            pieChart.invalidate();




//            dataSet.setDrawValues(false);
//
////            xVals = new ArrayList<>();
////
////            xVals.add((int)green + " Compliance Verified");
////            xVals.add((int)yellow + " Compliance Processing");
////            xVals.add((int)red + " Pending Tasks");
//
////            PieData data = new PieData(xVals, dataSet);
//            pieChart.setDrawSliceText(false);
//            // In Percentage term
////            data.setValueFormatter(new PercentFormatter());
//            // Default value
//            //data.setValueFormatter(new DefaultValueFormatter(0));
////            pieChart.setData(data);
//            pieChart.setDescription("");
//            pieChart.setCenterText(stateName + " \nOutlets Ranking");
//            final int[] VORDIPLOM_COLORS = {
//                    Color.parseColor("#29ffc6"), Color.parseColor("#ff5f6d"), Color.parseColor("#ffb971")
//            };
//            dataSet.setColors(VORDIPLOM_COLORS);
////            data.setValueTextSize(20f);
////            data.setValueTextColor(Color.RED);
////        pieChart.setOnChartValueSelectedListener(this);
//            pieChart.notifyDataSetChanged();
//            pieChart.invalidate();
////        pieChart.animateXY(1400, 1400);
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.push_right_enter, R.anim.push_right_exit);
    }
}
