package com.slaterama.fab2.widget.roundedbutton;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.Button;

import com.slaterama.fab2.R;

import static com.slaterama.fab2.widget.roundedbutton.RoundedButtonHelper.RoundedButtonDelegate;
import static com.slaterama.fab2.widget.roundedbutton.RoundedButtonHelper.RoundedButtonImpl;
import static com.slaterama.fab2.widget.roundedbutton.RoundedButtonHelper.RoundedButtonOptions;
import static com.slaterama.fab2.widget.roundedbutton.RoundedButtonHelper.newRoundedButtonImpl;

public class RoundedButton extends Button
		implements RoundedButtonDelegate {

	RoundedButtonImpl mImpl;

	final Rect mContentPadding = new Rect();

	boolean mPreventCornerOverlap;

	final Rect mDrawablePadding = new Rect();

	public RoundedButton(Context context) {
		this(context, null);
	}

	public RoundedButton(Context context, AttributeSet attrs) {
		this(context, attrs, R.attr.roundedButtonStyle);
	}

	public RoundedButton(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initialize(context, attrs, defStyleAttr, 0);
	}

	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	public RoundedButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		initialize(context, attrs, defStyleAttr, defStyleRes);
	}

	void initialize(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RoundedButton,
				defStyleAttr, defStyleRes);
		RoundedButtonOptions options = new RoundedButtonOptions();
		options.color = a.getColorStateList(R.styleable.RoundedButton_qslib_buttonColor);
		int defaultContentPadding = a.getDimensionPixelOffset(
				R.styleable.RoundedButton_qslib_contentPadding, 0);
		mContentPadding.left = a.getDimensionPixelOffset(
				R.styleable.RoundedButton_qslib_contentPaddingLeft, defaultContentPadding);
		mContentPadding.top = a.getDimensionPixelOffset(
				R.styleable.RoundedButton_qslib_contentPaddingTop, defaultContentPadding);
		mContentPadding.right = a.getDimensionPixelOffset(
				R.styleable.RoundedButton_qslib_contentPaddingRight, defaultContentPadding);
		mContentPadding.bottom = a.getDimensionPixelOffset(
				R.styleable.RoundedButton_qslib_contentPaddingBottom, defaultContentPadding);
		options.cornerRadius = a.getDimension(R.styleable.RoundedButton_qslib_cornerRadius,
				options.cornerRadius);
		options.elevation = a.getDimension(R.styleable.RoundedButton_qslib_elevation,
				options.elevation);
		int defaultInsetPadding = a.getDimensionPixelOffset(
				R.styleable.RoundedButton_qslib_insetPadding, 0);
		options.insetPadding.left = a.getDimensionPixelOffset(
				R.styleable.RoundedButton_qslib_insetPaddingLeft, defaultInsetPadding);
		options.insetPadding.top = a.getDimensionPixelOffset(
				R.styleable.RoundedButton_qslib_insetPaddingTop, defaultInsetPadding);
		options.insetPadding.right = a.getDimensionPixelOffset(
				R.styleable.RoundedButton_qslib_insetPaddingRight, defaultInsetPadding);
		options.insetPadding.bottom = a.getDimensionPixelOffset(
				R.styleable.RoundedButton_qslib_insetPaddingBottom, defaultInsetPadding);
		options.maxElevation = a.getDimension(R.styleable.RoundedButton_qslib_maxElevation,
				options.maxElevation);
		options.pressedTranslationZ = a.getDimension(
				R.styleable.RoundedButton_qslib_pressedTranslationZ, options.pressedTranslationZ);
		options.translationZ = a.getDimension(R.styleable.RoundedButton_qslib_translationZ,
				options.translationZ);
		options.useCompatAnimation = a.getBoolean(
				R.styleable.RoundedButton_qslib_useCompatAnimation, options.useCompatAnimation);
		options.useCompatPadding = a.getBoolean(
				R.styleable.RoundedButton_qslib_useCompatPadding, options.useCompatPadding);
		mImpl = newRoundedButtonImpl(this, options);
		boolean preventCornerOverlap = a.getBoolean(
				R.styleable.RoundedButton_qslib_preventCornerOverlap, mPreventCornerOverlap);
		a.recycle();
		setPreventCornerOverlap(preventCornerOverlap);
	}

	@Override
	public void setPadding(int left, int top, int right, int bottom) {
		// NO OP
	}

	@Override
	public void setPaddingRelative(int start, int top, int end, int bottom) {
		// NO OP
	}

	@Override
	public ColorStateList getColor() {
		return mImpl.getColor();
	}

	@Override
	public void setColor(int color) {
		setColor(ColorStateList.valueOf(color));
	}

	@Override
	public void setColor(ColorStateList color) {
		mImpl.setColor(color);
	}

	@Override
	public int getContentPaddingLeft() {
		return mImpl.getContentPaddingLeft();
	}

	@Override
	public int getContentPaddingTop() {
		return mImpl.getContentPaddingTop();
	}

	@Override
	public int getContentPaddingRight() {
		return mImpl.getContentPaddingRight();
	}

	@Override
	public int getContentPaddingBottom() {
		return mImpl.getContentPaddingBottom();
	}

	@Override
	public void setContentPadding(int left, int top, int right, int bottom) {
		mImpl.setContentPadding(left, top, right, bottom);
	}

	@Override
	public float getCornerRadius() {
		return mImpl.getCornerRadius();
	}

	@Override
	public void setCornerRadius(float cornerRadius) {
		mImpl.setCornerRadius(cornerRadius);
	}

	@Override
	public int getInsetPaddingLeft() {
		return mImpl.getInsetPaddingLeft();
	}

	@Override
	public int getInsetPaddingTop() {
		return mImpl.getInsetPaddingTop();
	}

	@Override
	public int getInsetPaddingRight() {
		return mImpl.getInsetPaddingRight();
	}

	@Override
	public int getInsetPaddingBottom() {
		return mImpl.getInsetPaddingBottom();
	}

	@Override
	public void setInsetPadding(int left, int top, int right, int bottom) {
		mImpl.setInsetPadding(left, top, right, bottom);
	}

	@Override
	public float getMaxElevation() {
		return mImpl.getMaxElevation();
	}

	@Override
	public void setMaxElevation(float maxElevation) {
		mImpl.setMaxElevation(maxElevation);
	}

	@Override
	public boolean isPreventCornerOverlap() {
		return mImpl.isPreventCornerOverlap();
	}

	@Override
	public void setPreventCornerOverlap(boolean preventCornerOverlap) {
		mImpl.setPreventCornerOverlap(preventCornerOverlap);
	}

	@Override
	public float getSupportElevation() {
		return mImpl.getSupportElevation();
	}

	@Override
	public void setSupportElevation(float elevation) {
		mImpl.setSupportElevation(elevation);
	}

	@Override
	public float getSupportTranslationZ() {
		return mImpl.getSupportTranslationZ();
	}

	@Override
	public void setSupportTranslationZ(float translationZ) {
		mImpl.setSupportTranslationZ(translationZ);
	}

	@Override
	public boolean isUseCompatAnimation() {
		return mImpl.isUseCompatAnimation();
	}

	@Override
	public void setUseCompatAnimation(boolean useCompatAnimation) {
		mImpl.setUseCompatAnimation(useCompatAnimation);
	}

	@Override
	public boolean isUseCompatPadding() {
		return mImpl.isUseCompatPadding();
	}

	@Override
	public void setUseCompatPadding(boolean useCompatPadding) {
		mImpl.setUseCompatPadding(useCompatPadding);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		final float cornerRadius = mImpl.getCornerRadius();
		final int minWidth = (int) (2 * cornerRadius) +
				mDrawablePadding.left + mDrawablePadding.right;
		final int minHeight = (int) (2 * cornerRadius) +
				mDrawablePadding.top + mDrawablePadding.bottom;
		final int resolvedWidth;
		final int resolvedHeight;

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			int measuredWidthAndState = getMeasuredWidthAndState();
			int measuredHeightAndState = getMeasuredHeightAndState();
			int measuredWidth = measuredWidthAndState & MEASURED_SIZE_MASK;
			int measuredWidthState = measuredWidthAndState & MEASURED_STATE_MASK;
			int measuredHeight = measuredHeightAndState & MEASURED_SIZE_MASK;
			int measuredHeightState = measuredHeightAndState & MEASURED_STATE_MASK;
			int resolvedWidthSizeAndState = resolveSizeAndState(
					Math.max(measuredWidth, minWidth), widthMeasureSpec, measuredWidthState);
			int resolvedHeightSizeAndState = resolveSizeAndState(
					Math.max(measuredHeight, minHeight), heightMeasureSpec, measuredHeightState);
			resolvedWidth = resolvedWidthSizeAndState & MEASURED_SIZE_MASK;
			resolvedHeight = resolvedHeightSizeAndState & MEASURED_SIZE_MASK;
		} else {
			int measuredWidth = getMeasuredWidth();
			int measuredHeight = getMeasuredHeight();
			resolvedWidth = resolveSize(Math.max(measuredWidth, minWidth), widthMeasureSpec);
			resolvedHeight = resolveSize(Math.max(measuredHeight, minHeight), heightMeasureSpec);
		}

		setMeasuredDimension(resolvedWidth, resolvedHeight);
	}

	@Override
	public Drawable createDrawableWrapper(Drawable source) {
		return null;
	}

	@Override
	public void onPaddingChanged(int left, int top, int right, int bottom, int overlay) {
		super.setPadding(
				left + overlay + mContentPadding.left, top + overlay + mContentPadding.top,
				right + overlay + mContentPadding.right, bottom + overlay + mContentPadding.bottom);
		mDrawablePadding.set(left, top, right, bottom);
		requestLayout();
	}
}
