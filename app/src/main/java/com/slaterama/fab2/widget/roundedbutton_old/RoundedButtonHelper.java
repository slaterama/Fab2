package com.slaterama.fab2.widget.roundedbutton_old;

import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.StateSet;
import android.view.View;

public class RoundedButtonHelper {

	static int[] PRESSED_SPECS = new int[]{android.R.attr.state_pressed,
			android.R.attr.state_enabled};
	static int[] BASE_SPECS = new int[]{android.R.attr.state_enabled};
	static int[][] SPECS_ARRAY = new int[][]{PRESSED_SPECS, BASE_SPECS, StateSet.WILD_CARD};

	static String ELEVATION_PROPERTY = "elevation";
	static String TRANSLATION_Z_PROPERTY = "translationZ";

	static final float SHADOW_MULTIPLIER = 1.5f;

	// used to calculate overlap padding
	static final double COS_45 = Math.cos(Math.toRadians(45));

	final static RoundRectHelper sRoundRectHelper;

	static {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
			sRoundRectHelper = new RoundRectHelperJellybeanMr1();
		} else {
			sRoundRectHelper = new RoundRectHelperEclairMr1();
		}
	}

	static RoundedButtonImpl newRoundedButtonImpl(RoundedButtonDelegate delegate,
	                                              RoundedButtonOptions options) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			return new RoundedButtonImplLollipop(delegate, options);
		} else {
			return new RoundedButtonImplEclairMr1(delegate, options);
		}
	}

	static void getResolvedSize(View view, float cornerRadius, Rect drawablePadding,
	                            int widthMeasureSpec, int heightMeasureSpec,
	                            boolean useMeasuredSize, Point resolvedSize) {
		final int diameter = (int) (2 * cornerRadius);
		final int minWidth = diameter + drawablePadding.left + drawablePadding.right;
		final int minHeight = diameter + drawablePadding.top + drawablePadding.bottom;
		int requestedWidth = (useMeasuredSize
				? Math.max(minWidth, view.getMeasuredWidth()) : minWidth);
		int requestedHeight = (useMeasuredSize
				? Math.max(minHeight, view.getMeasuredHeight()) : minHeight);
		resolvedSize.set(View.resolveSize(requestedWidth, widthMeasureSpec),
				View.resolveSize(requestedHeight, heightMeasureSpec));
	}

	static class RoundedButtonOptions {
		ColorStateList color = null;
		float cornerRadius = 0f;
		final Rect contentPadding = new Rect();
		float elevation = 0f;
		final Rect insetPadding = new Rect();
		float maxElevation = 0f;
		float pressedTranslationZ = 0f;
		boolean preventCornerOverlap = false;
		float translationZ = 0f;
		boolean useCompatAnimation = false;
		boolean useCompatPadding = false;
	}

	private RoundedButtonHelper() {
	}

	interface OnPaddingChangedListener {
		void onPaddingChanged(int left, int top, int right, int bottom, int overlay);
	}

	interface RoundedButtonBase {
		ColorStateList getColor();

		void setColor(ColorStateList color);

		int getContentPaddingLeft();

		int getContentPaddingTop();

		int getContentPaddingRight();

		int getContentPaddingBottom();

		void setContentPadding(int left, int top, int right, int bottom);

		float getCornerRadius();

		void setCornerRadius(float cornerRadius);

		int getInsetPaddingLeft();

		int getInsetPaddingTop();

		int getInsetPaddingRight();

		int getInsetPaddingBottom();

		void setInsetPadding(int left, int top, int right, int bottom);

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

		Drawable createDrawableWrapper(Drawable source);
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
