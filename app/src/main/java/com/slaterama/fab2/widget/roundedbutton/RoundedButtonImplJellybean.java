package com.slaterama.fab2.widget.roundedbutton;

import android.annotation.TargetApi;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
public class RoundedButtonImplJellybean extends RoundedButtonImpl {
	public RoundedButtonImplJellybean(View view, RoundedButtonOptions options) {
		super(view, options);
	}

	@Override
	RoundedButtonDrawable newRoundedButtonDrawable() {
		return new RoundedButtonDrawableJellybean();
	}

	@Override
	void setSupportBackground(Drawable background) {
		mView.setBackground(background);
	}

	class RoundedButtonDrawableJellybean extends RoundedButtonDrawable {
		@Override
		public void draw(Canvas canvas) {
// TODO			canvas.drawRoundRect(bounds, rx, ry, paint);
		}
	}
}
