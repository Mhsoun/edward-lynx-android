package com.ingenuitymobile.edwardlynx.api.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.ingenuitymobile.edwardlynx.api.models.Links;
import com.ingenuitymobile.edwardlynx.api.models.TeamReportItem;
import com.ingenuitymobile.edwardlynx.api.models.TeamReportLinks;

import java.util.List;

/**
 * Created by riddick on 7/13/17.
 */

public class TeamReportResponse {
    @SerializedName("_links")
    @Expose
    public TeamReportLinks links;
    @SerializedName("items")
    @Expose
    public List<TeamReportItem> items = null;
}
