package com.slaterama.fab2.widget.roundedbutton;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.os.Build;
import android.util.StateSet;
import android.view.View;

import com.slaterama.fab2.R;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class RoundedButtonImplHoneycomb extends RoundedButtonImplEclairMr1 {

	static AnimatorSet createAnimatorForState(int[] state, View view, float elevation,
	                                          float pressedTranslationZ) {
		AnimatorSet animator = new AnimatorSet();
		int duration = view.getResources().getInteger(
				R.integer.qslib_button_pressed_animation_duration);
		if (StateSet.stateSetMatches(PRESSED_SPECS, state)) {
			animator.playTogether(ObjectAnimator.ofFloat(view, TRANSLATION_Z_PROPERTY,
							pressedTranslationZ).setDuration(duration),
					ObjectAnimator.ofFloat(view, ELEVATION_PROPERTY, elevation)
							.setDuration(0L));
		} else if (StateSet.stateSetMatches(BASE_SPECS, state)) {
			animator.playTogether(ObjectAnimator.ofFloat(view, TRANSLATION_Z_PROPERTY, 0f)
							.setDuration(duration),
					ObjectAnimator.ofFloat(view, ELEVATION_PROPERTY, elevation)
							.setDuration(0L));
		} else {
			animator.playTogether(ObjectAnimator.ofFloat(view, TRANSLATION_Z_PROPERTY, 0f)
							.setDuration(0L),
					ObjectAnimator.ofFloat(view, ELEVATION_PROPERTY, 0f).setDuration(0L));
		}
		return animator;
	}

	public RoundedButtonImplHoneycomb(View view, RoundedButtonAttributes attributes) {
		super(view, attributes);
	}

	// TODO Needs its own animation logic
}
