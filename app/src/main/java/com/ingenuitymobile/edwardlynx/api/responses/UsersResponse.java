package com.ingenuitymobile.edwardlynx.api.responses;

import com.google.gson.annotations.SerializedName;
import com.ingenuitymobile.edwardlynx.api.models.Links;
import com.ingenuitymobile.edwardlynx.api.models.User;

import java.util.List;

/**
 * Created by mEmEnG-sKi on 11/01/2017.
 */

public class UsersResponse extends Response {

  @SerializedName("_links")
  public Links      links;
  @SerializedName("items")
  public List<User> users;
}
