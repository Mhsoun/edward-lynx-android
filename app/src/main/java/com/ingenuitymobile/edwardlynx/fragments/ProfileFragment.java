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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ingenuitymobile.edwardlynx.R;
import com.ingenuitymobile.edwardlynx.Shared;
import com.ingenuitymobile.edwardlynx.api.bodyparams.UserBody;
import com.ingenuitymobile.edwardlynx.api.models.User;
import com.ingenuitymobile.edwardlynx.api.responses.Response;
import com.ingenuitymobile.edwardlynx.utils.LogUtil;
import com.ingenuitymobile.edwardlynx.utils.NetUtil;
import com.ingenuitymobile.edwardlynx.utils.ViewUtil;

import retrofit.RetrofitError;
import rx.Subscriber;

/**
 * Created by mEmEnG-sKi on 30/01/2017.
 */

public class ProfileFragment extends BaseFragment {

  private View       mainView;
  private EditText   nameEdit;
  private EditText   infoEdit;
  private EditText   departmentEdit;
  private EditText   roleEdit;
  private RadioGroup genderRadiogroup;
  private EditText   genderEdit;
  private EditText   countryEdit;
  private EditText   cityEdit;
  private TextView   editText;
  private TextView   cancelText;

  private User   user;
  private String gender;

  public static ProfileFragment newInstance(Context ctx) {
    ProfileFragment fragment = new ProfileFragment();
    Bundle bundle = new Bundle();
    bundle.putString("title", ctx.getString(R.string.profile_bold));
    fragment.setArguments(bundle);
    return fragment;
  }


  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {

    mainView = inflater.inflate(R.layout.fragment_profile, container, false);
    LogUtil.e("AAA onCreateView profile");
    initViews();
    return mainView;
  }

  @Override
  public void onResume() {
    super.onResume();
    LogUtil.e("AAA onResume profile");
    getData();
  }

  private void getData() {
    editText.setText(getString(R.string.loading));
    subscription.add(Shared.apiClient.getMe(new Subscriber<User>() {
      @Override
      public void onCompleted() {
        editText.setOnClickListener(onClickListener);
        editText.setText(getString(R.string.edit));
      }

      @Override
      public void onError(Throwable e) {
        if (!NetUtil.hasActiveConnection(getActivity())) {
          Toast.makeText(getActivity(), getString(R.string.no_internet_connection),
              Toast.LENGTH_SHORT).show();
        } else {
          Response response = (Response) ((RetrofitError) e).getBody();
          if (response != null) {
            if (!TextUtils.isEmpty(response.message)) {
              ViewUtil.showAlert(getActivity(), getString(R.string.error), response.message);
            }
          }
        }
      }

      @Override
      public void onNext(User userResponse) {
        user = userResponse;
        setEditable(false);
      }
    }));
  }

