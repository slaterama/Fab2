package com.slaterama.fab2.widget.roundedbutton;

import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;

public abstract class RoundedButtonImpl {

	static final float SHADOW_MULTIPLIER = 1.5f;

	static RoundedButtonImpl newInstance(View view, RoundedButtonOptions options) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			return new RoundedButtonImplLollipop(view, options);
		} else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
			return new RoundedButtonImplJellybeanMr1(view, options);
		} else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
			return new RoundedButtonImplJellybean(view, options);
		} else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			return new RoundedButtonImplHoneycomb(view, options);
		} else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR_MR1) {
			return new RoundedButtonImplEclairMr1(view, options);
		} else {
			throw new IllegalStateException("RoundedButton requires at API 7 or above");
		}
	}

	View mView;
	RoundedButtonDelegate mDelegate;
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

	RoundedButtonDrawable mRoundedButtonDrawable;

	public RoundedButtonImpl(View view, RoundedButtonOptions options) {
		super();
		mView = view;
		try {
			mDelegate = (RoundedButtonDelegate) view;
		} catch (ClassCastException e) {
			throw new ClassCastException("view must implement RoundedButtonDelegate");
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
		mUseCompatPadding = options.useCompatPadding;

		mRoundedButtonDrawable = newRoundedButtonDrawable();
		setSupportBackground(mRoundedButtonDrawable);
	}

	abstract RoundedButtonDrawable newRoundedButtonDrawable();

	abstract void setSupportBackground(Drawable background);

	public ColorStateList getColor() {
		return mColor;
	}

	public void setColor(ColorStateList color) {
		mColor = color;
	}

	public Rect getContentPadding() {
		return mContentPadding;
	}

	public void setContentPadding(Rect contentPadding) {
		mContentPadding = contentPadding;
	}

	public float getCornerRadius() {
		return mCornerRadius;
	}

	public void setCornerRadius(float cornerRadius) {
		mCornerRadius = cornerRadius;
	}

	public float getElevation() {
		return mElevation;
	}

	public void setElevation(float elevation) {
		mElevation = elevation;
	}

	public Rect getInsetPadding() {
		return mInsetPadding;
	}

	public void setInsetPadding(Rect insetPadding) {
		if (insetPadding.left != mInsetPadding.left || insetPadding.top != mInsetPadding.top
				|| insetPadding.right != mInsetPadding.right
				|| insetPadding.bottom != mInsetPadding.bottom) {
			mInsetPadding.set(insetPadding);
			mRoundedButtonDrawable.invalidateBounds();
			invalidatePadding();
		}
	}

	public float getMaxElevation() {
		return mMaxElevation;
	}

	public void setMaxElevation(float maxElevation) {
		mMaxElevation = maxElevation;
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
		mPreventCornerOverlap = preventCornerOverlap;
	}

	public float getTranslationZ() {
		return mTranslationZ;
	}

	public void setTranslationZ(float translationZ) {
		mTranslationZ = translationZ;
	}

	public boolean isUseCompatAnimation() {
		return mUseCompatAnimation;
	}

	public void setUseCompatAnimation(boolean useCompatAnimation) {
		mUseCompatAnimation = useCompatAnimation;
	}

	public boolean isUseCompatPadding() {
		return mUseCompatPadding;
	}

	public void setUseCompatPadding(boolean useCompatPadding) {
		mUseCompatPadding = useCompatPadding;
	}

	abstract class RoundedButtonDrawable extends Drawable {
		final Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
		final Rect mBounds = new Rect();
		final RectF mBoundsF = new RectF();

		public RoundedButtonDrawable() {
			super();
		}

		@Override
		public void setAlpha(int alpha) {
			mPaint.setAlpha(alpha);
		}

		@Override
		public void setColorFilter(ColorFilter cf) {
			mPaint.setColorFilter(cf);
		}

		@Override
		public int getOpacity() {
			return PixelFormat.OPAQUE;
		}

		@Override
		public boolean isStateful() {
			return mColor.isStateful();
		}

		@Override
		protected void onBoundsChange(Rect bounds) {
			super.onBoundsChange(bounds);
			invalidateBounds(bounds);
		}

		@Override
		protected boolean onStateChange(int[] state) {
			int color = mColor.getColorForState(state, mColor.getDefaultColor());
			if (color != mPaint.getColor()) {
				mPaint.setColor(color);
				invalidateSelf();
				return true;
			} else {
				return super.onStateChange(state);
			}
		}

		@Override
		public void draw(Canvas canvas) {
			canvas.drawRect(mBounds, mPaint);
		}

		void invalidateBounds(Rect bounds, boolean invalidate) {
			mBounds.set(bounds.left + mInsetPadding.left, bounds.top + mInsetPadding.top,
					bounds.right - mInsetPadding.right, bounds.bottom - mInsetPadding.bottom);
			mBoundsF.set(mBounds);
			if (mUseCompatPadding) {
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
	}

	void invalidatePadding() {

	}

	static class RoundedButtonOptions {
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

	interface RoundedButtonDelegate {
		void onPaddingChanged(int left, int top, int right, int bottom);
	}
}
