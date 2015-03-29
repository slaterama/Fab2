package com.slaterama.fab2.widget.roundedbutton;

import android.annotation.TargetApi;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
public class RoundedButtonImplJellybean extends RoundedButtonImplHoneycomb {
	public RoundedButtonImplJellybean(View view, RoundedButtonAttributes attributes) {
		super(view, attributes);
	}

	@Override
	void setSupportBackground(Drawable background) {
		mView.setBackground(background);
	}
}
