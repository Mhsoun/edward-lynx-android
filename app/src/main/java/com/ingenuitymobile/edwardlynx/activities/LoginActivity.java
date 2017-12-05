package com.ingenuitymobile.edwardlynx.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ingenuitymobile.edwardlynx.R;
import com.ingenuitymobile.edwardlynx.SessionStore;
import com.ingenuitymobile.edwardlynx.Shared;
import com.ingenuitymobile.edwardlynx.api.responses.Authentication;
import com.ingenuitymobile.edwardlynx.api.responses.Response;
import com.ingenuitymobile.edwardlynx.utils.LogUtil;
import com.ingenuitymobile.edwardlynx.utils.ViewUtil;
import com.ingenuitymobile.edwardlynx.views.EditTextGroup;

import retrofit.RetrofitError;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by mEmEnG-sKi on 13/12/2016.
 * Activity for the login screen and login logic handling of the app.
 * Also includes the options to reset password and contact Edward Lynx.
 */

public class LoginActivity extends BaseActivity {

  private EditTextGroup usernameGroup;
  private EditTextGroup passwordGroup;
  private TextView      loginText;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);

    initViews();
  }

  /**
   * initViews initializes views used in the activity
   */
  private void initViews() {
    final EditText usernameEdit = (EditText) findViewById(R.id.edit_username);
    final EditText passwordEdit = (EditText) findViewById(R.id.edit_password);
    final ImageView usernameImage = (ImageView) findViewById(R.id.image_profile);
    final ImageView passwordImage = (ImageView) findViewById(R.id.image_lock);
    final TextView usernameErrorText = (TextView) findViewById(R.id.text_username_error);
    final TextView passwordErrorText = (TextView) findViewById(R.id.text_password_error);

    loginText = (TextView) findViewById(R.id.text_login);

    LogUtil.e("AAA " + SessionStore.restoreUsername(this));

    usernameGroup = new EditTextGroup(usernameEdit, usernameImage, usernameErrorText);
    passwordGroup = new EditTextGroup(passwordEdit, passwordImage, passwordErrorText);

    passwordEdit.setOnEditorActionListener(editorActionListener);

    usernameEdit.setText(SessionStore.restoreUsername(this));
    passwordEdit.setText(SessionStore.restorePassword(this));

    loginText.setText(getString(R.string.login).toUpperCase());
  }

  /**
   * action invoked when the login button is clicked, will try user login
   * @param v
   */
  public void login(View v) {
    usernameGroup.removeError();
    passwordGroup.removeError();

    if (TextUtils.isEmpty(usernameGroup.getEditTextSting())) {
      usernameGroup.setError(getString(R.string.username_required));
      usernameGroup.requestFocus();
      return;
    }

    if (TextUtils.isEmpty(passwordGroup.getEditTextSting())) {
      passwordGroup.setError(getString(R.string.password_required));
      passwordGroup.requestFocus();
      return;
    }

    loginText.setText(getString(R.string.loading));


    subscription.add(Shared.apiClient.postLogin(usernameGroup.getEditTextSting(),
        passwordGroup.getEditTextSting())
        .subscribeOn(Schedulers.newThread())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Subscriber<Authentication>() {
          @Override
          public void onCompleted() {}

          @Override
          public void onError(Throwable e) {
            loginText.setText(getString(R.string.login).toUpperCase());
            if (e != null) {
              Response response = (Response) ((RetrofitError) e).getBody();
              if (response != null) {
                ViewUtil.showAlert(LoginActivity.this, getString(R.string.error), response.message);
              }
            }
          }

          @Override
          public void onNext(Authentication authentication) {
            SessionStore.saveUsername(usernameGroup.getEditTextSting(), LoginActivity.this);
            SessionStore.savePassword(passwordGroup.getEditTextSting(), LoginActivity.this);
            SessionStore.saveRefreshToken(authentication.refresh_token, LoginActivity.this);
            SessionStore.saveAccessToken(authentication.accessToken, LoginActivity.this);
            Intent intent = new Intent(LoginActivity.this, SplashActivity.class);
            startActivity(intent);
            finish();
          }
        }));
  }

  /**
   * action invoked when the forgot password is clicked, redirects the user to ForgotPasswordActivity
   * @param v
   */
  public void forgotPassword(View v) {
    startActivity(new Intent(this, ForgotPasswordActivity.class));
  }

  /**
   * action invoked when the contact us is clicked, opens a webpage to contact Edward Lynx
   * @param v
   */
  public void contactUs(View v) {
    Intent i = new Intent(Intent.ACTION_VIEW);
    i.setData(Uri.parse("http://www.edwardlynx.com/contact/"));
    startActivity(i);
  }

  /**
   * listener for the input when the action is done, will try to login the user
   */
  private TextView.OnEditorActionListener editorActionListener = new TextView
      .OnEditorActionListener() {
    @Override
    public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
      if (actionId == EditorInfo.IME_ACTION_DONE) {
        hideKeyboard();

        login(textView);
        return true;
      }
      return false;
    }
  };
}
