package com.slaterama.fab2.widget.roundedbutton;

import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;
import android.util.StateSet;
import android.view.View;

import static android.os.Build.VERSION_CODES.*;

public class RoundedButtonDrawable extends Drawable {

	static final String TAG = "RoundedButtonDrawable";

	static final float SHADOW_MULTIPLIER = 1.5f;

	// used to calculate overlap padding
	static final double COS_45 = Math.cos(Math.toRadians(45));

	static int[] PRESSED_SPECS = new int[]{android.R.attr.state_pressed,
			android.R.attr.state_enabled};
	static int[] BASE_SPECS = new int[]{android.R.attr.state_enabled};
	static int[] DISABLED_SPECS = new int[]{-android.R.attr.state_enabled};

	static final RoundRectHelper sRoundRectHelper = newRoundRectHelper();

	static boolean isApi(int versionCode) {
		return Build.VERSION.SDK_INT >= versionCode;
	}

	static boolean isStateEnabled(int[] stateSet) {
		return StateSet.stateSetMatches(DISABLED_SPECS, stateSet);
	}

	static ShadowHelper newShadowHelper() {
		if (isApi(LOLLIPOP)) {
			return new ShadowHelperLollipop();
		} else if (isApi(ECLAIR_MR1)) {
			return new ShadowHelperEclairMr1();
		} else {
			throw new IllegalStateException(String.format("%s requires API %d or later",
					TAG, Build.VERSION_CODES.ECLAIR_MR1));
		}
	}

	static RoundRectHelper newRoundRectHelper() {
		if (isApi(JELLY_BEAN_MR1)) {
			return new RoundRectHelperJellybeanMr1();
		} else if (isApi(ECLAIR_MR1)) {
			return new RoundRectHelperEclairMr1();
		} else {
			throw new IllegalStateException(String.format("%s requires API %d or later",
					TAG, Build.VERSION_CODES.ECLAIR_MR1));
		}
	}

	View mView;
	OnPaddingChangeListener mOnPaddingChangeListener;
	ColorStateList mColor;
	final Rect mContentPadding;
	float mCornerRadius;
	int mDiameter;
	float mElevation;
	float mEnabledElevation;
	final Rect mInsetPadding;
	float mMaxElevation;
	float mPressedTranslationZ;
	boolean mPreventCornerOverlap;
	float mTranslationZ;
	boolean mUseCompatAnimation;
	boolean mUseCompatPadding;

	final Point mShadowPadding = new Point(); // TODO Move to helper?
	final ShadowHelper mShadowHelper = newShadowHelper();

	final Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
	final Rect mBounds = new Rect();
	final RectF mBoundsF = new RectF();

	public RoundedButtonDrawable(View view, RoundedButtonAttributes attributes) {
		super();
		mView = view;
		try {
			mOnPaddingChangeListener = (OnPaddingChangeListener) view;
		} catch (ClassCastException e) {
			Log.w(TAG, "view passed does not implement OnPaddingChangeListener");
		}
		mColor = attributes.color;
		mContentPadding = new Rect(attributes.contentPadding);
		mCornerRadius = attributes.cornerRadius;
		mDiameter = Math.round(attributes.cornerRadius * 2);
		mElevation = attributes.elevation;
		mEnabledElevation = mElevation;
		mInsetPadding = new Rect(attributes.insetPadding);
		mMaxElevation = attributes.maxElevation;
		mPressedTranslationZ = attributes.pressedTranslationZ;
		mPreventCornerOverlap = attributes.preventCornerOverlap;
		mTranslationZ = attributes.translationZ;
		mUseCompatAnimation = attributes.useCompatAnimation;
		mUseCompatPadding = attributes.useCompatPadding;

//		mRoundedButtonDrawable = newRoundedButtonDrawable();
//		setSupportBackground(mRoundedButtonDrawable);
//		invalidatePadding();
	}

	public RoundedButtonDrawable setOnPaddingChangeListener(OnPaddingChangeListener listener) {
		mOnPaddingChangeListener = listener;
		return this;
	}

	@Override
	public void setAlpha(int alpha) {

	}

	@Override
	public void setColorFilter(ColorFilter cf) {

	}

	@Override
	public int getMinimumHeight() {
		return mDiameter + mInsetPadding.top + mInsetPadding.bottom + mShadowPadding.y * 2;
	}

	@Override
	public int getMinimumWidth() {
		return mDiameter + mInsetPadding.left + mInsetPadding.right + mShadowPadding.x * 2;
	}

	@Override
	public int getOpacity() {
		return PixelFormat.OPAQUE;
	}

	public ColorStateList getColor() {
		return mColor;
	}

	public void setColor(ColorStateList color) {
		mColor = color;
	}

	public float getCornerRadius() {
		return mCornerRadius;
	}

	public Rect getContentPadding() {
		return mContentPadding;
	}

	public void setContentPadding(Rect contentPadding) {
		if (contentPadding.left != mContentPadding.left || contentPadding.top != mContentPadding.top
				|| contentPadding.right != mContentPadding.right
				|| contentPadding.bottom != mContentPadding.bottom) {
			mContentPadding.set(contentPadding);
			invalidatePadding();
		}
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
		if (isStateEnabled(getState())) {
			mEnabledElevation = elevation;
		}
		if (elevation != mElevation) {
			mElevation = elevation;
//			onElevationChanged(elevation);
		}
	}

	public Rect getInsetPadding() {
		return mInsetPadding;
	}

