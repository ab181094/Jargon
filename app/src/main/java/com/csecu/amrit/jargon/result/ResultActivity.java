package com.csecu.amrit.jargon.result;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
import android.widget.Toast;

import com.csecu.amrit.jargon.R;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class ResultActivity extends AppCompatActivity {
    PieChart pieChart;
    TextView textView;
    int correct = 0, wrong = 0;
    float success = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if (bundle != null) {
            correct = bundle.getInt("correct");
            wrong = bundle.getInt("wrong");
        }

        textView = (TextView) findViewById(R.id.tvResultComment);
        if (correct != 0 || wrong != 0) {
            success = (float) correct * 100;
            success = success / (float) (correct + wrong);
            if (success >= 80) {
                textView.setText("Your performance is excellent");
            } else if (success >= 50 && success < 80) {
                textView.setText("Your performance is average");
            } else if (success >= 0 && success < 50) {
                textView.setText("Your performance is mediocre");
            }
        }
        pieChart = (PieChart) findViewById(R.id.pieChart);
        pieChart.setDrawHoleEnabled(false);
        pieChart.setUsePercentValues(true);
        Description description = new Description();
        description.setTextColor(ColorTemplate.VORDIPLOM_COLORS[2]);
        description.setText("Success rate");
        pieChart.setDescription(description);

        addDataChart();

        /*pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                int pos = e.toString().indexOf("(sum): ");
                String value = e.toString().substring(pos + 7);

                toastIt(value);

                *//*if (value.equals(String.valueOf(correct))) {
                    // toastIt("Total correct: " + success + "%");
                    toastIt(String.valueOf(correct));
                } else if (value.equals(String.valueOf(wrong))) {
                    // toastIt("Total wrong: " + (100 - wrong) + "%");
                    toastIt(String.valueOf(wrong));
                }*//*

                toastIt();
            }

            @Override
            public void onNothingSelected() {

            }
        });*/
    }

    private void addDataChart() {
        ArrayList<PieEntry> entries = new ArrayList<>();
        ArrayList<String> list = new ArrayList<>();

        entries.add(new PieEntry((float) correct, 0));
        entries.add(new PieEntry((float) wrong, 1));

        list.add("Correct");
        list.add("Incorrect");

        PieDataSet pieDataSet = new PieDataSet(entries, "Success rate");
        pieDataSet.setSliceSpace(2);
        pieDataSet.setValueTextSize(12);

        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);

        Legend legend = pieChart.getLegend();
        legend.setForm(Legend.LegendForm.CIRCLE);

        PieData pieData = new PieData(pieDataSet);
        pieData.setValueFormatter(new PercentFormatter());
        pieChart.setData(pieData);
        pieChart.invalidate();
    }

    private void toastIt(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }
}
