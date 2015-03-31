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
import android.view.View;

@SuppressWarnings("unused")
public abstract class RoundedButtonImpl {

	static final float SHADOW_MULTIPLIER = 1.5f;

	// used to calculate overlap padding
	static final double COS_45 = Math.cos(Math.toRadians(45));

	static RoundedButtonImpl newInstance(View view, RoundedButtonAttributes attributes) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			return new RoundedButtonImplLollipop(view, attributes);
		} else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
			return new RoundedButtonImplJellybeanMr1(view, attributes);
		} else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
			return new RoundedButtonImplJellybean(view, attributes);
		} else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			return new RoundedButtonImplHoneycomb(view, attributes);
		} else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR_MR1) {
			return new RoundedButtonImplEclairMr1(view, attributes);
		} else {
			throw new IllegalStateException("RoundedButton requires at API 7 or above");
		}
	}

	View mView;
	RoundedButtonDelegate mDelegate;
	ColorStateList mColor;
	final Rect mContentPadding;
	float mCornerRadius;
	int mDiameter;
	float mElevation;
	final Rect mInsetPadding;
	float mMaxElevation;
	float mPressedTranslationZ;
	boolean mPreventCornerOverlap;
	float mTranslationZ;
	boolean mUseCompatAnimation;
	boolean mUseCompatPadding;

	RoundedButtonDrawable mRoundedButtonDrawable;
	final Point mShadowPadding = new Point();

	public RoundedButtonImpl(View view, RoundedButtonAttributes attributes) {
		super();
		mView = view;
		try {
			mDelegate = (RoundedButtonDelegate) view;
		} catch (ClassCastException e) {
			throw new ClassCastException("view must implement RoundedButtonDelegate");
		}
		mColor = attributes.color;
		mContentPadding = new Rect(attributes.contentPadding);
		mCornerRadius = attributes.cornerRadius;
		mDiameter = Math.round(attributes.cornerRadius + 2);
		mElevation = attributes.elevation;
		mInsetPadding = new Rect(attributes.insetPadding);
		mMaxElevation = attributes.maxElevation;
		mPressedTranslationZ = attributes.pressedTranslationZ;
		mPreventCornerOverlap = attributes.preventCornerOverlap;
		mTranslationZ = attributes.translationZ;
		mUseCompatAnimation = attributes.useCompatAnimation;
		mUseCompatPadding = attributes.useCompatPadding;

		mRoundedButtonDrawable = newRoundedButtonDrawable();
		setSupportBackground(mRoundedButtonDrawable);
		invalidatePadding();
	}

	abstract RoundedButtonDrawable newRoundedButtonDrawable();

	abstract void setSupportBackground(Drawable background);

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
		if (maxElevation != mMaxElevation) {
			mMaxElevation = maxElevation;
			if (willUseCompatPadding()) {
				mRoundedButtonDrawable.invalidateBounds();
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
		mTranslationZ = translationZ;
	}

	public boolean isUseCompatAnimation() {
		return mUseCompatAnimation;
	}

	protected boolean willUseCompatAnimation() {
		return mUseCompatAnimation;
	}

	public void setUseCompatAnimation(boolean useCompatAnimation) {
		mUseCompatAnimation = useCompatAnimation;
	}

	public boolean isUseCompatPadding() {
		return mUseCompatPadding;
	}

	protected boolean willUseCompatPadding() {
		return mUseCompatPadding;
	}

	public void setUseCompatPadding(boolean useCompatPadding) {
		mUseCompatPadding = useCompatPadding;
	}

	void resolveSize(int widthMeasureSpec, int heightMeasureSpec, boolean useMeasuredSize,
	                 Point resolvedSize) {
		final int minWidth = mDiameter + mInsetPadding.left + mInsetPadding.right
				+ mShadowPadding.x * 2;
		final int minHeight = mDiameter + mInsetPadding.top + mInsetPadding.bottom
				+ mShadowPadding.y * 2;
		int requestedWidth = (useMeasuredSize
				? Math.max(minWidth, mView.getMeasuredWidth()) : minWidth);
		int requestedHeight = (useMeasuredSize
				? Math.max(minHeight, mView.getMeasuredHeight()) : minHeight);
		resolvedSize.set(View.resolveSize(requestedWidth, widthMeasureSpec),
				View.resolveSize(requestedHeight, heightMeasureSpec));
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
			mBounds.set(bounds);
			mBounds.left += mInsetPadding.left;
			mBounds.top += mInsetPadding.top;
			mBounds.right -= mInsetPadding.right;
			mBounds.bottom -= mInsetPadding.bottom;
			mBoundsF.set(mBounds);
			if (willUseCompatPadding()) {
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
		final int cornerOverlapPadding = (mPreventCornerOverlap ?
				(int) Math.ceil((1 - COS_45) * mCornerRadius) : 0);
		final int horizontalShadowPadding;
		final int verticalShadowPadding;
		if (willUseCompatPadding()) {
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
		mDelegate.onPaddingChanged(left, top, right, bottom, horizontalShadowPadding,
				verticalShadowPadding);
		mView.requestLayout();
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

	interface RoundedButtonDelegate {
		void onPaddingChanged(int left, int top, int right, int bottom,
		                      int horizontalShadowPadding, int verticalShadowPadding);
	}
}
