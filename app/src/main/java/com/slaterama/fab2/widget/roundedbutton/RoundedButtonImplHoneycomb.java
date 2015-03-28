package com.slaterama.fab2.widget.roundedbutton;

import android.annotation.TargetApi;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class RoundedButtonImplHoneycomb extends RoundedButtonImpl {
	public RoundedButtonImplHoneycomb(View view, RoundedButtonOptions options) {
		super(view, options);
	}

	@Override
	RoundedButtonDrawable newRoundedButtonDrawable() {
		return new RoundedButtonDrawableHoneycomb();
	}

	@Override
	void setSupportBackground(Drawable background) {
		mView.setBackgroundDrawable(background);
	}

	class RoundedButtonDrawableHoneycomb extends RoundedButtonDrawable {
		@Override
		public void draw(Canvas canvas) {
// TODO			canvas.drawRoundRect(bounds, rx, ry, paint);
		}
	}
}
