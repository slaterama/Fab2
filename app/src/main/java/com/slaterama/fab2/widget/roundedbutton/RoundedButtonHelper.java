package com.slaterama.fab2.widget.roundedbutton;

import android.content.res.ColorStateList;
import android.graphics.Rect;
import android.os.Build;

public class RoundedButtonHelper {

	private RoundedButtonHelper() {
	}

	static RoundedButtonImpl newRoundedButtonImpl(RoundedButtonOptions options) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			return new RoundedButtonImplLollipop(options);
		} else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
			return new RoundedButtonImplJellybeanMr1(options);
		} else {
			return new RoundedButtonImplEclairMr1(options);
		}
	}

	static class RoundedButtonOptions {
		ColorStateList color = null;
		final Rect contentPadding = new Rect();
		float cornerRadius = 0f;
		float disabledElevation = 0f;
		float elevation = 0f;
		final Rect insetPadding = new Rect();
		float maxElevation = 0f;
		float pressedTranslationZ = 0f;
		boolean preventCornerOverlap = false;
		float translationZ = 0f;
		boolean useCompatAnimation = false;
		boolean useCompatPadding = false;
	}

	interface OnPaddingChangedListener {
		void onPaddingChanged(int left, int top, int right, int bottom);
	}

	interface RoundedButtonBase {
		void setColor(ColorStateList color);
		int getContentPaddingLeft();
		int getContentPaddingTop();
		int getContentPaddingRight();
		int getContentPaddingBottom();
		void setContentPadding(int paddingLeft, int paddingTop, int paddingRight,
		                       int paddingBottom);
		float getCornerRadius();
		void setCornerRadius(float cornerRadius);
		int getInsetPaddingLeft();
		int getInsetPaddingTop();
		int getInsetPaddingRight();
		int getInsetPaddingBottom();
		void setInsetPadding(int paddingLeft, int paddingTop, int paddingRight, int paddingBottom);
		float getMaxElevation();
		void setMaxElevation(float maxElevation);
		boolean isPreventCornerOverlap();
		void setPreventCornerOverlap(boolean preventCornerOverlap);
		float getSupportElevation();
		void setSupportElevation(float elevation);
		float getSupportTranslationZ();
		void setSupportTranslationZ(float translationZ);
		boolean isUseCompatAnimation();
		void setUseCompatAnimation(boolean useCompatAnimation);
		boolean isUseCompatPadding();
		void setUseCompatPadding(boolean useCompatPadding);
	}

	interface RoundedButtonDelegate extends RoundedButtonBase, OnPaddingChangedListener {
		void setColor(int color);
	}

	interface RoundedButtonImpl extends RoundedButtonBase {

	}
}
