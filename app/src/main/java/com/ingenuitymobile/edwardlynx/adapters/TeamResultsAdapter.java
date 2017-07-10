package com.ingenuitymobile.edwardlynx.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bignerdranch.expandablerecyclerview.Adapter.ExpandableRecyclerAdapter;
import com.bignerdranch.expandablerecyclerview.Model.ParentListItem;
import com.bignerdranch.expandablerecyclerview.ViewHolder.ChildViewHolder;
import com.bignerdranch.expandablerecyclerview.ViewHolder.ParentViewHolder;
import com.ingenuitymobile.edwardlynx.R;
import com.ingenuitymobile.edwardlynx.api.models.Survey;

import java.util.List;

/**
 * Created by riddick on 7/5/17.
 */

public class TeamResultsAdapter extends ExpandableRecyclerAdapter<TeamResultsAdapter.ParentView, TeamResultsAdapter.ChildView> {

    public TeamResultsAdapter(List<ParentListItem> data) {
        super(data);
    }

    class ParentView extends ParentViewHolder {
        TextView nameText;
        ParentView(View itemView) {
            super(itemView);
            nameText = (TextView) itemView.findViewById(R.id.text_name);
        }

        @Override
        public void setExpanded(boolean expanded) {
            super.setExpanded(expanded);
        }
    }

    class ChildView extends ChildViewHolder {
        TextView reportText;
        RelativeLayout layoutBody;
        ImageView imageView;
        ChildView(View itemView) {
            super(itemView);
            reportText = (TextView) itemView.findViewById(R.id.text_name);
            layoutBody = (RelativeLayout) itemView.findViewById(R.id.layout_body);
            imageView = (ImageView) itemView.findViewById(R.id.image_download);
        }

    }

    @Override
    public TeamResultsAdapter.ParentView onCreateParentViewHolder(ViewGroup parent) {
        return new TeamResultsAdapter.ParentView(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_survey_results, parent, false));
    }

    @Override
    public TeamResultsAdapter.ChildView onCreateChildViewHolder(ViewGroup childViewGroup) {
        return new TeamResultsAdapter.ChildView(LayoutInflater.from(childViewGroup.getContext())
                .inflate(R.layout.list_survey_reports, childViewGroup, false));
    }

    @Override
    public void onBindParentViewHolder(final TeamResultsAdapter.ParentView holder, int position,
                                       ParentListItem parentListItem) {
        final Survey survey = (Survey) parentListItem;
        final Context context = holder.itemView.getContext();

        holder.nameText.setText(survey.name);
    }

    @Override
    public void onBindChildViewHolder(final TeamResultsAdapter.ChildView holder, int position, Object childListItem) {
        final String string = (String) childListItem;
        final Context context = holder.itemView.getContext();
        holder.reportText.setText(string);

        holder.layoutBody.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Report Preview", Toast.LENGTH_SHORT).show();
                context.startActivity(new Intent(Intent.ACTION_VIEW ,
                        Uri.parse("http://docs.google.com/gview?embedded=true&url=" +
                                "https://www.antennahouse.com/XSLsample/pdf/sample-link_1.pdf"))); // Sample PDF file - Replace when final
            }
        });

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Downloading Report", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.antennahouse.com/XSLsample/pdf/sample-link_1.pdf"));
                context.startActivity(intent);
            }
        });
    }

}

