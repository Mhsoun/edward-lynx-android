package com.ingenuitymobile.edwardlynx.fragments;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ingenuitymobile.edwardlynx.R;
import com.ingenuitymobile.edwardlynx.SessionStore;
import com.ingenuitymobile.edwardlynx.Shared;
import com.ingenuitymobile.edwardlynx.api.bodyparams.UserBody;
import com.ingenuitymobile.edwardlynx.api.models.User;
import com.ingenuitymobile.edwardlynx.api.responses.Response;
import com.ingenuitymobile.edwardlynx.utils.LogUtil;
import com.ingenuitymobile.edwardlynx.utils.ViewUtil;

import retrofit.RetrofitError;
import rx.Subscriber;

/**
 * Created by mEmEnG-sKi on 30/01/2017.
 */

public class ChangePasswordFragment extends BaseFragment {

  private View     mainView;
  private EditText oldPasswordEdit;
  private EditText newPasswordEdit;
  private EditText rePasswordEdit;
  private TextView saveText;

  /**
   * Fragment to display the fields for changing password.
   * @param ctx the context to be used in this fragment
   * @return the created fragment
   */
  public static ChangePasswordFragment newInstance(Context ctx) {
    ChangePasswordFragment fragment = new ChangePasswordFragment();
    Bundle bundle = new Bundle();
    bundle.putString("title", ctx.getString(R.string.change_password).toUpperCase());
    fragment.setArguments(bundle);
    return fragment;
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {

    mainView = inflater.inflate(R.layout.fragment_change_password, container, false);
    LogUtil.e("AAA onCreateView profile");
    initViews();
    return mainView;
  }

  /**
   * initViews initializes views used in the fragment
   */
  private void initViews() {
    oldPasswordEdit = (EditText) mainView.findViewById(R.id.edit_current_password);
    newPasswordEdit = (EditText) mainView.findViewById(R.id.edit_new_password);
    rePasswordEdit = (EditText) mainView.findViewById(R.id.edit_new_re_password);

    rePasswordEdit.setOnEditorActionListener(editorActionListener);

    saveText = (TextView) mainView.findViewById(R.id.text_save);
    saveText.setOnClickListener(onClickListener);
  }

  /**
   * the listener for clicking the change password button
   */
  private View.OnClickListener onClickListener = new View.OnClickListener() {
    @Override
    public void onClick(View view) {
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
        public void onCompleted() {
          saveText.setText(getString(R.string.save));
          oldPasswordEdit.setText("");
          newPasswordEdit.setText("");
          rePasswordEdit.setText("");
          SessionStore.savePassword(newPassword, getActivity());
        }

        @Override
        public void onError(Throwable e) {
          progressDialog.dismiss();
          if (e != null) {
            Response response = (Response) ((RetrofitError) e).getBody();
            if (response != null) {
              if (response.errors != null) {
                if (response.errors.currentPassword != null &&
                    !response.errors.currentPassword.isEmpty()) {
                  oldPasswordEdit.setError(response.errors.currentPassword.get(0));
                  oldPasswordEdit.requestFocus();
                }

                if (response.errors.password != null &&
                    !response.errors.password.isEmpty()) {
                  newPasswordEdit.setError(response.errors.password.get(0));
                  newPasswordEdit.requestFocus();
                }

              } else if (!TextUtils.isEmpty(response.message)) {
                ViewUtil.showAlert(getActivity(), getString(R.string.error),
                    response.message);
              }
            }
          }
        }

        @Override
        public void onNext(User userResponse) {
          progressDialog.dismiss();
          Toast.makeText(getActivity(), getString(R.string.password_updated), Toast.LENGTH_SHORT)
              .show();
        }
      }));
    }
  };

  /**
   * listener for clicking done on the keyboard, clicks the change password button
   */
  private TextView.OnEditorActionListener editorActionListener = new TextView
      .OnEditorActionListener() {
    @Override
    public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
      if (actionId == EditorInfo.IME_ACTION_DONE) {

        saveText.performClick();
        return true;
      }
      return false;
    }
  };
}
