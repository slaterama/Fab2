package com.slaterama.fab2.widget.roundedbutton;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.StateListAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Build;
import android.util.StateSet;
import android.view.View;

import com.slaterama.fab2.R;

import static com.slaterama.fab2.widget.roundedbutton.RoundedButtonHelper.RoundedButtonDelegate;
import static com.slaterama.fab2.widget.roundedbutton.RoundedButtonHelper.RoundedButtonImpl;
import static com.slaterama.fab2.widget.roundedbutton.RoundedButtonHelper.RoundedButtonOptions;

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class RoundedButtonImplLollipop
		implements RoundedButtonImpl {

	static int[] PRESSED_SPECS = new int[]{android.R.attr.state_pressed,
			android.R.attr.state_enabled};
	static int[] BASE_SPECS = new int[]{android.R.attr.state_enabled};

	static String ELEVATION_PROPERTY = "elevation";
	static String TRANSLATION_Z_PROPERTY = "translationZ";

	RoundedButtonDelegate mDelegate;
	View mView;
	ColorStateList mColor;
	Rect mContentPadding;
	float mCornerRadius;
	float mDisabledElevation;
//	float mElevation;
	Rect mInsetPadding;
	float mMaxElevation;
	float mPressedTranslationZ;
	boolean mPreventCornerOverlap;
//	float mTranslationZ;
	boolean mUseCompatAnimation;
	boolean mUseCompatPadding;

	StateListAnimator mSavedStateListAnimator;

	public RoundedButtonImplLollipop(RoundedButtonDelegate delegate, RoundedButtonOptions options) {
		super();
		mDelegate = delegate;
		try {
			mView = (View) delegate;
		} catch (ClassCastException e) {
			throw new ClassCastException("delegate must be of type View");
		}
		mColor = options.color;
		mContentPadding = options.contentPadding;
		mCornerRadius = options.cornerRadius;
		mDisabledElevation = options.disabledElevation;
		mView.setElevation(options.elevation);
		mInsetPadding = options.insetPadding;
		mMaxElevation = options.maxElevation;
		mPressedTranslationZ = options.pressedTranslationZ;
		mPreventCornerOverlap = options.preventCornerOverlap;
		mView.setTranslationZ(options.translationZ);
		mUseCompatAnimation = options.useCompatAnimation;
		if (mUseCompatAnimation) {
			mSavedStateListAnimator = mView.getStateListAnimator();
			mView.setStateListAnimator(newStateListAnimator(mView, mDisabledElevation,
					mPressedTranslationZ));
		}
		mUseCompatPadding = options.useCompatPadding;
	}

	@Override
	public void setColor(ColorStateList color) {

	}

	@Override
	public int getContentPaddingLeft() {
		return mContentPadding.left;
	}

	@Override
	public int getContentPaddingTop() {
		return mContentPadding.top;
	}

	@Override
	public int getContentPaddingRight() {
		return mContentPadding.right;
	}

	@Override
	public int getContentPaddingBottom() {
		return mContentPadding.bottom;
	}

	@Override
	public void setContentPadding(int left, int top, int right, int bottom) {
		if (left != mContentPadding.left || top != mContentPadding.top
				|| right != mContentPadding.right || bottom != mContentPadding.bottom) {
			mContentPadding.set(left, top, right, bottom);
			// TODO: paddingChange only (+ requestLayout)
		}
	}

	@Override
	public float getCornerRadius() {
		return mCornerRadius;
	}

	@Override
	public void setCornerRadius(float cornerRadius) {
		if (cornerRadius != mCornerRadius) {
			mCornerRadius = cornerRadius;
			// TODO: draw change
			// TODO: possible padding change IF preventCornerOverlap (+ requestLayout)
		}
	}

	@Override
	public int getInsetPaddingLeft() {
		return mInsetPadding.left;
	}

	@Override
	public int getInsetPaddingTop() {
		return mInsetPadding.top;
	}

	@Override
	public int getInsetPaddingRight() {
		return mInsetPadding.right;
	}

	@Override
	public int getInsetPaddingBottom() {
		return mInsetPadding.bottom;
	}

	@Override
	public void setInsetPadding(int left, int top, int right, int bottom) {
		if (left != mInsetPadding.left || top != mInsetPadding.top
				|| right != mInsetPadding.right || bottom != mInsetPadding.bottom) {
			mInsetPadding.set(left, top, right, bottom);
			// TODO: draw change
			// TODO: paddingChange (+ requestLayout)
		}
	}

	@Override
	public float getMaxElevation() {
		return mMaxElevation;
	}

	@Override
	public void setMaxElevation(float maxElevation) {
		if (maxElevation != mMaxElevation) {
			mMaxElevation = maxElevation;
			// TODO: draw change (if < Lollipop or useCompatPadding)
			// TODO: paddingChange (if < Lollipop or useCompatPadding)
		}
	}

	@Override
	public boolean isPreventCornerOverlap() {
		return mPreventCornerOverlap;
	}

	@Override
	public void setPreventCornerOverlap(boolean preventCornerOverlap) {
		if (preventCornerOverlap != mPreventCornerOverlap) {
			mPreventCornerOverlap = preventCornerOverlap;
			// TODO: paddingChange (+ requestLayout)
		}
	}

	@Override
	public float getSupportElevation() {
		return mView.getElevation();
	}

	@Override
	public void setSupportElevation(float elevation) {
		mView.setElevation(elevation);
	}

	@Override
	public float getSupportTranslationZ() {
		return mView.getTranslationZ();
	}

	@Override
	public void setSupportTranslationZ(float translationZ) {
		mView.setTranslationZ(translationZ);
	}

	@Override
	public boolean isUseCompatAnimation() {
		return mUseCompatAnimation;
	}

	@Override
	public void setUseCompatAnimation(boolean useCompatAnimation) {
		if (useCompatAnimation != mUseCompatAnimation) {
			mUseCompatAnimation = useCompatAnimation;
			if (mUseCompatAnimation) {
				mSavedStateListAnimator = mView.getStateListAnimator();
				mView.setStateListAnimator(newStateListAnimator(mView, mDisabledElevation,
						mPressedTranslationZ));
			} else {
				mView.setStateListAnimator(mSavedStateListAnimator);
			}
		}
	}

	@Override
	public boolean isUseCompatPadding() {
		return false;
	}

	@Override
	public void setUseCompatPadding(boolean useCompatPadding) {

	}

	static StateListAnimator newStateListAnimator(View view,
	                                              float disabledElevation,
	                                              float pressedTranslationZ) {
		Resources resources = view.getResources();
		float elevation = view.getElevation();
		int duration = resources.getInteger(R.integer.qslib_button_pressed_animation_duration);
		int startDelay = resources.getInteger(R.integer.qslib_button_pressed_animation_delay);

		StateListAnimator animator = new StateListAnimator();
		AnimatorSet set = new AnimatorSet();
		ObjectAnimator translationZAnimator = ObjectAnimator.ofFloat(view, TRANSLATION_Z_PROPERTY,
				pressedTranslationZ).setDuration(duration);
		ObjectAnimator elevationAnimator = ObjectAnimator.ofFloat(view, ELEVATION_PROPERTY,
				elevation).setDuration(0L);
		set.playTogether(translationZAnimator, elevationAnimator);
		animator.addState(PRESSED_SPECS, set);

		set = new AnimatorSet();
		translationZAnimator = ObjectAnimator.ofFloat(view, TRANSLATION_Z_PROPERTY,
				pressedTranslationZ, 0f).setDuration(duration);
		translationZAnimator.setStartDelay(startDelay);
		elevationAnimator = ObjectAnimator.ofFloat(view, ELEVATION_PROPERTY, elevation)
				.setDuration(0L);
		set.playTogether(translationZAnimator, elevationAnimator);
		animator.addState(BASE_SPECS, set);

		set = new AnimatorSet();
		translationZAnimator = ObjectAnimator.ofFloat(view, TRANSLATION_Z_PROPERTY, 0f)
				.setDuration(0L);
		elevationAnimator = ObjectAnimator.ofFloat(view, ELEVATION_PROPERTY, disabledElevation)
				.setDuration(0L);
		set.playTogether(translationZAnimator, elevationAnimator);
		animator.addState(StateSet.WILD_CARD, set);
		return animator;
	}
}
