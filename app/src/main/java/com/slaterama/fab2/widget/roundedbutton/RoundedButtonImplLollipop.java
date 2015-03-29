package com.slaterama.fab2.widget.roundedbutton;

import android.annotation.TargetApi;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.Outline;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.os.Build;
import android.util.TypedValue;
import android.view.View;

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class RoundedButtonImplLollipop extends RoundedButtonImpl {
	public RoundedButtonImplLollipop(View view, RoundedButtonOptions options) {
		super(view, options);
		mView.setElevation(options.elevation);
		mView.setTranslationZ(options.translationZ);
		mView.setClipToOutline(true);
	}

	@Override
	RoundedButtonDrawable newRoundedButtonDrawable() {
		return new RoundedButtonDrawableLollipop();
	}

	@Override
	void setSupportBackground(Drawable background) {
		mView.setBackground(wrapBackgroundDrawable(background));
	}

	@Override
	public float getElevation() {
		return mView.getElevation();
	}

	@Override
	public void setElevation(float elevation) {
		mView.setElevation(elevation);
	}

	@Override
	public float getTranslationZ() {
		return mView.getTranslationZ();
	}

	@Override
	public void setTranslationZ(float translationZ) {
		mView.setTranslationZ(translationZ);
	}

	@Override
	public void setUseCompatAnimation(boolean useCompatAnimation) {
		boolean oldValue = mUseCompatAnimation;
		super.setUseCompatAnimation(useCompatAnimation);
		if (mUseCompatAnimation != oldValue) {
			// TODO Save or restore old state list
		}
	}

	@Override
	public void setUseCompatPadding(boolean useCompatPadding) {
		boolean oldValue = mUseCompatPadding;
		super.setUseCompatPadding(useCompatPadding);
		if (mUseCompatPadding != oldValue) {
			mRoundedButtonDrawable.invalidateBounds();
		}
	}

	protected Drawable wrapBackgroundDrawable(Drawable background) {
		TypedValue outValue = new TypedValue();
		mView.getContext().getTheme().resolveAttribute(android.R.attr.colorControlHighlight,
				outValue, true);
		ColorStateList rippleColor = ColorStateList.valueOf(outValue.data);
		return new RippleDrawable(rippleColor, background, null);
	}

	class RoundedButtonDrawableLollipop extends RoundedButtonDrawable {
		@Override
		public void getOutline(Outline outline) {
			outline.setRoundRect(mBounds, mCornerRadius);
		}

		@Override
		public void draw(Canvas canvas) {
			super.draw(canvas);
			canvas.drawRoundRect(mBoundsF, mCornerRadius, mCornerRadius, mPaint);
		}
	}
}
