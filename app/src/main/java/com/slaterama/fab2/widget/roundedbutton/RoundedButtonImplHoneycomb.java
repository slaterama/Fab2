package com.slaterama.fab2.widget.roundedbutton;

import android.annotation.TargetApi;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class RoundedButtonImplHoneycomb extends RoundedButtonImplEclairMr1 {
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

	class RoundedButtonDrawableHoneycomb extends RoundedButtonDrawableEclairMr1 {
	}
}
