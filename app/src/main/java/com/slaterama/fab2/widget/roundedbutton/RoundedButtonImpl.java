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

import static com.slaterama.fab2.widget.roundedbutton.RoundedButtonHelper.COS_45;
import static com.slaterama.fab2.widget.roundedbutton.RoundedButtonHelper.RoundedButtonBase;
import static com.slaterama.fab2.widget.roundedbutton.RoundedButtonHelper.RoundedButtonDelegate;
import static com.slaterama.fab2.widget.roundedbutton.RoundedButtonHelper.RoundedButtonOptions;
import static com.slaterama.fab2.widget.roundedbutton.RoundedButtonHelper.SHADOW_MULTIPLIER;

public abstract class RoundedButtonImpl implements RoundedButtonBase {

	static RoundRectHelper sRoundRectHelper;

	static {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
			sRoundRectHelper = new RoundRectHelperJellybeanMr1();
		} else {
			sRoundRectHelper = new RoundRectHelperEclairMr1();
		}
	}

	RoundedButtonDelegate mDelegate;
	View mView;
	ColorStateList mColor;
	Rect mContentPadding;
	float mCornerRadius;
	Rect mInsetPadding;
	float mMaxElevation;
	float mPressedTranslationZ;
	boolean mPreventCornerOverlap;
	boolean mUseCompatAnimation;
	boolean mUseCompatPadding;

	BackgroundDrawableBase mBackgroundDrawable;

	public RoundedButtonImpl(RoundedButtonDelegate delegate, RoundedButtonOptions options) {
		mDelegate = delegate;
		try {
			mView = (View) delegate;
		} catch (ClassCastException e) {
			throw new ClassCastException("delegate must be of type View");
		}
		mColor = options.color;
		mContentPadding = options.contentPadding;
		mCornerRadius = options.cornerRadius;
		mInsetPadding = options.insetPadding;
		mMaxElevation = options.maxElevation;
		mPressedTranslationZ = options.pressedTranslationZ;
		mPreventCornerOverlap = options.preventCornerOverlap;
		mUseCompatAnimation = options.useCompatAnimation;
		mUseCompatPadding = options.useCompatPadding;
		mBackgroundDrawable = createBackgroundDrawable();
		Drawable drawable = (willWrapBackgroundDrawable()
				? wrapBackgroundDrawable(mBackgroundDrawable) : mBackgroundDrawable);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
			mView.setBackground(drawable);
		} else {
			mView.setBackgroundDrawable(drawable);
		}
		invalidatePadding();
	}

	abstract BackgroundDrawableBase createBackgroundDrawable();

	abstract boolean willWrapBackgroundDrawable();

	Drawable wrapBackgroundDrawable(BackgroundDrawableBase drawable) {
		return drawable;
	}

	@Override
	public ColorStateList getColor() {
		return mColor;
	}

	@Override
	public void setColor(ColorStateList color) {
		mColor = color;
		mBackgroundDrawable.invalidateSelf();
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
			invalidatePadding();
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
			mBackgroundDrawable.invalidateSelf();
			if (mPreventCornerOverlap) {
				invalidatePadding();
			}
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
			mBackgroundDrawable.updateBounds(null);
			mBackgroundDrawable.invalidateSelf();
			invalidatePadding();
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
			if (shouldUseCompatPadding()) {
				mBackgroundDrawable.updateBounds(null);
				mBackgroundDrawable.invalidateSelf();
				invalidatePadding();
			}
		}
	}

	@Override
	public boolean isPreventCornerOverlap() {
		return false;
	}

	@Override
	public void setPreventCornerOverlap(boolean preventCornerOverlap) {
		if (preventCornerOverlap != mPreventCornerOverlap) {
			mPreventCornerOverlap = preventCornerOverlap;
			invalidatePadding();
		}
	}

	@Override
	public boolean isUseCompatAnimation() {
		return mUseCompatAnimation;
	}

	protected boolean shouldUseCompatAnimation() {
		return mUseCompatAnimation;
	}

	@Override
	public void setUseCompatAnimation(boolean useCompatAnimation) {
		if (useCompatAnimation != mUseCompatAnimation) {
			mUseCompatAnimation = useCompatAnimation;
			onUseCompatAnimationChange(mUseCompatAnimation);
		}
	}

	protected void onUseCompatAnimationChange(boolean useCompatAnimation) {
	}

	@Override
	public boolean isUseCompatPadding() {
		return mUseCompatPadding;
	}

	protected boolean shouldUseCompatPadding() {
		return mUseCompatPadding;
	}

	@Override
	public void setUseCompatPadding(boolean useCompatPadding) {
		if (useCompatPadding != mUseCompatPadding) {
			mUseCompatPadding = useCompatPadding;
			onUseCompatPaddingChange(mUseCompatPadding);
		}
	}

	protected void onUseCompatPaddingChange(boolean useCompatPadding) {
	}

	void invalidatePadding() {
		int left = mInsetPadding.left;
		int top = mInsetPadding.top;
		int right = mInsetPadding.right;
		int bottom = mInsetPadding.bottom;
		if (shouldUseCompatPadding()) {
			float verticalPadding = mMaxElevation * SHADOW_MULTIPLIER;
			left += mMaxElevation;
			top += verticalPadding;
			right += mMaxElevation;
			bottom += verticalPadding;
		}
		int overlay = (mPreventCornerOverlap ? (int) Math.ceil((1 - COS_45) * mCornerRadius) : 0);
		mDelegate.onPaddingChanged(left, top, right, bottom, overlay);
	}

	public abstract class BackgroundDrawableBase extends Drawable {
		final Paint mPaint;

		public BackgroundDrawableBase() {
			super();
			mPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
		}

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

		@Override
		public boolean isStateful() {
			return mColor.isStateful();
		}

		@Override
		protected void onBoundsChange(Rect bounds) {
			super.onBoundsChange(bounds);
			updateBounds(bounds);
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

		public void updateBounds(Rect bounds) {

		}
	}

	interface RoundRectHelper {
		void drawRoundRect(Canvas canvas, RectF bounds, float rx, float ry, Paint paint);
	}

	static class RoundRectHelperEclairMr1 implements RoundRectHelper {
		private final RectF mCornerRect = new RectF();

		@Override
		public void drawRoundRect(Canvas canvas, RectF rect, float rx, float ry, Paint paint) {
			// Draws a round rect using 7 draw operations. This is faster than using
			// canvas.drawRoundRect before JBMR1 because API 11-16 used alpha mask textures to draw
			// shapes.
			if (rect != null) {
				final float twoRx = rx * 2;
				final float twoRy = ry * 2;
				final float innerWidth = rect.width() - twoRx;
				final float innerHeight = rect.height() - twoRy;
				mCornerRect.set(rect.left, rect.top, rect.left + twoRx, rect.top + twoRy);

				canvas.drawArc(mCornerRect, 180, 90, true, paint);
				mCornerRect.offset(innerWidth, 0);
				canvas.drawArc(mCornerRect, 270, 90, true, paint);
				mCornerRect.offset(0, innerHeight);
				canvas.drawArc(mCornerRect, 0, 90, true, paint);
				mCornerRect.offset(-innerWidth, 0);
				canvas.drawArc(mCornerRect, 90, 90, true, paint);

				//draw top and bottom pieces
				canvas.drawRect(rect.left + rx, rect.top, rect.right - rx, rect.top + ry, paint);
				canvas.drawRect(rect.left + rx, rect.bottom - ry, rect.right - rx, rect.bottom,
						paint);

				//center
				canvas.drawRect(rect.left, (float) Math.floor(rect.top + ry), rect.right,
						(float) Math.ceil(rect.bottom - ry), paint);
			}
		}
	}

	static class RoundRectHelperJellybeanMr1 implements RoundRectHelper {
		@Override
		public void drawRoundRect(Canvas canvas, RectF bounds, float rx, float ry, Paint paint) {
			canvas.drawRoundRect(bounds, rx, ry, paint);
		}
	}
}
