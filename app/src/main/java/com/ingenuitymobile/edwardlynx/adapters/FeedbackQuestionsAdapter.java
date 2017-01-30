package com.ingenuitymobile.edwardlynx.adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
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

import java.util.List;

/**
 * Created by mEmEnG-sKi on 10/01/2017.
 */

public class FeedbackQuestionsAdapter extends RecyclerView.Adapter<FeedbackQuestionsAdapter.ViewHolder> {

  private List<Question>       data;
  private boolean              isEnabled;
  private OnAnswerItemListener listener;

  public FeedbackQuestionsAdapter(List<Question> data, OnAnswerItemListener listener) {
    super();
    this.data = data;
    this.listener = listener;
    isEnabled = true;
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
    return new FeedbackQuestionsAdapter.ViewHolder(LayoutInflater.from(parent.getContext())
        .inflate(R.layout.list_feedback_questions, parent, false));
  }

  @Override
  public void onBindViewHolder(final ViewHolder holder, int position) {
    final Context context = holder.itemView.getContext();
    final Question question = data.get(position);
    holder.questionText.setText(question.text);
    holder.helpText.setText(question.answer.help);
    holder.editText.setVisibility(View.GONE);
    if (question.answer.options != null) {
      holder.radioGroup.removeAllViews();
      for (Option option : question.answer.options) {
        createRadioButton(holder.radioGroup, context, option.description, option.value);
      }

      if (question.isNA == 1) {
        createRadioButton(holder.radioGroup, context, "N/A", -1);
      }

      if (question.value != null) {
        listener.onAnswer(question.id, String.valueOf((double) question.value));
        for (int i = 0; i < holder.radioGroup.getChildCount(); i++) {
          holder.radioGroup.getChildAt(i).setEnabled(isEnabled);

          if (holder.radioGroup.getChildAt(i).getTag().equals(
              String.valueOf((int) (double) question.value))) {
            ((RadioButton) holder.radioGroup.getChildAt(i)).setChecked(true);
          }
        }
      }

      holder.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup radioGroup, int i) {
          RadioButton radioButton = (RadioButton) radioGroup.findViewById(i);
          listener.onAnswer(question.id, (String) radioButton.getTag());
        }
      });


    } else if (question.answer.decscription.equals("Text")) {
      holder.editText.setVisibility(View.VISIBLE);
      holder.editText.setEnabled(isEnabled);
      if (question.value != null) {
        holder.editText.setText((String) question.value);
        holder.editText.setSelection(holder.editText.getText().length());
        listener.onAnswer(question.id, (String) question.value);
      }
      holder.editText.addTextChangedListener(new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
          listener.onAnswer(question.id, holder.editText.getText().toString());
        }
      });
    }
  }

  @Override
  public int getItemCount() {
    return data.size();
  }

  @Override
  public int getItemViewType(int position) {
    return super.getItemViewType(position);
  }

  private void createRadioButton(final RadioGroup radioGroup, final Context context,
      final String description, int value) {
    final RadioButton radioButton = new RadioButton(context);
    final LayoutParams lparam = new LayoutParams(LayoutParams.WRAP_CONTENT,
        LayoutParams.WRAP_CONTENT);
    radioButton.setLayoutParams(lparam);
    radioButton.setTextColor(context.getResources().getColor(R.color.white));
    radioButton.setTag(String.valueOf(value));
    int textColor = Color.parseColor("#ffffff");
    radioButton.setButtonTintList(ColorStateList.valueOf(textColor));
    radioButton.setText(description);
    radioGroup.addView(radioButton);
  }

  public void isEnabled(boolean isEnabled) {
    this.isEnabled = isEnabled;
    notifyDataSetChanged();
  }

  public interface OnAnswerItemListener {
    public void onAnswer(long id, String value);
  }
}
