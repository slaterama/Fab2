package com.slaterama.fab2.widget.roundedbutton;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.os.Build;
import android.view.View;

import com.slaterama.fab2.R;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class RoundedButtonImplHoneycomb extends RoundedButtonImplEclairMr1 {

	static AnimatorSet createAnimator(
			ButtonState buttonState, View view, String evelationPropertyName,
			String translationZPropertyName, float elevation, float pressedTranslationZ) {
		AnimatorSet animator = new AnimatorSet();
		int duration = view.getResources().getInteger(
				R.integer.qslib_button_pressed_animation_duration);
		switch (buttonState) {
			case PRESSED:
				animator.playTogether(ObjectAnimator.ofFloat(view, translationZPropertyName,
								pressedTranslationZ).setDuration(duration),
						ObjectAnimator.ofFloat(view, evelationPropertyName, elevation)
								.setDuration(0L));
				break;
			case DEFAULT:
				animator.playTogether(ObjectAnimator.ofFloat(view, translationZPropertyName, 0f)
								.setDuration(duration),
						ObjectAnimator.ofFloat(view, evelationPropertyName, elevation)
								.setDuration(0L));
				break;
			case WILD_CARD:
			default:
				animator.playTogether(ObjectAnimator.ofFloat(view, translationZPropertyName, 0f)
								.setDuration(0L),
						ObjectAnimator.ofFloat(view, evelationPropertyName, 0f).setDuration(0L));
		}
		return animator;
	}

	public RoundedButtonImplHoneycomb(View view) {
		super(view);
	}

	@Override
	RoundedButtonDrawable newRoundedButtonDrawable() {
		return new RoundedButtonDrawableHoneycomb();
	}

	@Override
	RoundedButtonDrawable newRoundedButtonDrawable() {
		return new RoundedButtonDrawableHoneycomb();
	}

	class RoundedButtonDrawableHoneycomb extends RoundedButtonDrawableEclairMr1 {
		AnimatorSet mShadowAnimatorSet;

		@Override
		void onButtonStateChange(ButtonState buttonState) {
			if (mShadowAnimatorSet != null) {
				mShadowAnimatorSet.cancel();
			}
			mShadowAnimatorSet = createAnimator(buttonState, mView,
					SUPPORT_ELEVATION_PROPERTY, SUPPORT_TRANSLATION_Z_PROPERTY,
					mEnabledElevation, mPressedTranslationZ);
			mShadowAnimatorSet.start();
		}
	}
}
