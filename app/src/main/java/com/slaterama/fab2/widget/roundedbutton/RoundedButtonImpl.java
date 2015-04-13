package com.slaterama.fab2.widget.roundedbutton;

import android.annotation.TargetApi;
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
import android.util.StateSet;
import android.view.View;

@SuppressWarnings("unused")
public abstract class RoundedButtonImpl {

	static final float SHADOW_MULTIPLIER = 1.5f;

	static int[] PRESSED_SPECS = new int[]{android.R.attr.state_pressed,
			android.R.attr.state_enabled};
	static int[] BASE_SPECS = new int[]{android.R.attr.state_enabled};

	static String ELEVATION_PROPERTY = "elevation";
	static String TRANSLATION_Z_PROPERTY = "translationZ";
	static String SUPPORT_ELEVATION_PROPERTY = "supportElevation";
	static String SUPPORT_TRANSLATION_Z_PROPERTY = "supportTranslationZ";

	// used to calculate overlap padding
	static final double COS_45 = Math.cos(Math.toRadians(45));

	static final BackgroundCompat sBackgroundCompat = (
			Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN
			? new BackgroundCompatJellyBean()
			: new BackgroundCompatBase());

	static final RoundRectCompat sRoundRectCompat = (
			Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1
			? new RoundRectCompatJellybeanMr1()
			: new RoundRectCompatBase());

	static RoundedButtonImpl newInstance(View view, RoundedButtonAttributes attributes) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			return new RoundedButtonImplLollipop(view, attributes);
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
	float mEnabledElevation;
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

		mRoundedButtonDrawable = newRoundedButtonDrawable();
		sBackgroundCompat.setBackground(view, getBackgroundDrawable(mRoundedButtonDrawable));
		invalidatePadding();
	}

	void initialize() {
		mRoundedButtonDrawable.initialize();
	}

	abstract RoundedButtonDrawable newRoundedButtonDrawable();

	Drawable getBackgroundDrawable(RoundedButtonDrawable roundedButtonDrawable) {
		return roundedButtonDrawable;
	}

	public ColorStateList getColor() {
		return mColor;
	}

	public void setColor(ColorStateList color) {
		mColor = color;
		// TODO ?
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
		if (mView.isEnabled()) {
			mEnabledElevation = elevation;
		}
		if (elevation != mElevation) {
			mElevation = elevation;
			onElevationChanged(elevation);
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
		if (translationZ != mTranslationZ) {
			mTranslationZ = translationZ;
			onTranslationZChanged(translationZ);
		}
	}

	public boolean isUseCompatAnimation() {
		return mUseCompatAnimation;
	}

	public void setUseCompatAnimation(boolean useCompatAnimation) {
		if (useCompatAnimation != mUseCompatAnimation) {
			mUseCompatAnimation = useCompatAnimation;
			onUseCompatAnimationChanged(useCompatAnimation);
		}
	}

	public boolean isUseCompatPadding() {
		return mUseCompatPadding;
	}

	public void setUseCompatPadding(boolean useCompatPadding) {
		if (useCompatPadding != mUseCompatPadding) {
			mUseCompatPadding = useCompatPadding;
			onUseCompatPaddingChanged(useCompatPadding);
		}
	}

	protected boolean willUseCompatAnimation() {
		return mUseCompatAnimation;
	}

	protected boolean willUseCompatPadding() {
		return mUseCompatPadding;
	}

	void onElevationChanged(float elevation) {
	}

	void onTranslationZChanged(float translationZ) {
	}

	void onUseCompatAnimationChanged(boolean useCompatAnimation) {
	}

	void onUseCompatPaddingChanged(boolean useCompatPadding) {
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
		boolean mInitialized;
		int[] mPendingState;
		int[] mCurrentSpecs;
		ButtonState mButtonState;

		final Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
		final Rect mBounds = new Rect();
		final RectF mBoundsF = new RectF();

		void initialize() {
			mInitialized = true;
			if (mPendingState != null) {
				onStateChange(mPendingState);
				mPendingState = null;
			}
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
			if (mInitialized) {
				ButtonState buttonState = ButtonState.fromState(state);
				if (!buttonState.equals(mButtonState)) {
					mButtonState = buttonState;
					onButtonStateChange(buttonState);
				}
				int color = mColor.getColorForState(state, mColor.getDefaultColor());
				if (color != mPaint.getColor()) {
					mPaint.setColor(color);
					invalidateSelf();
					return true;
				}
			} else {
				mPendingState = state;
			}

			return super.onStateChange(state);
		}

		void onButtonStateChange(ButtonState buttonState) {
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

		void invalidateShadow() {
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
		float getSupportElevation();
		void setSupportElevation(float elevation);
		float getSupportTranslationZ();
		void setSupportTranslationZ(float translationZ);
		void onPaddingChanged(int left, int top, int right, int bottom,
		                      int horizontalShadowPadding, int verticalShadowPadding);
	}

	interface BackgroundCompat {
		void setBackground(View view, Drawable background);
	}

	@TargetApi(Build.VERSION_CODES.BASE)
	static class BackgroundCompatBase implements BackgroundCompat {
		@Override
		public void setBackground(View view, Drawable background) {
			view.setBackgroundDrawable(background);
		}
	}

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	static class BackgroundCompatJellyBean implements BackgroundCompat {
		@Override
		public void setBackground(View view, Drawable background) {
			view.setBackground(background);
		}
	}

	interface RoundRectCompat {
		void drawRoundRect(Canvas canvas, RectF rect, float rx, float ry, Paint paint,
		                   RectF cornerRect);
	}

	@TargetApi(Build.VERSION_CODES.BASE)
	static class RoundRectCompatBase implements RoundRectCompat {
		@Override
		public void drawRoundRect(Canvas canvas, RectF rect, float rx, float ry, Paint paint,
		                          RectF cornerRect) {
			// Draws a round rect using 7 draw operations. This is faster than using
			// canvas.drawRoundRect before JBMR1 because API 11-16 used alpha mask textures to draw
			// shapes.
			if (rect != null) {
				final float twoRx = rx * 2;
				final float twoRy = ry * 2;
				final float innerWidth = rect.width() - twoRx;
				final float innerHeight = rect.height() - twoRy;
				cornerRect.set(rect.left, rect.top, rect.left + twoRx, rect.top + twoRy);

				canvas.drawArc(cornerRect, 180, 90, true, paint);
				cornerRect.offset(innerWidth, 0);
				canvas.drawArc(cornerRect, 270, 90, true, paint);
				cornerRect.offset(0, innerHeight);
				canvas.drawArc(cornerRect, 0, 90, true, paint);
				cornerRect.offset(-innerWidth, 0);
				canvas.drawArc(cornerRect, 90, 90, true, paint);

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

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
	static class RoundRectCompatJellybeanMr1 implements RoundRectCompat {
		@Override
		public void drawRoundRect(Canvas canvas, RectF rect, float rx, float ry, Paint paint,
		                          RectF cornerRect) {
			canvas.drawRoundRect(rect, rx, ry, paint);
		}
	}

	enum ButtonState {
		PRESSED(PRESSED_SPECS),
		DEFAULT(BASE_SPECS),
		WILD_CARD(StateSet.WILD_CARD);

		static ButtonState fromState(int[] state) {
			ButtonState[] values = values();
			for (ButtonState value : values) {
				if (StateSet.stateSetMatches(value.mState, state)) {
					return value;
				}
			}
			return WILD_CARD;
		}

		int[] mState;

		ButtonState(int[] state) {
			mState = state;
		}

		public int[] getState() {
			return mState;
		}
	}
}