	public void setInsetPadding(Rect insetPadding) {
		if (insetPadding.left != mInsetPadding.left || insetPadding.top != mInsetPadding.top
				|| insetPadding.right != mInsetPadding.right
				|| insetPadding.bottom != mInsetPadding.bottom) {
			mInsetPadding.set(insetPadding);
			invalidateBounds();
			invalidatePadding();
		}
	}

	public float getMaxElevation() {
		return mMaxElevation;
	}

	public void setMaxElevation(float maxElevation) {
		if (maxElevation != mMaxElevation) {
			mMaxElevation = maxElevation;
			if (mUseCompatPadding || !isApi(LOLLIPOP)) {
				invalidateBounds();
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
//			onTranslationZChanged(translationZ);
		}
	}

	public boolean isUseCompatAnimation() {
		return mUseCompatAnimation;
	}

	public void setUseCompatAnimation(boolean useCompatAnimation) {
		if (useCompatAnimation != mUseCompatAnimation) {
			mUseCompatAnimation = useCompatAnimation;
//			onUseCompatAnimationChanged(useCompatAnimation);
		}
	}

	public boolean isUseCompatPadding() {
		return mUseCompatPadding;
	}

	public void setUseCompatPadding(boolean useCompatPadding) {
		if (useCompatPadding != mUseCompatPadding) {
			mUseCompatPadding = useCompatPadding;
//			onUseCompatPaddingChanged(useCompatPadding);
		}
	}

	@Override
	public void draw(Canvas canvas) {
		mShadowHelper.drawShadow(canvas); // TODO Other parameters
		sRoundRectHelper.drawRoundRect(canvas, mBoundsF, mCornerRadius, mCornerRadius, mPaint);
	}

	void invalidateBounds(Rect bounds, boolean invalidate) {
		mBounds.set(bounds);
		mBounds.left += mInsetPadding.left;
		mBounds.top += mInsetPadding.top;
		mBounds.right -= mInsetPadding.right;
		mBounds.bottom -= mInsetPadding.bottom;
		mBoundsF.set(mBounds);
		if (mUseCompatPadding || !isApi(LOLLIPOP)) {
			mBounds.inset(Math.round(mMaxElevation),
					Math.round(mMaxElevation * SHADOW_MULTIPLIER));
			mBoundsF.set(mBounds);
		}
		if (invalidate) {
			invalidateSelf();
		}
	}

	void invalidateBounds(Rect bounds) {
		invalidateBounds(bounds, false);
	}

	void invalidateBounds() {
		invalidateBounds(getBounds(), true);
	}

	void invalidatePadding() {
		final int cornerOverlapPadding = (mPreventCornerOverlap ?
				(int) Math.ceil((1 - COS_45) * mCornerRadius) : 0);
		final int horizontalShadowPadding;
		final int verticalShadowPadding;
		if (mUseCompatPadding || !isApi(LOLLIPOP)) {
			mShadowPadding.set(Math.round(mMaxElevation),
					Math.round(mMaxElevation * SHADOW_MULTIPLIER));
			horizontalShadowPadding = mShadowPadding.x * 2;
			verticalShadowPadding = mShadowPadding.y * 2;
		} else {
			mShadowPadding.set(0, 0);
			horizontalShadowPadding = 0;
			verticalShadowPadding = 0;
		}
		int left = mInsetPadding.left + mShadowPadding.x + cornerOverlapPadding
				+ mContentPadding.left;
		int top = mInsetPadding.top + mShadowPadding.y + cornerOverlapPadding
				+ mContentPadding.top;
		int right = mInsetPadding.right + mShadowPadding.x + cornerOverlapPadding
				+ mContentPadding.right;
		int bottom = mInsetPadding.bottom + mShadowPadding.y + cornerOverlapPadding
				+ mContentPadding.bottom;
		mOnPaddingChangeListener.onPaddingChange(left, top, right, bottom, horizontalShadowPadding,
				verticalShadowPadding);
//		mView.requestLayout();
	}

	static class RoundedButtonAttributes {
		ColorStateList color = null;
		final Rect contentPadding = new Rect();
		float cornerRadius = 0f;
		float elevation = 0f;
		final Rect insetPadding = new Rect();
		float maxElevation = 0f;
		float pressedTranslationZ = 0f;
		boolean preventCornerOverlap = false;
		float translationZ = 0f;
		boolean useCompatAnimation = false;
		boolean useCompatPadding = false;
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

	interface RoundRectHelper {
		void drawRoundRect(Canvas canvas, RectF rect, float rx, float ry, Paint paint);
	}

	static class RoundRectHelperEclairMr1 implements RoundRectHelper {
		@Override
		public void drawRoundRect(Canvas canvas, RectF rect, float rx, float ry, Paint paint) {
			// TODO Lots of code to copy over
		}
	}

	static class RoundRectHelperJellybeanMr1 implements RoundRectHelper {
		@Override
		public void drawRoundRect(Canvas canvas, RectF rect, float rx, float ry, Paint paint) {
			canvas.drawRoundRect(rect, rx, ry, paint);
		}
	}

	interface ShadowHelper {
		void drawShadow(Canvas canvas /*, other args */);
	}

	static class ShadowHelperEclairMr1 implements ShadowHelper {
		@Override
		public void drawShadow(Canvas canvas) {
			// TODO Lots of shadow code
		}
	}

	static class ShadowHelperLollipop implements ShadowHelper {
		@Override
		public void drawShadow(Canvas canvas) {
			// NO OP
		}
	}
}
