package com.slaterama.fab2.widget.roundedbutton;

import android.annotation.TargetApi;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;

import static com.slaterama.fab2.widget.roundedbutton.RoundedButtonHelper.RoundedButtonDelegate;
import static com.slaterama.fab2.widget.roundedbutton.RoundedButtonHelper.RoundedButtonImpl;
import static com.slaterama.fab2.widget.roundedbutton.RoundedButtonHelper.RoundedButtonOptions;

@TargetApi(Build.VERSION_CODES.ECLAIR_MR1)
public class RoundedButtonImplEclairMr1
		implements RoundedButtonImpl {

	RoundedButtonDelegate mDelegate;
	View mView;
	ColorStateList mColor;
	Rect mContentPadding;
	float mCornerRadius;
	float mElevation;
	Rect mInsetPadding;
	float mMaxElevation;
	float mPressedTranslationZ;
	boolean mPreventCornerOverlap;
	float mTranslationZ;
	boolean mUseCompatAnimation;
	boolean mUseCompatPadding;

	BackgroundDrawable mBackgroundDrawable;

	public RoundedButtonImplEclairMr1(RoundedButtonDelegate delegate,
	                                  RoundedButtonOptions options) {
		mDelegate = delegate;
		try {
			mView = (View) delegate;
		} catch (ClassCastException e) {
			throw new ClassCastException("delegate must be of type View");
		}
		mColor = options.color;
		mContentPadding = options.contentPadding;
		mCornerRadius = options.cornerRadius;
		mElevation = options.elevation;
		mInsetPadding = options.insetPadding;
		mMaxElevation = options.maxElevation;
		mPressedTranslationZ = options.pressedTranslationZ;
		mPreventCornerOverlap = options.preventCornerOverlap;
		mTranslationZ = options.translationZ;
		mUseCompatAnimation = options.useCompatAnimation;
		/*
		// TODO Set up animations
		if (mUseCompatAnimation) {
			mSavedStateListAnimator = mView.getStateListAnimator();
			mView.setStateListAnimator(newStateListAnimator(mView, mPressedTranslationZ));
		}
		*/
		mUseCompatPadding = options.useCompatPadding;
		mBackgroundDrawable = new BackgroundDrawable();
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
			mView.setBackground(mBackgroundDrawable);
		} else {
			mView.setBackgroundDrawable(mBackgroundDrawable);
		}
		invalidatePadding();
	}

	@Override
	public ColorStateList getColor() {
		return null;
	}

	@Override
	public void setColor(ColorStateList color) {

	}

	@Override
	public int getContentPaddingLeft() {
		return 0;
	}

	@Override
	public int getContentPaddingTop() {
		return 0;
	}

	@Override
	public int getContentPaddingRight() {
		return 0;
	}

	@Override
	public int getContentPaddingBottom() {
		return 0;
	}

	@Override
	public void setContentPadding(int paddingLeft, int paddingTop, int paddingRight, int paddingBottom) {

	}

	@Override
	public float getCornerRadius() {
		return 0;
	}

	@Override
	public void setCornerRadius(float cornerRadius) {

	}

	@Override
	public int getInsetPaddingLeft() {
		return 0;
	}

	@Override
	public int getInsetPaddingTop() {
		return 0;
	}

	@Override
	public int getInsetPaddingRight() {
		return 0;
	}

	@Override
	public int getInsetPaddingBottom() {
		return 0;
	}

	@Override
	public void setInsetPadding(int paddingLeft, int paddingTop, int paddingRight, int paddingBottom) {

	}

	@Override
	public float getMaxElevation() {
		return 0;
	}

	@Override
	public void setMaxElevation(float maxElevation) {

	}

	@Override
	public boolean isPreventCornerOverlap() {
		return false;
	}

	@Override
	public void setPreventCornerOverlap(boolean preventCornerOverlap) {

	}

	@Override
	public float getSupportElevation() {
		return 0;
	}

	@Override
	public void setSupportElevation(float elevation) {

	}

	@Override
	public float getSupportTranslationZ() {
		return 0;
	}

	@Override
	public void setSupportTranslationZ(float translationZ) {

	}

	@Override
	public boolean isUseCompatAnimation() {
		return false;
	}

	@Override
	public void setUseCompatAnimation(boolean useCompatAnimation) {

	}

	@Override
	public boolean isUseCompatPadding() {
		return false;
	}

	@Override
	public void setUseCompatPadding(boolean useCompatPadding) {

	}

	void invalidatePadding() {

	}

	class BackgroundDrawable extends Drawable {
		@Override
		public void setAlpha(int alpha) {

		}

		@Override
		public void setColorFilter(ColorFilter cf) {

		}

		@Override
		public int getOpacity() {
			return 0;
		}

		@Override
		public void draw(Canvas canvas) {

		}
	}
}
