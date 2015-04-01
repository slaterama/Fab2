package com.slaterama.fab2.widget.roundedbutton;

import android.annotation.TargetApi;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.view.View;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
public class RoundedButtonImplJellybeanMr1 extends RoundedButtonImplJellybean {
	public RoundedButtonImplJellybeanMr1(View view, RoundedButtonAttributes attributes) {
		super(view, attributes);
	}

	@Override
	RoundedButtonDrawable newRoundedButtonDrawable() {
		return new RoundedButtonDrawableJellybeanMr1();
	}

	class RoundedButtonDrawableJellybeanMr1 extends RoundedButtonDrawableHoneycomb {
		@Override
		protected void drawRoundRect(Canvas canvas, RectF rect, float rx, float ry, Paint paint) {
			canvas.drawRoundRect(rect, rx, ry, paint);
		}
	}
}
