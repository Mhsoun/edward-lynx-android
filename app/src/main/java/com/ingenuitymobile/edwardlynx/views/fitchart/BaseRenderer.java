package com.ingenuitymobile.edwardlynx.views.fitchart;

import android.graphics.RectF;

abstract class BaseRenderer {
  private final RectF         drawingArea;
  private final FitChartValue value;

  FitChartValue getValue() {
    return value;
  }

  RectF getDrawingArea() {
    return drawingArea;
  }

  public BaseRenderer(RectF drawingArea, FitChartValue value) {
    this.drawingArea = drawingArea;
    this.value = value;
  }
}
