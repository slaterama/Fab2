package com.slaterama.fab2.widget.roundedbutton;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.StateListAnimator;
import android.annotation.TargetApi;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Outline;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.os.Build;
import android.util.StateSet;
import android.util.TypedValue;
import android.view.View;

import com.slaterama.fab2.R;

import static com.slaterama.fab2.widget.roundedbutton.RoundedButtonHelper.BASE_SPECS;
import static com.slaterama.fab2.widget.roundedbutton.RoundedButtonHelper.ELEVATION_PROPERTY;
import static com.slaterama.fab2.widget.roundedbutton.RoundedButtonHelper.PRESSED_SPECS;
import static com.slaterama.fab2.widget.roundedbutton.RoundedButtonHelper.RoundedButtonDelegate;
import static com.slaterama.fab2.widget.roundedbutton.RoundedButtonHelper.RoundedButtonOptions;
import static com.slaterama.fab2.widget.roundedbutton.RoundedButtonHelper.SHADOW_MULTIPLIER;

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class RoundedButtonImplLollipop extends RoundedButtonImpl {

	StateListAnimator mSavedStateListAnimator;

	public RoundedButtonImplLollipop(RoundedButtonDelegate delegate, RoundedButtonOptions options) {
		super(delegate, options);
		mView.setElevation(options.elevation);
		mView.setTranslationZ(options.translationZ);
		mUseCompatAnimation = options.useCompatAnimation;
		if (mUseCompatAnimation) {
			mSavedStateListAnimator = mView.getStateListAnimator();
			mView.setStateListAnimator(newStateListAnimator(mView, mPressedTranslationZ));
		}
		mView.setClipToOutline(true);
	}

	@Override
	BackgroundDrawableBase createBackgroundDrawable() {
		return new BackgroundDrawable();
	}

	@Override
	boolean willWrapBackgroundDrawable() {
		return true;
	}

	@Override
	Drawable wrapBackgroundDrawable(BackgroundDrawableBase drawable) {
		Drawable wrapper = mDelegate.createDrawableWrapper(mBackgroundDrawable);
		if (wrapper == null) {
			TypedValue outValue = new TypedValue();
			mView.getContext().getTheme().resolveAttribute(android.R.attr.colorControlHighlight,
					outValue, true);
			ColorStateList rippleColor = ColorStateList.valueOf(outValue.data);
			wrapper = new RippleDrawable(rippleColor, drawable, null);
		}
		return wrapper;
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
	protected void onUseCompatAnimationChange(boolean useCompatAnimation) {
		super.onUseCompatAnimationChange(useCompatAnimation);
		if (useCompatAnimation) {
			mSavedStateListAnimator = mView.getStateListAnimator();
			mView.setStateListAnimator(newStateListAnimator(mView, mPressedTranslationZ));
		} else {
			mView.setStateListAnimator(mSavedStateListAnimator);
		}
	}

	@Override
	protected void onUseCompatPaddingChange(boolean useCompatPadding) {
		super.onUseCompatPaddingChange(useCompatPadding);
		mBackgroundDrawable.updateBounds(null);
		mBackgroundDrawable.invalidateSelf();
		invalidatePadding();
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

	class BackgroundDrawable extends BackgroundDrawableBase {
		final RectF mBoundsF = new RectF();
		final Rect mBoundsI = new Rect();

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
		public void getOutline(Outline outline) {
			outline.setRoundRect(mBoundsI, mCornerRadius);
		}

		@Override
		public void draw(Canvas canvas) {
			sRoundRectHelper.drawRoundRect(canvas, mBoundsF, mCornerRadius, mCornerRadius, mPaint);
		}

		@Override
		public void updateBounds(Rect bounds) {
			// TODO Could move to super????
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
