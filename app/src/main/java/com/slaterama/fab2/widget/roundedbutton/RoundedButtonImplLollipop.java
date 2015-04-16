package com.slaterama.fab2.widget.roundedbutton;

import android.animation.StateListAnimator;
import android.annotation.TargetApi;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.Outline;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.os.Build;
import android.util.TypedValue;
import android.view.View;

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class RoundedButtonImplLollipop extends RoundedButtonImplHoneycomb {

	static StateListAnimator createStateListAnimator(View view, float elevation,
	                                                 float pressedTranslationZ) {
		StateListAnimator stateListAnimator = new StateListAnimator();
		ButtonState[] values = ButtonState.values();
		for (ButtonState value : values) {
			stateListAnimator.addState(value.getState(), createAnimator(value, view,
					ELEVATION_PROPERTY, TRANSLATION_Z_PROPERTY, elevation, pressedTranslationZ));
		}
		return stateListAnimator;
	}

	StateListAnimator mSavedStateListAnimator;

	public RoundedButtonImplLollipop(View view, RoundedButtonAttributes attributes) {
		super(view, attributes);
		view.setElevation(attributes.elevation);
		view.setTranslationZ(attributes.translationZ);
		if (mUseCompatAnimation) {
			mSavedStateListAnimator = view.getStateListAnimator();
			view.setStateListAnimator(createStateListAnimator(mView, mElevation,
					mPressedTranslationZ));
		}
		view.setClipToOutline(true);
	}

	@Override
	RoundedButtonDrawable newRoundedButtonDrawable() {
		return new RoundedButtonDrawableLollipop();
	}

	@Override
	void onElevationChanged(float elevation) {
		mView.setElevation(elevation);
	}

	@Override
	void onTranslationZChanged(float translationZ) {
		mView.setTranslationZ(translationZ);
	}

	@Override
	protected boolean willUseCompatAnimation() {
		return mUseCompatAnimation;
	}

	@Override
	protected boolean willUseCompatPadding() {
		return mUseCompatPadding;
	}

	@Override
	void onUseCompatAnimationChanged(boolean useCompatAnimation) {
		if (useCompatAnimation) {
			mSavedStateListAnimator = mView.getStateListAnimator();
			mView.setStateListAnimator(createStateListAnimator(mView, mEnabledElevation,
					mPressedTranslationZ));
		} else {
			mView.setStateListAnimator(mSavedStateListAnimator);
			mSavedStateListAnimator = null;
		}
	}

	@Override
	void onUseCompatPaddingChanged(boolean useCompatPadding) {
		mRoundedButtonDrawable.invalidateBounds();
		invalidatePadding();
	}

	@Override
	Drawable getBackgroundDrawable(RoundedButtonDrawable roundedButtonDrawable) {
		TypedValue outValue = new TypedValue();
		mView.getContext().getTheme().resolveAttribute(android.R.attr.colorControlHighlight,
				outValue, true);
		ColorStateList rippleColor = ColorStateList.valueOf(outValue.data);
		return new RippleDrawable(rippleColor, roundedButtonDrawable, null);
	}

	class RoundedButtonDrawableLollipop extends RoundedButtonDrawable {
		@Override
		public void getOutline(Outline outline) {
			outline.setRoundRect(mBounds, mCornerRadius);
		}

		@Override
		void onButtonStateChange(ButtonState buttonState) {
			// NO OP
		}
	}
}
