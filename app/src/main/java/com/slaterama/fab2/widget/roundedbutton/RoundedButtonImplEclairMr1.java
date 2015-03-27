package com.slaterama.fab2.widget.roundedbutton;

import android.annotation.TargetApi;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;

import com.slaterama.fab2.R;

import static com.slaterama.fab2.widget.roundedbutton.RoundedButtonHelper.RoundedButtonDelegate;
import static com.slaterama.fab2.widget.roundedbutton.RoundedButtonHelper.RoundedButtonOptions;

@TargetApi(Build.VERSION_CODES.ECLAIR_MR1)
public class RoundedButtonImplEclairMr1 extends RoundedButtonImplBase {

	float mElevation;
	float mTranslationZ;

	private final long mAnimDuration;
	private final int mShadowStartColor;
	private final int mShadowEndColor;
	final float mInsetShadowExtra;

	private final Paint mPaint;
	private final Paint mCornerShadowPaint;
	private final Paint mEdgeShadowPaint;
	private final Paint mSolidPaint;

	public RoundedButtonImplEclairMr1(RoundedButtonDelegate delegate,
	                                  RoundedButtonOptions options) {
		super(delegate, options);
		mElevation = options.elevation;
		mTranslationZ = options.translationZ;
		/*
		// TODO Set up animations
		if (mUseCompatAnimation) {
			mSavedStateListAnimator = mView.getStateListAnimator();
			mView.setStateListAnimator(newStateListAnimator(mView, mPressedTranslationZ));
		}
		*/

		Resources resources = mView.getResources();
		mAnimDuration = resources.getInteger(R.integer.qslib_button_pressed_animation_duration);
		mShadowStartColor = resources.getColor(R.color.qslib_button_shadow_start_color);
		mShadowEndColor = resources.getColor(R.color.qslib_button_shadow_end_color);
		mInsetShadowExtra = resources.getDimension(R.dimen.qslib_button_compat_inset_shadow);

		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
		mCornerShadowPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
		mSolidPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);

		mPaint.setColor(options.color.getDefaultColor());

		mCornerShadowPaint.setStyle(Paint.Style.FILL);
		mCornerShadowPaint.setDither(true);
		mEdgeShadowPaint = new Paint(mCornerShadowPaint);

		mSolidPaint.setColor(mShadowStartColor);
		mSolidPaint.setStyle(Paint.Style.FILL);
		mSolidPaint.setDither(true);
	}

	@Override
	BackgroundDrawableBase createBackgroundDrawable() {
		return new BackgroundDrawable();
	}

	@Override
	boolean willWrapBackgroundDrawable() {
		return false;
	}

	@Override
	public float getSupportElevation() {
		return mElevation;
	}

	@Override
	public void setSupportElevation(float elevation) {
		if (elevation != mElevation) {
			mElevation = elevation;
			mBackgroundDrawable.invalidateSelf();
		}
	}

	@Override
	public float getSupportTranslationZ() {
		return mTranslationZ;
	}

	@Override
	public void setSupportTranslationZ(float translationZ) {
		if (translationZ != mTranslationZ) {
			mTranslationZ = translationZ;
			mBackgroundDrawable.invalidateSelf();
		}
	}

	@Override
	protected boolean shouldUseCompatAnimation() {
		return true;
	}

	@Override
	protected boolean shouldUseCompatPadding() {
		return true;
	}

	class BackgroundDrawable extends BackgroundDrawableBase {
		@Override
		public void setAlpha(int alpha) {
			mPaint.setAlpha(alpha);
			mCornerShadowPaint.setAlpha(alpha);
			mEdgeShadowPaint.setAlpha(alpha);
			mSolidPaint.setAlpha(alpha);
		}

		@Override
		public void setColorFilter(ColorFilter cf) {
			mPaint.setColorFilter(cf);
			mCornerShadowPaint.setColorFilter(cf);
			mEdgeShadowPaint.setColorFilter(cf);
			mSolidPaint.setColorFilter(cf);
		}

		@Override
		public void draw(Canvas canvas) {

		}

		@Override
		public void updateBounds(Rect bounds) {

		}
	}
}
