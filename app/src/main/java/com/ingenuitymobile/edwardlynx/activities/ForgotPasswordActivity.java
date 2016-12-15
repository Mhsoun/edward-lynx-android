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

import com.ingenuitymobile.edwardlynx.R;
import com.ingenuitymobile.edwardlynx.utils.StringUtil;
import com.ingenuitymobile.edwardlynx.views.EditTextGroup;

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
