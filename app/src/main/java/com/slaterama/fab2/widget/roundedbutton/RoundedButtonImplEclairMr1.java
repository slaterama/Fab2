package com.slaterama.fab2.widget.roundedbutton;

import android.annotation.TargetApi;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;

import com.slaterama.fab2.R;

@TargetApi(Build.VERSION_CODES.ECLAIR_MR1)
public class RoundedButtonImplEclairMr1 extends RoundedButtonImpl {

	static void drawRoundRect(Canvas canvas, RectF rect, float rx, float ry, Paint paint,
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

	final RoundedButtonDrawableEclairMr1 mRoundedButtonDrawableEclairMr1;

	final RectF mCornerRect = new RectF();

	public RoundedButtonImplEclairMr1(View view, RoundedButtonOptions options) {
		super(view, options);
		mRoundedButtonDrawableEclairMr1 = (RoundedButtonDrawableEclairMr1) mRoundedButtonDrawable;
	}

	@Override
	RoundedButtonDrawable newRoundedButtonDrawable() {
		return new RoundedButtonDrawableEclairMr1();
	}

	@Override
	void setSupportBackground(Drawable background) {
		mView.setBackgroundDrawable(background);
	}

	@Override
	public void setElevation(float elevation) {
		float oldValue = mElevation;
		super.setElevation(elevation);
		if (mElevation != oldValue) {
			mRoundedButtonDrawableEclairMr1.invalidateShadow();
		}
	}

	@Override
	public void setTranslationZ(float translationZ) {
		float oldValue = mTranslationZ;
		super.setElevation(translationZ);
		if (mTranslationZ != oldValue) {
			mRoundedButtonDrawableEclairMr1.invalidateShadow();
		}
	}

	@Override
	protected boolean willUseCompatAnimation() {
		return true;
	}

	@Override
	protected boolean willUseCompatPadding() {
		return true;
	}

	class RoundedButtonDrawableEclairMr1 extends RoundedButtonDrawable {
		final long mAnimDuration;
		final int mShadowStartColor;
		final int mShadowEndColor;
		final float mInsetShadowExtra;

		final Paint mCornerShadowPaint;
		final Paint mEdgeShadowPaint;
		final Paint mSolidPaint;

		Path mCornerShadowPath;
		float mShadowSize;

		boolean mShadowDirty = true;

		public RoundedButtonDrawableEclairMr1() {
			super();
			mShadowSize = mElevation;

			Resources resources = mView.getResources();
			mAnimDuration = resources.getInteger(R.integer.qslib_button_pressed_animation_duration);
			mShadowStartColor = resources.getColor(R.color.qslib_button_shadow_start_color);
			mShadowEndColor = resources.getColor(R.color.qslib_button_shadow_end_color);
			mInsetShadowExtra = resources.getDimension(R.dimen.qslib_button_compat_inset_shadow);

			mCornerShadowPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
			mSolidPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);

			mPaint.setColor(mColor.getDefaultColor());

			mCornerShadowPaint.setStyle(Paint.Style.FILL);
			mCornerShadowPaint.setDither(true);
			mEdgeShadowPaint = new Paint(mCornerShadowPaint);

			mSolidPaint.setColor(mShadowStartColor);
			mSolidPaint.setStyle(Paint.Style.FILL);
			mSolidPaint.setDither(true);
		}

		@Override
		public void setAlpha(int alpha) {
			super.setAlpha(alpha);
			mCornerShadowPaint.setAlpha(alpha);
			mEdgeShadowPaint.setAlpha(alpha);
			mSolidPaint.setAlpha(alpha);
		}

		@Override
		public void setColorFilter(ColorFilter cf) {
			super.setColorFilter(cf);
			mCornerShadowPaint.setColorFilter(cf);
			mEdgeShadowPaint.setColorFilter(cf);
			mSolidPaint.setColorFilter(cf);
		}

		@Override
		protected boolean onStateChange(int[] state) {
			boolean retVal = super.onStateChange(state);
			// TODO Handle animations
			return retVal;
		}

		@Override
		public void draw(Canvas canvas) {
			if (mShadowDirty) {
				mShadowDirty = false;
				buildComponents(getBounds());
			}
			float dy = mShadowSize / 2;
			canvas.translate(0, dy);
			drawShadow(canvas);
			canvas.translate(0, -dy);
			drawRoundRect(canvas, mBoundsF, mCornerRadius, mCornerRadius, mPaint);
		}

		void invalidateShadow() {
			mShadowDirty = true;
			invalidateSelf();
		}

		protected void drawRoundRect(Canvas canvas, RectF rect, float rx, float ry, Paint paint) {
			RoundedButtonImplEclairMr1.drawRoundRect(canvas, rect, rx, ry, paint, mCornerRect);
		}

		void buildComponents(Rect bounds) {
			// Button is offset SHADOW_MULTIPLIER * maxElevation to account for the shadow shift.
			// We could have different top-bottom offsets to avoid extra gap above but in that case
			// center aligning Views inside the Button would be problematic.
			final float verticalOffset = mMaxElevation * SHADOW_MULTIPLIER;
			mBoundsF.set(bounds.left + mMaxElevation, bounds.top + verticalOffset,
					bounds.right - mMaxElevation, bounds.bottom - verticalOffset);
			buildShadowCorners();
		}

		void buildShadowCorners() {
			if (mShadowSize > 0f) {
				//float insetShadow = mShadowSize / 2 + mInsetShadowExtra;
				float innerRadius = 0f; //Math.max(mCornerRadius - insetShadow, 0.0f);
				float outerRadius = mCornerRadius + mShadowSize;
				RectF innerBounds = new RectF(-innerRadius, -innerRadius, innerRadius, innerRadius);
				RectF outerBounds = new RectF(-outerRadius, -outerRadius, outerRadius, outerRadius);

				if (mCornerShadowPath == null) {
					mCornerShadowPath = new Path();
				} else {
					mCornerShadowPath.reset();
				}
				mCornerShadowPath.setFillType(Path.FillType.EVEN_ODD);
				mCornerShadowPath.moveTo(-innerRadius, 0);
				mCornerShadowPath.lineTo(-outerRadius, 0);
				// outer arc
				mCornerShadowPath.arcTo(outerBounds, 180f, 90f, false);
				// inner arc
				mCornerShadowPath.arcTo(innerBounds, 270f, -90f, false);
				mCornerShadowPath.close();

				final float startRatio = innerRadius / outerRadius;
				final int[] colors = new int[]{mShadowStartColor, mShadowStartColor, mShadowEndColor};
				final float[] stops = new float[]{0.0f, startRatio, 1.0f};
				RadialGradient radialGradient = new RadialGradient(0, 0, outerRadius,
						colors, stops, Shader.TileMode.CLAMP);
				mCornerShadowPaint.setShader(radialGradient);

				// We offset the content elevation/2 pixels up to make it more realistic.
				// this is why edge shadow shader has some extra space
				// When drawing bottom edge shadow, we use that extra space.
				LinearGradient linearGradient = new LinearGradient(0, 0, 0, -outerRadius,
						colors, stops, Shader.TileMode.CLAMP);
				mEdgeShadowPaint.setShader(linearGradient);
			} else {
				mCornerShadowPath = null;
			}
		}

		void drawShadow(Canvas canvas) {
			if (mShadowSize > 0) {
				//float insetShadow = mShadowSize / 2 + mInsetShadowExtra;
				final float edgeShadowTop = -mCornerRadius - mShadowSize;
				final float edgeShadowBottom = 0f; //Math.min(-mCornerRadius + insetShadow, 0.0f);
				float inset = mCornerRadius;
				final boolean drawHorizontalEdges = mBoundsF.width() > 2 * mCornerRadius;
				final boolean drawVerticalEdges = mBoundsF.height() > 2 * mCornerRadius;

				// LT
				int saved = canvas.save();
				canvas.translate(mBoundsF.left + inset, mBoundsF.top + inset);
				canvas.drawPath(mCornerShadowPath, mCornerShadowPaint);
				if (drawHorizontalEdges) {
					canvas.drawRect(0, edgeShadowTop,
							mBoundsF.width() - 2 * inset, edgeShadowBottom,
							mEdgeShadowPaint);
				}
				canvas.restoreToCount(saved);
				// RB
				saved = canvas.save();
				canvas.translate(mBoundsF.right - inset, mBoundsF.bottom - inset);
				canvas.rotate(180f);
				canvas.drawPath(mCornerShadowPath, mCornerShadowPaint);
				if (drawHorizontalEdges) {
					canvas.drawRect(0, edgeShadowTop,
							mBoundsF.width() - 2 * inset, edgeShadowBottom,
							mEdgeShadowPaint);
				}
				canvas.restoreToCount(saved);
				// LB
				saved = canvas.save();
				canvas.translate(mBoundsF.left + inset, mBoundsF.bottom - inset);
				canvas.rotate(270f);
				canvas.drawPath(mCornerShadowPath, mCornerShadowPaint);
				if (drawVerticalEdges) {
					canvas.drawRect(0, edgeShadowTop,
							mBoundsF.height() - 2 * inset, edgeShadowBottom,
							mEdgeShadowPaint);
				}
				canvas.restoreToCount(saved);
				// RT
				saved = canvas.save();
				canvas.translate(mBoundsF.right - inset, mBoundsF.top + inset);
				canvas.rotate(90f);
				canvas.drawPath(mCornerShadowPath, mCornerShadowPaint);
				if (drawVerticalEdges) {
					canvas.drawRect(0, edgeShadowTop,
							mBoundsF.height() - 2 * inset, edgeShadowBottom,
							mEdgeShadowPaint);
				}
				canvas.restoreToCount(saved);
				// Center
				if (drawHorizontalEdges && drawVerticalEdges) {
					saved = canvas.save();
					canvas.translate(mBoundsF.left + inset, mBoundsF.top + inset);
					canvas.drawRect(0f, 0f,
							mBoundsF.width() - 2 * inset, mBoundsF.height() - 2 * inset,
							mSolidPaint);
					canvas.restoreToCount(saved);
				}
			}
		}
	}
}
