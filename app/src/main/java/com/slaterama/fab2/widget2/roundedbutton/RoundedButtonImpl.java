package com.slaterama.fab2.widget2.roundedbutton;

import android.annotation.TargetApi;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;

public class RoundedButtonImpl extends Drawable {

	final static BackgroundHelper sBackgroundHelper = newBackgroundHelper();

	static ImplHelper newImplHelper() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			return new ImplHelperLollipop();
		} else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR_MR1) {
			return new ImplHelperEclairMr1();
		} else {
			throw new IllegalStateException("RoundedButton requires api 7 or higher");
		}
	}

	static BackgroundHelper newBackgroundHelper() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
			return new BackgroundHelperJellyBean();
		} else {
			return new BackgroundHelperBase();
		}
	}

	static void setViewBackground(View view, Drawable background) {
		sBackgroundHelper.setBackground(view, background);
	}

	View mView;
	OnPaddingChangeListener mOnPaddingChangeListener;
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

	private float mDiameter;
	private float mEnabledElevation; // TODO Might not need here

	final ImplHelper mImplHelper = newImplHelper();

	public RoundedButtonImpl(View view) {
		super();
		mView = view;
		try {
			mOnPaddingChangeListener = (OnPaddingChangeListener) view;
		} catch (ClassCastException e) {
			throw new ClassCastException("view must implement OnPaddingChangeListener");
		}
	}

	// Overridden getters/setters

	@Override
	public void setAlpha(int alpha) {

	}

	@Override
	public void setColorFilter(ColorFilter cf) {

	}

	@Override
	public int getOpacity() {
		return PixelFormat.OPAQUE;
	}

	// Getters & setters

	public ColorStateList getColor() {
		return mColor;
	}

	public void setColor(ColorStateList color) {
		mColor = color;
		invalidateSelf();
	}

	public Rect getContentPadding() {
		return mContentPadding;
	}

	public void setContentPadding(Rect contentPadding) {
		if (contentPadding.left != mContentPadding.left
				|| contentPadding.top != mContentPadding.top
				|| contentPadding.right != mContentPadding.right
				|| contentPadding.bottom != mContentPadding.bottom) {
			mContentPadding.set(contentPadding);
			invalidatePadding();
		}
	}

	public float getCornerRadius() {
		return mCornerRadius;
	}

	public void setCornerRadius(float cornerRadius) {
		if (cornerRadius != mCornerRadius) {
			mCornerRadius = cornerRadius;
			mDiameter = Math.round(cornerRadius * 2);
			if (mPreventCornerOverlap) {
				invalidatePadding();
			}
		}
	}

	public float getElevation() {
		return mElevation;
	}

	public void setElevation(float elevation) {
		if (elevation != mElevation) {
			mElevation = elevation;
			mImplHelper.setElevation(mView, elevation);
		}
	}

	public Rect getInsetPadding() {
		return mInsetPadding;
	}

	public void setInsetPadding(Rect insetPadding) {
		if (insetPadding.left != mInsetPadding.left
				|| insetPadding.top != mInsetPadding.top
				|| insetPadding.right != mInsetPadding.right
				|| insetPadding.bottom != mInsetPadding.bottom) {
			mInsetPadding.set(insetPadding);
			/*mRoundedButtonDrawable.*/ invalidateBounds();
			invalidatePadding();
		}
	}

	public float getMaxElevation() {
		return mMaxElevation;
	}

	public void setMaxElevation(float maxElevation) {
		if (maxElevation != mMaxElevation) {
			mMaxElevation = maxElevation;
			if (shouldUseCompatPadding()) {
				/*mRoundedButtonDrawable.*/ invalidateBounds();
				invalidatePadding();
			}
		}
	}

	public float getPressedTranslationZ() {
		return mPressedTranslationZ;
	}

	public void setPressedTranslationZ(float pressedTranslationZ) {
		mPressedTranslationZ = pressedTranslationZ;
	}

	public boolean isPreventCornerOverlap() {
		return mPreventCornerOverlap;
	}

	public void setPreventCornerOverlap(boolean preventCornerOverlap) {
		if (preventCornerOverlap != mPreventCornerOverlap) {
			mPreventCornerOverlap = preventCornerOverlap;
			invalidatePadding();
		}
	}

	public float getTranslationZ() {
		return mTranslationZ;
	}

	public void setTranslationZ(float translationZ) {
		if (translationZ != mTranslationZ) {
			mTranslationZ = translationZ;
			mImplHelper.setTranslationZ(mView, translationZ);
		}
	}

	public boolean isUseCompatAnimation() {
		return mUseCompatAnimation;
	}

	public void setUseCompatAnimation(boolean useCompatAnimation) {
		if (useCompatAnimation != mUseCompatAnimation) {
			mUseCompatAnimation = useCompatAnimation;
			mImplHelper.setUseCompatAnimation(useCompatAnimation);
		}
	}

	public boolean isUseCompatPadding() {
		return mUseCompatPadding;
	}

	boolean shouldUseCompatPadding() {
		return Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP || mUseCompatPadding;
	}

	public void setUseCompatPadding(boolean useCompatPadding) {
		if (useCompatPadding != mUseCompatPadding) {
			mUseCompatPadding = useCompatPadding;
			mImplHelper.setUseCompatPadding(useCompatPadding);
		}
	}

	// Overridden methods

	@Override
	public void draw(Canvas canvas) {

	}

	// Methods

	void invalidateBounds(Rect bounds, boolean invalidate) {
		// TODO
	}

	void invalidateBounds(Rect bounds) {
		invalidateBounds(bounds, false);
	}

	void invalidateBounds() {
		invalidateBounds(getBounds(), true);
	}

	void invalidatePadding() {
		// TODO
	}

	interface ImplHelper {
		void setElevation(View view, float elevation);

		void setTranslationZ(View view, float translationZ);

		void setUseCompatAnimation(boolean useCompatAnimation);

		void setUseCompatPadding(boolean useCompatPadding);
	}

	@TargetApi(Build.VERSION_CODES.ECLAIR_MR1)
	static class ImplHelperEclairMr1 implements ImplHelper { // TODO Probably not static OR these methods require more arguments
		@Override
		public void setElevation(View view, float elevation) {
			// TODO deal with "mEnabledElevation" etc. Will this require a non-static class?
		}

		@Override
		public void setTranslationZ(View view, float translationZ) {
			// TODO draw or whatever
		}

		@Override
		public void setUseCompatAnimation(boolean useCompatAnimation) {
			// NO OP
		}

		@Override
		public void setUseCompatPadding(boolean useCompatPadding) {
			// NO OP
		}
	}

	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	static class ImplHelperLollipop implements ImplHelper {
		@Override
		public void setElevation(View view, float elevation) {
			view.setElevation(elevation);
		}

		@Override
		public void setTranslationZ(View view, float translationZ) {
			view.setTranslationZ(translationZ);
		}

		@Override
		public void setUseCompatAnimation(boolean useCompatAnimation) {
			// TODO
		}

		@Override
		public void setUseCompatPadding(boolean useCompatPadding) {
			// TODO
		}
	}

	interface BackgroundHelper {
		void setBackground(View view, Drawable background);
	}

	@TargetApi(Build.VERSION_CODES.BASE)
	static class BackgroundHelperBase implements BackgroundHelper {
		@Override
		public void setBackground(View view, Drawable background) {
			view.setBackgroundDrawable(background);
		}
	}

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	static class BackgroundHelperJellyBean implements BackgroundHelper {
		@Override
		public void setBackground(View view, Drawable background) {
			view.setBackground(background);
		}
	}

	interface OnPaddingChangeListener {
		/*
		float getSupportElevation();
		void setSupportElevation(float elevation);
		float getSupportTranslationZ();
		void setSupportTranslationZ(float translationZ);
		*/
		void onPaddingChange(int left, int top, int right, int bottom,
		                     int horizontalShadowPadding, int verticalShadowPadding);
	}

}
