package com.ingenuitymobile.edwardlynx.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.ingenuitymobile.edwardlynx.R;
import com.ingenuitymobile.edwardlynx.api.models.YesNo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by memengski on 6/1/17.
 */

public class YesNoAdapter extends RecyclerView.Adapter<YesNoAdapter.ViewHolder> {

  private static final float FONT_SIZE = 11;

  private List<YesNo> data;

  public YesNoAdapter(List<YesNo> data) {
    super();
    this.data = data;
  }

  class ViewHolder extends RecyclerView.ViewHolder {
    TextView titleText;
    TextView questionText;
    PieChart pieChart;

    ViewHolder(View itemView) {
      super(itemView);

      titleText = (TextView) itemView.findViewById(R.id.text_title);
      questionText = (TextView) itemView.findViewById(R.id.text_question);
      pieChart = (PieChart) itemView.findViewById(R.id.pie_chart);
      pieChart.setNoDataText(itemView.getContext().getString(R.string.no_chart_data_available));
      pieChart.invalidate();

      pieChart.setUsePercentValues(true);
      pieChart.getDescription().setEnabled(false);
      pieChart.setDrawHoleEnabled(false);
      pieChart.setRotationEnabled(false);
      pieChart.setHighlightPerTapEnabled(false);

      Legend l = pieChart.getLegend();
      l.setVerticalAlignment(Legend.LegendVerticalAlignment.CENTER);
      l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
      l.setOrientation(Legend.LegendOrientation.VERTICAL);
      l.setDrawInside(false);
      l.setXEntrySpace(7f);
      l.setYEntrySpace(0f);
      l.setTextSize(11f);
      l.setTextColor(Color.WHITE);
      l.setYOffset(0f);
    }
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    return new ViewHolder(LayoutInflater.from(parent.getContext())
        .inflate(R.layout.list_yes_no, parent, false));
  }

  @Override
  public void onBindViewHolder(ViewHolder holder, int position) {
    final Context context = holder.itemView.getContext();
    final YesNo yesNo = data.get(position);

    holder.titleText.setText(yesNo.category);
    holder.questionText.setText(yesNo.question);

    ArrayList<PieEntry> entries = new ArrayList<>();
    entries.add(new PieEntry((float) yesNo.yesPercentage, context.getString(R.string.yes)));
    entries.add(new PieEntry((float) yesNo.noPercentage, context.getString(R.string.no)));

    PieDataSet dataSet = new PieDataSet(entries, "");
    dataSet.setDrawIcons(false);
    dataSet.setValueTextColor(Color.WHITE);
    dataSet.setSliceSpace(0f);
    dataSet.setColors(
        context.getResources().getColor(R.color.inactive_color),
        context.getResources().getColor(R.color.lynx_color)
    );
    dataSet.setValueFormatter(new IValueFormatter() {
      @Override
      public String getFormattedValue(float value, Entry entry, int dataSetIndex,
          ViewPortHandler viewPortHandler) {
        if (entry.getY() != 0) {
          return String.valueOf((int) value) + "%";
        }
        return "";
      }
    });

    PieData data = new PieData(dataSet);
    data.setValueTextSize(11f);
    data.setValueTextColor(Color.WHITE);
    holder.pieChart.setData(data);
    holder.pieChart.highlightValues(null);
    holder.pieChart.setDrawEntryLabels(false);
    holder.pieChart.invalidate();
  }

  @Override
  public int getItemCount() {
    return data.size();
  }

}
