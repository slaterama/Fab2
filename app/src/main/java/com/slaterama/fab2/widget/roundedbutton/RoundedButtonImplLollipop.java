package com.slaterama.fab2.widget.roundedbutton;

import android.animation.StateListAnimator;
import android.annotation.TargetApi;
import android.content.res.ColorStateList;
import android.graphics.ColorFilter;
import android.graphics.Outline;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.os.Build;
import android.util.TypedValue;

import com.slaterama.fab2.widget.roundedbutton.ShadowAnimationHelper.ShadowAnimationHelperLollipop;

import static com.slaterama.fab2.widget.roundedbutton.RoundedButtonHelper.RoundedButtonDelegate;
import static com.slaterama.fab2.widget.roundedbutton.RoundedButtonHelper.RoundedButtonOptions;

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class RoundedButtonImplLollipop extends RoundedButtonImpl {

	StateListAnimator mSavedStateListAnimator;
	ShadowAnimationHelperLollipop mShadowAnimationHelperLollipop;

	public RoundedButtonImplLollipop(RoundedButtonDelegate delegate, RoundedButtonOptions options) {
		super(delegate, options);
		mView.setElevation(options.elevation);
		mView.setTranslationZ(options.translationZ);
		mUseCompatAnimation = options.useCompatAnimation;
		mShadowAnimationHelperLollipop = (ShadowAnimationHelperLollipop) mShadowAnimationHelper;
		if (mUseCompatAnimation) {
			mSavedStateListAnimator = mView.getStateListAnimator();
			mView.setStateListAnimator(mShadowAnimationHelperLollipop
					.configureStateListAnimator(mView.getElevation(), mPressedTranslationZ));
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
			mView.setStateListAnimator(mShadowAnimationHelperLollipop
					.configureStateListAnimator(mView.getElevation(), mPressedTranslationZ));
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

	class BackgroundDrawable extends BackgroundDrawableBase {
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
	}
}
