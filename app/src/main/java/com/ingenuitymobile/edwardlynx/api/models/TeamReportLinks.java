package com.ingenuitymobile.edwardlynx.api.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by riddick on 7/13/17.
 * API model for team report links.
 */

public class TeamReportLinks {
    @SerializedName("self")
    @Expose
    public TeamReportSelf self;
}
