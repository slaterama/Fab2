package com.slaterama.fab2.widget.roundedbutton;

import android.annotation.TargetApi;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;

@TargetApi(Build.VERSION_CODES.ECLAIR_MR1)
public class RoundedButtonImplEclairMr1 extends RoundedButtonImpl {
	public RoundedButtonImplEclairMr1(View view, RoundedButtonOptions options) {
		super(view, options);
	}

	@Override
	RoundedButtonDrawable newRoundedButtonDrawable() {
		return new RoundedButtonDrawableEclairMr1();
	}

	@Override
	void setSupportBackground(Drawable background) {
		mView.setBackgroundDrawable(background);
	}

	class RoundedButtonDrawableEclairMr1 extends RoundedButtonDrawable {
		@Override
		public void draw(Canvas canvas) {
// TODO			canvas.drawRoundRect(bounds, rx, ry, paint);
		}
	}
}
