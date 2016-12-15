package com.ingenuitymobile.edwardlynx.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ingenuitymobile.edwardlynx.R;
import com.ingenuitymobile.edwardlynx.views.EditTextGroup;

/**
 * Created by mEmEnG-sKi on 13/12/2016.
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

  private void initViews() {
    final EditText usernameEdit = (EditText) findViewById(R.id.edit_username);
    final EditText passwordEdit = (EditText) findViewById(R.id.edit_password);
    final ImageView usernameImage = (ImageView) findViewById(R.id.image_profile);
    final ImageView passwordImage = (ImageView) findViewById(R.id.image_lock);
    final TextView usernameErrorText = (TextView) findViewById(R.id.text_username_error);
    final TextView passwordErrorText = (TextView) findViewById(R.id.text_password_error);

    loginText = (TextView) findViewById(R.id.text_login);

    usernameGroup = new EditTextGroup(usernameEdit, usernameImage, usernameErrorText);
    passwordGroup = new EditTextGroup(passwordEdit, passwordImage, passwordErrorText);

    passwordEdit.setOnEditorActionListener(editorActionListener);
  }

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
  }

  public void forgotPassword(View v) {
    startActivity(new Intent(this, ForgotPasswordActivity.class));
  }

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
