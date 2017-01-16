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

public class ProfileActivity extends BaseActivity {

  private EditText nameEdit;
  private EditText infoEdit;
  private TextView editText;
  private TextView cancelText;

  private User user;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    initViews();

    editText.setText(getString(R.string.loading));
    Shared.apiClient.getMe(new Subscriber<User>() {
      @Override
      public void onCompleted() {
        editText.setText("Edit");
      }

      @Override
      public void onError(Throwable e) {
        if (!NetUtil.hasActiveConnection(ProfileActivity.this)) {
          Toast.makeText(ProfileActivity.this, getString(R.string.no_internet_connection),
              Toast.LENGTH_SHORT).show();
        } else {
          Response response = (Response) ((RetrofitError) e).getBody();
          if (response != null) {
            if (!TextUtils.isEmpty(response.message)) {
              ViewUtil.showAlert(ProfileActivity.this, getString(R.string.error),
                  response.message);
            }
          }
        }
      }

      @Override
      public void onNext(User userResponse) {
        user = userResponse;
        setUserData();
      }
    });
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == android.R.id.home) {
      finish();
    }
    return super.onOptionsItemSelected(item);
  }

  private void initViews() {
    nameEdit = (EditText) findViewById(R.id.edit_name);
    infoEdit = (EditText) findViewById(R.id.edit_info);
    editText = (TextView) findViewById(R.id.text_edit);
    cancelText = (TextView) findViewById(R.id.text_cancel);

    infoEdit.setOnEditorActionListener(editorActionListener);
    editText.setOnClickListener(onClickListener);
    cancelText.setOnClickListener(onClickListener);
  }

  private void setUserData() {
    nameEdit.setText(user.name);
    infoEdit.setText(user.info);
  }

  private void updateProfile() {
    final String name = nameEdit.getText().toString();
    final String info = infoEdit.getText().toString();

    if (TextUtils.isEmpty(name)) {
      nameEdit.setError(getString(R.string.first_name_required));
      nameEdit.requestFocus();
      return;
    }

    hideKeyboard();
    editText.setText(getString(R.string.loading));

    UserBody body = new UserBody();
    body.name = name;

    if (!TextUtils.isEmpty(info)) {
      body.info = info;
    }

    Shared.apiClient.updateUser(body, new Subscriber<User>() {
      @Override
      public void onCompleted() {
        setEditable(false);
      }

      @Override
      public void onError(Throwable e) {
        editText.setText("Save");
        if (!NetUtil.hasActiveConnection(ProfileActivity.this)) {
          Toast.makeText(ProfileActivity.this, getString(R.string.no_internet_connection),
              Toast.LENGTH_SHORT).show();
        } else {
          Response response = (Response) ((RetrofitError) e).getBody();
          if (response != null) {
            if (!TextUtils.isEmpty(response.message)) {
              ViewUtil.showAlert(ProfileActivity.this, getString(R.string.error),
                  response.message);
            }
          }
        }
      }

      @Override
      public void onNext(User userResponse) {
        Toast.makeText(ProfileActivity.this, "User updated", Toast.LENGTH_SHORT).show();
        finish();
      }
    });
  }

  private void setEditable(boolean isEdit) {
    setUserData();
    editText.setText(isEdit ? "Save" : "Edit");
    cancelText.setVisibility(isEdit ? View.VISIBLE : View.GONE);
    nameEdit.setEnabled(isEdit);
    nameEdit.setSelection(nameEdit.getText().length());
    infoEdit.setEnabled(isEdit);
    infoEdit.setSelection(infoEdit.getText().length());
  }

  private TextView.OnEditorActionListener editorActionListener = new TextView
      .OnEditorActionListener() {
    @Override
    public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
      if (actionId == EditorInfo.IME_ACTION_DONE) {
        updateProfile();
        return true;
      }
      return false;
    }
  };

  private View.OnClickListener onClickListener = new View.OnClickListener() {
    @Override
    public void onClick(View view) {
      switch (view.getId()) {
      case R.id.text_edit:
        if (editText.getText().toString().equals("Save")) {
          updateProfile();
        } else {
          setEditable(true);
        }
        break;
      case R.id.text_cancel:
        setEditable(false);
        break;
      }
    }
  };

}
