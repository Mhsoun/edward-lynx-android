package com.ingenuitymobile.edwardlynx.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatRadioButton;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.ingenuitymobile.edwardlynx.R;
import com.ingenuitymobile.edwardlynx.api.models.Option;
import com.ingenuitymobile.edwardlynx.api.models.Question;
import com.ingenuitymobile.edwardlynx.utils.LogUtil;

import java.util.List;

import info.hoang8f.android.segmented.SegmentedGroup;

/**
 * Created by mEmEnG-sKi on 10/01/2017.
 */

public class SurveyQuestionsAdapter extends
    RecyclerView.Adapter<RecyclerView.ViewHolder> {

  private final int CATEGORY = 0;
  private final int DATA     = 1;

  private List<Question>       data;
  private boolean              isEnabled;
  private OnAnswerItemListener listener;

  public SurveyQuestionsAdapter(List<Question> data, OnAnswerItemListener listener) {
    super();
    this.data = data;
    this.listener = listener;
    isEnabled = true;
  }

  private class SectionViewHolder extends RecyclerView.ViewHolder {
    TextView nameText;

    SectionViewHolder(View itemView) {
      super(itemView);
      nameText = (TextView) itemView.findViewById(R.id.text_category_name);
    }
  }

  private class DataViewHolder extends RecyclerView.ViewHolder {
    TextView       questionText;
    SegmentedGroup segmentedGroup;
    RadioGroup     radioGroup;
    EditText       editText;


    DataViewHolder(View itemView) {
      super(itemView);
      questionText = (TextView) itemView.findViewById(R.id.text_question);
      segmentedGroup = (SegmentedGroup) itemView.findViewById(R.id.segmented_group);
      radioGroup = (RadioGroup) itemView.findViewById(R.id.group_button);
      editText = (EditText) itemView.findViewById(R.id.edit_text);
    }
  }

  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    switch (viewType) {
    case CATEGORY:
      return new SurveyQuestionsAdapter.SectionViewHolder(LayoutInflater.from(parent.getContext())
          .inflate(R.layout.list_survey_section, parent, false));
    default:
      return new SurveyQuestionsAdapter.DataViewHolder(LayoutInflater.from(parent.getContext())
          .inflate(R.layout.list_survey_questions, parent, false));
    }
  }

  @Override
  public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, int position) {
    final Context context = viewHolder.itemView.getContext();
    final Question question = data.get(position);

    if (question.isSectionHeader) {
      final SectionViewHolder holder = (SectionViewHolder) viewHolder;
      holder.nameText.setText(question.text);
    } else {
      final DataViewHolder holder = (DataViewHolder) viewHolder;
      holder.questionText.setText(question.text + (question.optional == 1 ? " " +
          context.getResources().getString(R.string.optional) : ""));
      holder.editText.setVisibility(View.GONE);

      holder.radioGroup.setVisibility(question.answer.isNumeric ? View.GONE : View.VISIBLE);
      holder.segmentedGroup.setVisibility(question.answer.isNumeric ? View.VISIBLE : View.GONE);

      if (question.answer.options != null) {
        holder.radioGroup.removeAllViews();
        holder.segmentedGroup.removeAllViews();
        for (Option option : question.answer.options) {
          if (question.answer.isNumeric) {
            createSegmentedButton(holder.segmentedGroup,
                context, option.description, option.value);
          } else {
            createRadioButton(holder.radioGroup,
                context, option.description, option.value);
          }

        }

        if (question.isNA == 1) {
          if (question.answer.isNumeric) {
            createSegmentedButton(holder.segmentedGroup, context,
                context.getString(R.string.not_available), -1);
          } else {
            createRadioButton(holder.radioGroup, context,
                context.getString(R.string.not_available), -1);
          }
        }

        holder.radioGroup = question.answer.isNumeric ? holder.segmentedGroup : holder.radioGroup;

        if (question.value != null) {
          listener.onAnswer(question.id, String.valueOf((double) question.value));
          for (int i = 0; i < holder.radioGroup.getChildCount(); i++) {
            holder.radioGroup.getChildAt(i).setClickable(isEnabled);

            if (holder.radioGroup.getChildAt(i).getTag().equals(
                String.valueOf((int) (double) question.value))) {
              ((RadioButton) holder.radioGroup.getChildAt(i)).setChecked(true);
              listener.onAnswer(question.id, (String) holder.radioGroup.getChildAt(i).getTag());
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
      } else if (question.answer.decscription.equals(
          context.getResources().getString(R.string.free_text_description))) {
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

        holder.editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
          @Override
          public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus) {
              try {
                InputMethodManager inputMethodManager = (InputMethodManager)
                    context.getSystemService(Activity.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
              } catch (Exception e) {}
            }
          }
        });
      }
    }
  }

  @Override
  public int getItemCount() {
    return data.size();
  }

  @Override
  public int getItemViewType(int position) {
    if (data.get(position).isSectionHeader) {
      return CATEGORY;
    }
    return DATA;
  }

  private void createRadioButton(final RadioGroup radioGroup, final Context context,
      final String description, int value) {
    final AppCompatRadioButton radioButton = new AppCompatRadioButton(context);
    final LinearLayout.LayoutParams lparam = new LinearLayout.LayoutParams(
        LinearLayout.LayoutParams.WRAP_CONTENT,
        LinearLayout.LayoutParams.WRAP_CONTENT);
    lparam.setMargins(0, 20, 0, 0);
    radioButton.setLayoutParams(lparam);
    radioButton.setTextColor(context.getResources().getColor(R.color.white));
    radioButton.setTag(String.valueOf(value));
    radioButton.setText(description);
    radioButton.setTextSize(14);
    radioGroup.addView(radioButton);
  }

  private void createSegmentedButton(final SegmentedGroup radioGroup, final Context context,
      final String description, int value) {

    final RadioButton radioButton = (RadioButton) LayoutInflater
        .from(context)
        .inflate(R.layout.radio_button_item,
            null
        );

    radioButton.setTag(String.valueOf(value));
    radioButton.setText(description);
    radioGroup.addView(radioButton);
    radioGroup.updateBackground();
  }

  public void isEnabled(boolean isEnabled) {
    this.isEnabled = isEnabled;
    notifyDataSetChanged();
  }

  public interface OnAnswerItemListener {
    void onAnswer(long id, String value);
  }
}
