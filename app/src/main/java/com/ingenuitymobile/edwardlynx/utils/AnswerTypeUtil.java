package com.ingenuitymobile.edwardlynx.utils;

import android.content.Context;

import com.ingenuitymobile.edwardlynx.R;
import com.ingenuitymobile.edwardlynx.api.models.Answer;

/**
 * Created by memengski on 3/22/17.
 */

public class AnswerTypeUtil {

  public static int getIntType(Context ctx, String type) {
    if (type.equals(ctx.getString(R.string.numeric_1_to_5))) {
      return Answer.NUMERIC_1_5_SCALE;
    } else if (type.equals(ctx.getString(R.string.numeric_1_to_10))) {
      return Answer.NUMERIC_1_10_SCALE;
    } else if (type.equals(ctx.getString(R.string.agreement_scale))) {
      return Answer.AGREEMENT_SCALE;
    } else if (type.equals(ctx.getString(R.string.yes_or_no))) {
      return Answer.YES_OR_NO;
    } else if (type.equals(ctx.getString(R.string.strong_agreement_scale))) {
      return Answer.STRONG_AGREEMENT_SCALE;
    } else if (type.equals(ctx.getString(R.string.free_text))) {
      return Answer.CUSTOM_TEXT;
    } else if (type.equals(ctx.getString(R.string.reverse_agreement_scale))) {
      return Answer.REVERSE_AGREEMENT_SCALE;
    } else if (type.equals(ctx.getString(R.string.numeric_1_to_10_explanation))) {
      return Answer.NUMERIC_1_10_WITH_EXPLANATION;
    } else if (type.equals(ctx.getString(R.string.custom_scale))) {
      return Answer.CUSTOM_SCALE;
    }
    return -1;
  }
}
