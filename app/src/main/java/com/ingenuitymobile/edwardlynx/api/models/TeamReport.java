package com.ingenuitymobile.edwardlynx.api.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by riddick on 7/13/17.
 */

public class TeamReport {
    @SerializedName("id")
    @Expose
    public int id;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("link")
    @Expose
    public String link;

}

