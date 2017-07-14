package com.ingenuitymobile.edwardlynx.api.models;

import java.util.List;

import com.bignerdranch.expandablerecyclerview.Model.ParentListItem;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by riddick on 7/13/17.
 */

public class TeamReportItem implements ParentListItem {
    @SerializedName("id")
    @Expose
    public int id;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("surveys")
    @Expose
    public List<TeamReportSurvey> surveys = null;

    @Override
    public List<?> getChildItemList() {
        return surveys;
    }

    @Override
    public boolean isInitiallyExpanded() {
        return false;
    }
}
