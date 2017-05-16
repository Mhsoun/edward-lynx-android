package com.ingenuitymobile.edwardlynx.activities;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ingenuitymobile.edwardlynx.R;
import com.ingenuitymobile.edwardlynx.Shared;
import com.ingenuitymobile.edwardlynx.api.bodyparams.UserBody;
import com.ingenuitymobile.edwardlynx.api.models.User;
import com.ingenuitymobile.edwardlynx.api.responses.Response;
import com.ingenuitymobile.edwardlynx.utils.NetUtil;
import com.ingenuitymobile.edwardlynx.utils.ViewUtil;

import retrofit.RetrofitError;
import rx.Subscriber;

public class ChangePasswordActivity extends BaseActivity {

  private EditText oldPasswordEdit;
  private EditText newPasswordEdit;
  private EditText rePasswordEdit;
  private TextView saveText;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_change_password);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    initViews();
  }

  private void initViews() {
    oldPasswordEdit = (EditText) findViewById(R.id.edit_current_password);
    newPasswordEdit = (EditText) findViewById(R.id.edit_new_password);
    rePasswordEdit = (EditText) findViewById(R.id.edit_new_re_password);

    rePasswordEdit.setOnEditorActionListener(editorActionListener);

    saveText = (TextView) findViewById(R.id.text_save);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == android.R.id.home) {
      finish();
    }
    return super.onOptionsItemSelected(item);
  }

  public void createAccount(View v) {
    final String oldPassword = oldPasswordEdit.getText().toString();
    final String newPassword = newPasswordEdit.getText().toString();
    final String rePassword = rePasswordEdit.getText().toString();

    if (TextUtils.isEmpty(oldPassword)) {
      oldPasswordEdit.setError(getString(R.string.old_password_required));
      oldPasswordEdit.requestFocus();
      return;
    }

    if (TextUtils.isEmpty(newPassword)) {
      newPasswordEdit.setError(getString(R.string.new_password_required));
      newPasswordEdit.requestFocus();
      return;
    }

    if (TextUtils.isEmpty(rePassword) || !newPassword.equals(rePassword)) {
      rePasswordEdit.setError(getString(R.string.password_not_match));
      rePasswordEdit.requestFocus();
      return;
    }

    hideKeyboard();

    UserBody body = new UserBody();
    body.currentPassword = oldPassword;
    body.password = newPassword;

    progressDialog.show();
    subscription.add(Shared.apiClient.updateUser(body, new Subscriber<User>() {
      @Override
      public void onCompleted() {}

      @Override
      public void onError(Throwable e) {
        progressDialog.dismiss();
        if (!NetUtil.hasActiveConnection(ChangePasswordActivity.this)) {
          Toast.makeText(ChangePasswordActivity.this, getString(R.string.no_internet_connection),
              Toast.LENGTH_SHORT).show();
        } else {
          Response response = (Response) ((RetrofitError) e).getBody();
          if (response != null) {
            if (response.errors != null) {
              if (!response.errors.currentPassword.isEmpty()) {
                oldPasswordEdit.setError(response.errors.currentPassword.get(0));
                oldPasswordEdit.requestFocus();
              }
            } else if (!TextUtils.isEmpty(response.message)) {
              ViewUtil.showAlert(ChangePasswordActivity.this, getString(R.string.error),
                  response.message);
            }
          }
        }
      }

      @Override
      public void onNext(User userResponse) {
        progressDialog.dismiss();
        Toast.makeText(ChangePasswordActivity.this, getString(R.string.password_updated),
            Toast.LENGTH_SHORT).show();
        finish();
      }
    }));
  }

  private TextView.OnEditorActionListener editorActionListener = new TextView
      .OnEditorActionListener() {
    @Override
    public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
      if (actionId == EditorInfo.IME_ACTION_DONE) {

        createAccount(textView);
        return true;
      }
      return false;
    }
  };

}
