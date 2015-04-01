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

	static AnimatorSet createAnimatorForState(
			int[] state, View view, String evelationPropertyName, String translationZPropertyName,
			float elevation, float pressedTranslationZ) {

		// TODO Close, but I might need an "mIsAnimating" variable. When that is set,
		// setElevation will set mAnimatingElevation, and setTranslationZ will set
		// mAnimatingTranslationZ.

		AnimatorSet animator = new AnimatorSet();
		int duration = view.getResources().getInteger(
				R.integer.qslib_button_pressed_animation_duration);
		if (StateSet.stateSetMatches(PRESSED_SPECS, state)) {
			animator.playTogether(ObjectAnimator.ofFloat(view, translationZPropertyName,
							pressedTranslationZ).setDuration(duration),
					ObjectAnimator.ofFloat(view, evelationPropertyName, elevation)
							.setDuration(0L));
		} else if (StateSet.stateSetMatches(BASE_SPECS, state)) {
			animator.playTogether(ObjectAnimator.ofFloat(view, translationZPropertyName, 0f)
							.setDuration(duration),
					ObjectAnimator.ofFloat(view, evelationPropertyName, elevation)
							.setDuration(0L));
		} else {
			animator.playTogether(ObjectAnimator.ofFloat(view, translationZPropertyName, 0f)
							.setDuration(0L),
					ObjectAnimator.ofFloat(view, evelationPropertyName, 0f).setDuration(0L));
		}
		return animator;
	}

	public RoundedButtonImplHoneycomb(View view, RoundedButtonAttributes attributes) {
		super(view, attributes);
	}

	class RoundedButtonDrawableHoneycomb extends RoundedButtonDrawableEclairMr1 {
		AnimatorSet mShadowAnimatorSet;

		@Override
		protected boolean onStateChange(int[] state) {
			boolean retVal = super.onStateChange(state);
			if (mInitialized) {
				if (mShadowAnimatorSet != null) {
					mShadowAnimatorSet.cancel();
				}
				mShadowAnimatorSet = createAnimatorForState(state, mView,
						SUPPORT_ELEVATION_PROPERTY, SUPPORT_TRANSLATION_Z_PROPERTY,
						mElevation, mPressedTranslationZ);
				mShadowAnimatorSet.start();
			}
			return retVal;
		}
	}
}
