package com.ingenuitymobile.edwardlynx.views.fitchart;

import android.graphics.Path;

interface Renderer {
  Path buildPath(float animationProgress, float animationSeek);
}
