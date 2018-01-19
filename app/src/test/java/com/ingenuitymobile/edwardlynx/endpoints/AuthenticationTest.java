package com.ingenuitymobile.edwardlynx.endpoints;


import com.ingenuitymobile.edwardlynx.APIClientFactory;
import com.ingenuitymobile.edwardlynx.api.ApiClient;
import com.ingenuitymobile.edwardlynx.api.responses.Authentication;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.HashMap;
import java.util.Map;

import static junit.framework.Assert.assertNotNull;

/**
 * Created by mEmEnG-sKi on 17/10/2016.
 * Test case for login authentication.
 */

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AuthenticationTest extends BaseTest {

  @Test
  public void postLogin() throws Exception {
    System.out.println("\u2713 Authentication postLogin...");
    client = login();
  }
}
