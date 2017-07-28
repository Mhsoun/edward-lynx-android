package com.ingenuitymobile.edwardlynx.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ingenuitymobile.edwardlynx.R;
import com.ingenuitymobile.edwardlynx.Shared;
import com.ingenuitymobile.edwardlynx.api.bodyparams.ForgotPassword;
import com.ingenuitymobile.edwardlynx.api.responses.Response;
import com.ingenuitymobile.edwardlynx.utils.LogUtil;
import com.ingenuitymobile.edwardlynx.utils.StringUtil;
import com.ingenuitymobile.edwardlynx.views.EditTextGroup;

import rx.Subscriber;

/**
 * Created by mEmEnG-sKi on 15/12/2016.
 */

public class ForgotPasswordActivity extends BaseActivity {

  private EditTextGroup emailGroup;
  private TextView      recoverText;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_forgot_password);

    context = this;

    initViews();
  }

  private void initViews() {
    final EditText emailEdit = (EditText) findViewById(R.id.edit_email);
    final ImageView emailImage = (ImageView) findViewById(R.id.image_profile);
    final TextView emailErrorText = (TextView) findViewById(R.id.text_email_error);

    recoverText = (TextView) findViewById(R.id.text_recover);

    emailGroup = new EditTextGroup(emailEdit, emailImage, emailErrorText);

    emailEdit.setOnEditorActionListener(editorActionListener);
  }

  public void recover(View v) {
    emailGroup.removeError();

    if (TextUtils.isEmpty(emailGroup.getEditTextSting())) {
      emailGroup.setError(getString(R.string.email_required));
      return;
    }

    if (!StringUtil.isValidEmail(emailGroup.getEditTextSting())) {
      emailGroup.setError(getString(R.string.valid_email_required));
      return;
    }

    recoverText.setText(getString(R.string.loading));

    ForgotPassword param = new ForgotPassword(emailGroup.getEditTextSting());

    LogUtil.e("AAA " + param.toString());

    subscription.add(Shared.apiClient.postForgotPassword(param
        , new Subscriber<Response>() {
          @Override
          public void onCompleted() {
            LogUtil.e("AAA onCompleted");
            finish();
          }

          @Override
          public void onError(Throwable e) {
            LogUtil.e("AAA onError");
            recoverText.setText(getString(R.string.recover));
          }

          @Override
          public void onNext(Response response) {
            LogUtil.e("AAA onNext");
            Toast.makeText(
                context,
                getString(R.string.forgot_password_message),
                Toast.LENGTH_SHORT
            ).show();
          }
        }));
  }

  public void login(View v) {
    finish();
  }

  private TextView.OnEditorActionListener editorActionListener = new TextView
      .OnEditorActionListener() {
    @Override
    public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
      if (actionId == EditorInfo.IME_ACTION_DONE) {
        hideKeyboard();

        recover(textView);
        return true;
      }
      return false;
    }
  };
}