  private void initViews() {
    nameEdit = (EditText) mainView.findViewById(R.id.edit_name);
    infoEdit = (EditText) mainView.findViewById(R.id.edit_info);
    departmentEdit = (EditText) mainView.findViewById(R.id.edit_department);
    roleEdit = (EditText) mainView.findViewById(R.id.edit_role);
    genderRadiogroup = (RadioGroup) mainView.findViewById(R.id.radiogroup_gender);
    genderEdit = (EditText) mainView.findViewById(R.id.edit_gender);
    countryEdit = (EditText) mainView.findViewById(R.id.edit_country);
    cityEdit = (EditText) mainView.findViewById(R.id.edit_city);
    editText = (TextView) mainView.findViewById(R.id.text_edit);
    cancelText = (TextView) mainView.findViewById(R.id.text_cancel);

    infoEdit.setOnEditorActionListener(editorActionListener);
    cancelText.setOnClickListener(onClickListener);

    genderRadiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
      @Override
      public void onCheckedChanged(RadioGroup radioGroup, int i) {
        RadioButton radioButton = (RadioButton) radioGroup.findViewById(i);
        gender = (String) radioButton.getTag();

        genderEdit.setVisibility(
            gender.equals(getString(R.string.other)) ? View.VISIBLE : View.GONE);
      }
    });
  }

  private void setUserData() {
    gender = user.gender;

    nameEdit.setText(user.name);
    infoEdit.setText(user.info);
    departmentEdit.setText(user.department);
    roleEdit.setText(user.role);
    countryEdit.setText(user.country);
    cityEdit.setText(user.city);

    genderEdit.setVisibility(View.GONE);

    if (user.gender.toLowerCase().equals(getString(R.string.male))) {
      genderRadiogroup.check(R.id.radiobutton_male);
    } else if (user.gender.toLowerCase().equals(getString(R.string.female))) {
      genderRadiogroup.check(R.id.radiobutton_female);
    } else {
      genderRadiogroup.check(R.id.radiobutton_other);

      genderEdit.setVisibility(View.VISIBLE);
      genderEdit.setText(user.gender);
    }

    for (int i = 0; i < genderRadiogroup.getChildCount(); i++) {
      genderRadiogroup.getChildAt(i).setEnabled(false);
    }
  }

  private void updateProfile() {
    final String name = nameEdit.getText().toString();
    final String info = infoEdit.getText().toString();
    final String department = departmentEdit.getText().toString();
    final String role = roleEdit.getText().toString();
    final String country = countryEdit.getText().toString();
    final String city = cityEdit.getText().toString();

    if (TextUtils.isEmpty(name)) {
      nameEdit.setError(getString(R.string.name_required));
      nameEdit.requestFocus();
      return;
    }

    UserBody body = new UserBody();
    body.name = name;

    if (!TextUtils.isEmpty(info)) {
      body.info = info;
    }

    if (!TextUtils.isEmpty(department)) {
      body.department = department;
    }

    if (!TextUtils.isEmpty(role)) {
      body.role = role;
    }

    if (!TextUtils.isEmpty(country)) {
      body.country = country;
    }

    if (!TextUtils.isEmpty(city)) {
      body.city = city;
    }

    if (!gender.toLowerCase().equals(getString(R.string.male)) &&
        !gender.toLowerCase().equals(getString(R.string.female))) {
      final String genderFromEdit = genderEdit.getText().toString();

      if (TextUtils.isEmpty(genderFromEdit)) {
        genderEdit.setError(getString(R.string.gender_required));
        genderEdit.requestFocus();
        return;
      }

      body.gender = genderFromEdit;
    } else {
      body.gender = gender;
    }

    hideKeyboard();
    editText.setText(getString(R.string.loading));

    subscription.add(Shared.apiClient.updateUser(body, new Subscriber<User>() {
      @Override
      public void onCompleted() {
        Toast.makeText(getActivity(), getString(R.string.user_updated), Toast.LENGTH_SHORT).show();
        setEditable(false);
      }

      @Override
      public void onError(Throwable e) {
        editText.setText(getString(R.string.save));
        if (!NetUtil.hasActiveConnection(getActivity())) {
          Toast.makeText(getActivity(), getString(R.string.no_internet_connection),
              Toast.LENGTH_SHORT).show();
        } else {
          Response response = (Response) ((RetrofitError) e).getBody();
          if (response != null) {
            if (!TextUtils.isEmpty(response.message)) {
              ViewUtil.showAlert(getActivity(), getString(R.string.error), response.message);
            }
          }
        }
      }

      @Override
      public void onNext(User userResponse) {
        Shared.user = userResponse;
      }
    }));
  }

  private void setEditable(boolean isEdit) {
    setUserData();
    editText.setText(isEdit ? getString(R.string.save) : getString(R.string.edit));
    cancelText.setVisibility(isEdit ? View.VISIBLE : View.GONE);
    nameEdit.setEnabled(isEdit);
    nameEdit.setSelection(nameEdit.getText().length());
    infoEdit.setEnabled(isEdit);
    infoEdit.setSelection(infoEdit.getText().length());
    departmentEdit.setEnabled(isEdit);
    departmentEdit.setSelection(departmentEdit.getText().length());
    roleEdit.setEnabled(isEdit);
    roleEdit.setSelection(roleEdit.getText().length());
    genderEdit.setEnabled(isEdit);
    genderEdit.setSelection(genderEdit.getText().length());
    countryEdit.setEnabled(isEdit);
    countryEdit.setSelection(countryEdit.getText().length());
    cityEdit.setEnabled(isEdit);
    cityEdit.setSelection(cityEdit.getText().length());

    for (int i = 0; i < genderRadiogroup.getChildCount(); i++) {
      genderRadiogroup.getChildAt(i).setEnabled(isEdit);
    }
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
        if (editText.getText().toString().equals(getString(R.string.save))) {
          updateProfile();
        } else if (editText.getText().toString().equals(getString(R.string.edit))) {
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
