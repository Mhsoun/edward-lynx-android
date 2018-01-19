package com.ingenuitymobile.edwardlynx.api.models;

import com.bignerdranch.expandablerecyclerview.Model.ParentListItem;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by riddick on 7/13/17.
 * API model for team report survey.
 */

public class TeamReportSurvey implements ParentListItem {
        @SerializedName("id")
        @Expose
        public int id;
        @SerializedName("name")
        @Expose
        public String name;
        @SerializedName("type")
        @Expose
        public int type;
        @SerializedName("reports")
        @Expose
        public List<TeamReport> reports = null;

        @Override
        public List<?> getChildItemList() {
                return reports;
        }

        @Override
        public boolean isInitiallyExpanded() {
                return false;
        }
}
