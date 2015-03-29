package com.slaterama.fab2.widget.roundedbutton;

import android.annotation.TargetApi;
import android.os.Build;
import android.view.View;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class RoundedButtonImplHoneycomb extends RoundedButtonImplEclairMr1 {
	public RoundedButtonImplHoneycomb(View view, RoundedButtonAttributes attributes) {
		super(view, attributes);
	}

	// TODO Needs its own animation logic
}
