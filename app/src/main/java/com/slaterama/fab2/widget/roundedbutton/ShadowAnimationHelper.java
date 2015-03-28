package com.slaterama.fab2.widget.roundedbutton;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.StateListAnimator;
import android.annotation.TargetApi;
import android.os.Build;
import android.util.StateSet;
import android.view.View;

import com.slaterama.fab2.R;

import static com.slaterama.fab2.widget.roundedbutton.RoundedButtonHelper.RoundedButtonDelegate;
import static com.slaterama.fab2.widget.roundedbutton.RoundedButtonHelper.BASE_SPECS;
import static com.slaterama.fab2.widget.roundedbutton.RoundedButtonHelper.ELEVATION_PROPERTY;
import static com.slaterama.fab2.widget.roundedbutton.RoundedButtonHelper.PRESSED_SPECS;
import static com.slaterama.fab2.widget.roundedbutton.RoundedButtonHelper.SPECS_ARRAY;
import static com.slaterama.fab2.widget.roundedbutton.RoundedButtonHelper.TRANSLATION_Z_PROPERTY;

public abstract class ShadowAnimationHelper {

	RoundedButtonDelegate mDelegate;
	View mView;

	static ShadowAnimationHelper newInstance(RoundedButtonDelegate delegate) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			return new ShadowAnimationHelperLollipop(delegate);
		} else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			return new ShadowAnimationHelperHoneycomb(delegate);
		} else {
			return new ShadowAnimationHelperBase(delegate);
		}
	}

	public ShadowAnimationHelper(RoundedButtonDelegate delegate) {
		super();
		mDelegate = delegate;
		try {
			mView = (View) delegate;
		} catch (ClassCastException e) {
			throw new ClassCastException("delegate must be of type View");
		}
	}

	static class ShadowAnimationHelperBase extends ShadowAnimationHelper {
		public ShadowAnimationHelperBase(RoundedButtonDelegate delegate) {
			super(delegate);
		}
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	static class ShadowAnimationHelperHoneycomb extends ShadowAnimationHelper {
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

		public ShadowAnimationHelperHoneycomb(RoundedButtonDelegate delegate) {
			super(delegate);
		}
	}

	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	static class ShadowAnimationHelperLollipop extends ShadowAnimationHelperHoneycomb {
		StateListAnimator mStateListAnimator;

		public ShadowAnimationHelperLollipop(RoundedButtonDelegate delegate) {
			super(delegate);
		}

		public StateListAnimator getStateListAnimator() {
			return mStateListAnimator;
		}

		public StateListAnimator configureStateListAnimator(float elevation,
		                                                    float pressedTranslationZ) {
			mStateListAnimator = new StateListAnimator();
			for (int[] specs : SPECS_ARRAY) {
				mStateListAnimator.addState(specs, createAnimatorForState(
						specs, mView, elevation, pressedTranslationZ));
			}
			return mStateListAnimator;
		}
	}
}
