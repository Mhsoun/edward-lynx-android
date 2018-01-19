package com.ingenuitymobile.edwardlynx.activities;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.ingenuitymobile.edwardlynx.R;
import com.ingenuitymobile.edwardlynx.Shared;
import com.ingenuitymobile.edwardlynx.api.bodyparams.ShareParam;
import com.ingenuitymobile.edwardlynx.api.responses.Response;
import com.ingenuitymobile.edwardlynx.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;

/**
 * Created by mEmEnG-sKi on 17/01/2017.
 * Activity to select people and share report to them.
 */

public class ShareReportActivity extends InviteBaseActivity {

  private long id;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_share_report);

    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    setTitle(getString(R.string.share_report).toUpperCase());

    id = getIntent().getLongExtra("id", 0L);

    initViews();
    getData();
  }

    /**
     * action to be invoked when the share button in the ShareReportActivity is clicked.
     * @param v
     */
  public void share(View v) {
    if (ids.isEmpty()) {
      Toast.makeText(ShareReportActivity.this, getString(R.string.select_atleast_one),
          Toast.LENGTH_SHORT).show();
    } else {
      List<Long> recipients = new ArrayList<>();
      for (String id : ids) {
        recipients.add(Long.parseLong(id));
      }

      ShareParam param = new ShareParam();
      param.users = recipients;
      LogUtil.e("AAA " + param.toString());

      progressDialog.show();
      subscription.add(Shared.apiClient.postShareInstantFeedback(id, param,
          new Subscriber<Response>() {
            @Override
            public void onCompleted() {
              LogUtil.e("AAA onCompleted");
              setResult(RESULT_OK);
              finish();
            }

            @Override
            public void onError(Throwable e) {
              progressDialog.dismiss();
              LogUtil.e("AAA onError");
            }

            @Override
            public void onNext(Response response) {
              LogUtil.e("AAA onNext");
              progressDialog.dismiss();
              Toast.makeText(ShareReportActivity.this, getString(R.string.shared_to_other_people),
                  Toast.LENGTH_SHORT).show();
            }
          }));
    }
  }

}
