package com.ingenuitymobile.edwardlynx.adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.ingenuitymobile.edwardlynx.R;
import com.ingenuitymobile.edwardlynx.api.models.Option;
import com.ingenuitymobile.edwardlynx.api.models.Question;
import com.ingenuitymobile.edwardlynx.utils.LogUtil;

import java.util.List;

/**
 * Created by mEmEnG-sKi on 10/01/2017.
 */

public class QuestionsAdapter extends RecyclerView.Adapter<QuestionsAdapter.ViewHolder> {

  private List<Question>       data;
  private OnAnswerItemListener listener;

  public QuestionsAdapter(List<Question> data, OnAnswerItemListener listener) {
    super();
    this.data = data;
    this.listener = listener;
  }

  class ViewHolder extends RecyclerView.ViewHolder {
    TextView   helpText;
    TextView   questionText;
    RadioGroup radioGroup;
    EditText   editText;

    ViewHolder(View itemView) {
      super(itemView);
      questionText = (TextView) itemView.findViewById(R.id.text_question);
      helpText = (TextView) itemView.findViewById(R.id.text_help);
      radioGroup = (RadioGroup) itemView.findViewById(R.id.group_button);
      editText = (EditText) itemView.findViewById(R.id.edit_text);
    }
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    return new QuestionsAdapter.ViewHolder(LayoutInflater.from(parent.getContext())
        .inflate(R.layout.list_questions, parent, false));
  }

  @Override
  public void onBindViewHolder(final ViewHolder holder, int position) {
    final Context context = holder.itemView.getContext();
    final Question question = data.get(position);
    holder.questionText.setText(question.text);
    holder.helpText.setText(question.answer.help);
    holder.editText.setVisibility(View.GONE);
    if (question.answer.options != null) {
      for (Option option : question.answer.options) {
        createRadioButton(holder.radioGroup, context, option.description);
      }

      createRadioButton(holder.radioGroup, context, "N/A");

      holder.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup radioGroup, int i) {
          LogUtil.e("AAA " + i);

          int value = -1;
          if (i <= question.answer.options.size()) {
            value = i - 1;
          }
          listener.onAnswer(question.id, String.valueOf(value));
        }
      });
    } else if (question.answer.decscription.equals("Text")) {
      holder.editText.setVisibility(View.VISIBLE);

      listener.onAnswer(question.id, holder.editText.getText().toString());
    }
  }

  @Override
  public int getItemCount() {
    return data.size();
  }

  private void createRadioButton(final RadioGroup radioGroup, final Context context,
      final String description) {
    final RadioButton radioButton = new RadioButton(context);
    final LayoutParams lparam = new LayoutParams(LayoutParams.WRAP_CONTENT,
        LayoutParams.WRAP_CONTENT);
    radioButton.setLayoutParams(lparam);
    radioButton.setTextColor(context.getResources().getColor(R.color.white));
    int textColor = Color.parseColor("#ffffff");
    radioButton.setButtonTintList(ColorStateList.valueOf(textColor));
    radioButton.setText(description);
    radioGroup.addView(radioButton);
  }

  public interface OnAnswerItemListener {
    public void onAnswer(long id, String value);
  }
}
