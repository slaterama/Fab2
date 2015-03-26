package com.slaterama.fab2.widget.roundedbutton;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.StateListAnimator;
import android.annotation.TargetApi;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.util.StateSet;
import android.util.TypedValue;
import android.view.View;

import com.slaterama.fab2.R;

import static android.os.Build.VERSION_CODES.LOLLIPOP;
import static com.slaterama.fab2.widget.roundedbutton.RoundedButtonHelper.BASE_SPECS;
import static com.slaterama.fab2.widget.roundedbutton.RoundedButtonHelper.COS_45;
import static com.slaterama.fab2.widget.roundedbutton.RoundedButtonHelper.ELEVATION_PROPERTY;
import static com.slaterama.fab2.widget.roundedbutton.RoundedButtonHelper.PRESSED_SPECS;
import static com.slaterama.fab2.widget.roundedbutton.RoundedButtonHelper.RoundedButtonDelegate;
import static com.slaterama.fab2.widget.roundedbutton.RoundedButtonHelper.RoundedButtonImpl;
import static com.slaterama.fab2.widget.roundedbutton.RoundedButtonHelper.RoundedButtonOptions;
import static com.slaterama.fab2.widget.roundedbutton.RoundedButtonHelper.SHADOW_MULTIPLIER;

@TargetApi(LOLLIPOP)
public class RoundedButtonImplLollipop
		implements RoundedButtonImpl {

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

	StateListAnimator mSavedStateListAnimator;

	BackgroundDrawable mBackgroundDrawable;

	public RoundedButtonImplLollipop(RoundedButtonDelegate delegate, RoundedButtonOptions options) {
		mDelegate = delegate;
		try {
			mView = (View) delegate;
		} catch (ClassCastException e) {
			throw new ClassCastException("delegate must be of type View");
		}
		mColor = options.color;
		mContentPadding = options.contentPadding;
		mCornerRadius = options.cornerRadius;
		mView.setElevation(options.elevation);
		mInsetPadding = options.insetPadding;
		mMaxElevation = options.maxElevation;
		mPressedTranslationZ = options.pressedTranslationZ;
		mPreventCornerOverlap = options.preventCornerOverlap;
		mView.setTranslationZ(options.translationZ);
		mUseCompatAnimation = options.useCompatAnimation;
		if (mUseCompatAnimation) {
			mSavedStateListAnimator = mView.getStateListAnimator();
			mView.setStateListAnimator(newStateListAnimator(mView, mPressedTranslationZ));
		}
		mUseCompatPadding = options.useCompatPadding;
		mView.setClipToOutline(true);

		mBackgroundDrawable = new BackgroundDrawable();
		Drawable drawableWrapper = mDelegate.createDrawableWrapper(mBackgroundDrawable);
		if (drawableWrapper == null) {
			drawableWrapper = newDefaultDrawableWrapper(mView.getContext().getTheme(),
					mBackgroundDrawable);
		}
		mView.setBackground(drawableWrapper);
		invalidatePadding();
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
			if (mUseCompatPadding) {
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
				mView.setStateListAnimator(newStateListAnimator(mView, mPressedTranslationZ));
			} else {
				mView.setStateListAnimator(mSavedStateListAnimator);
			}
		}
	}

	@Override
	public boolean isUseCompatPadding() {
		return mUseCompatPadding;
	}

	@Override
	public void setUseCompatPadding(boolean useCompatPadding) {
		if (useCompatPadding != mUseCompatPadding) {
			mUseCompatPadding = useCompatPadding;
			mBackgroundDrawable.updateBounds(null);
			mBackgroundDrawable.invalidateSelf();
			invalidatePadding();
		}
	}

	void invalidatePadding() {
		int left = mInsetPadding.left;
		int top = mInsetPadding.top;
		int right = mInsetPadding.right;
		int bottom = mInsetPadding.bottom;
		if (mUseCompatPadding) {
			float verticalPadding = mMaxElevation * SHADOW_MULTIPLIER;
			left += mMaxElevation;
			top += verticalPadding;
			right += mMaxElevation;
			bottom += verticalPadding;
		}
		int overlay = (mPreventCornerOverlap ? (int) Math.ceil((1 - COS_45) * mCornerRadius) : 0);
		mDelegate.onPaddingChanged(left, top, right, bottom, overlay);
	}

	static StateListAnimator newStateListAnimator(View view, float pressedTranslationZ) {
		Resources resources = view.getResources();
		float elevation = view.getElevation();
		int duration = resources.getInteger(R.integer.qslib_button_pressed_animation_duration);

		StateListAnimator animator = new StateListAnimator();
		AnimatorSet set = new AnimatorSet();
		set.playTogether(ObjectAnimator.ofFloat(view, View.TRANSLATION_Z, pressedTranslationZ).
						setDuration(duration),
				ObjectAnimator.ofFloat(view, ELEVATION_PROPERTY, elevation).setDuration(0L));
		animator.addState(PRESSED_SPECS, set);

		set = new AnimatorSet();
		set.playTogether(ObjectAnimator.ofFloat(view, View.TRANSLATION_Z, 0f).setDuration(duration),
				ObjectAnimator.ofFloat(view, ELEVATION_PROPERTY, elevation).setDuration(0L));
		animator.addState(BASE_SPECS, set);

		set = new AnimatorSet();
		set.playTogether(ObjectAnimator.ofFloat(view, View.TRANSLATION_Z, 0f).setDuration(0L),
				ObjectAnimator.ofFloat(view, ELEVATION_PROPERTY, 0f).setDuration(0L));
		animator.addState(StateSet.WILD_CARD, set);
		return animator;
	}

	static Drawable newDefaultDrawableWrapper(Theme theme, Drawable source) {
		TypedValue outValue = new TypedValue();
		theme.resolveAttribute(android.R.attr.colorControlHighlight, outValue, true);
		ColorStateList rippleColor = ColorStateList.valueOf(outValue.data);
		return new RippleDrawable(rippleColor, source, null);
	}

	class BackgroundDrawable extends Drawable {
		final RectF mBoundsF = new RectF();
		final Rect mBoundsI = new Rect();
		final Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);

		@Override
		public void setAlpha(int alpha) {
			// TODO ?
			// not supported because older versions do not support
		}

		@Override
		public void setColorFilter(ColorFilter cf) {
			// TODO ?
			// not supported because older versions do not support
		}

		@Override
		public int getOpacity() {
			return PixelFormat.OPAQUE;
		}

		@Override
		public void getOutline(Outline outline) {
			outline.setRoundRect(mBoundsI, mCornerRadius);
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
				return true;
			} else {
				return super.onStateChange(state);
			}
		}

		@Override
		public void draw(Canvas canvas) {
			canvas.drawRoundRect(mBoundsF, mCornerRadius, mCornerRadius, mPaint);
		}

		void updateBounds(Rect bounds) {
			if (bounds == null) {
				bounds = getBounds();
			}
			mBoundsI.set(bounds.left + mInsetPadding.left, bounds.top + mInsetPadding.top,
					bounds.right - mInsetPadding.right, bounds.bottom - mInsetPadding.bottom);
			mBoundsF.set(mBoundsI);
			if (mUseCompatPadding) {
				int paddingHorizontal = (int) Math.ceil(mMaxElevation);
				int paddingVertical = (int) Math.ceil(mMaxElevation * SHADOW_MULTIPLIER);
				mBoundsI.inset(paddingHorizontal, paddingVertical);
				mBoundsF.set(mBoundsI);
			}
 		}
	}
}
