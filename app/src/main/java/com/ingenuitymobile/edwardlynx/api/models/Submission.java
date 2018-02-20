package com.ingenuitymobile.edwardlynx.api.models;

import com.google.gson.annotations.SerializedName;

public class Submission {

    @SerializedName("name")
    public String name;

    @SerializedName("id")
    public int id;

    @SerializedName("email")
    public String email;
}