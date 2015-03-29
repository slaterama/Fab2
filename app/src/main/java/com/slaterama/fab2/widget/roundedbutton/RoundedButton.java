package com.slaterama.fab2.widget.roundedbutton;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.Button;

import com.slaterama.fab2.R;

import static com.slaterama.fab2.widget.roundedbutton.RoundedButtonImpl.RoundedButtonDelegate;
import static com.slaterama.fab2.widget.roundedbutton.RoundedButtonImpl.RoundedButtonOptions;
import static com.slaterama.fab2.widget.roundedbutton.RoundedButtonImpl.getResolvedSize;

public class RoundedButton extends Button
		implements RoundedButtonDelegate {

	RoundedButtonImpl mImpl;

	float mCornerRadius;

	final Rect mDrawablePadding = new Rect();

	final Point mResolvedSize = new Point();

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
		RoundedButtonOptions options = newOptions(context, attrs, defStyleAttr, defStyleRes);
		mImpl = RoundedButtonImpl.newInstance(this, options);
		mCornerRadius = options.cornerRadius;
	}

	RoundedButtonOptions newOptions(Context context, AttributeSet attrs, int defStyleAttr,
	                                int defStyleRes) {
		RoundedButtonOptions options = new RoundedButtonOptions();
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RoundedButton,
				defStyleAttr, defStyleRes);
		options.color = a.getColorStateList(R.styleable.RoundedButton_qslib_buttonColor);
		int defaultContentPadding = a.getDimensionPixelOffset(
				R.styleable.RoundedButton_qslib_contentPadding, 0);
		options.contentPadding.left = a.getDimensionPixelOffset(
				R.styleable.RoundedButton_qslib_contentPaddingLeft, defaultContentPadding);
		options.contentPadding.top = a.getDimensionPixelOffset(
				R.styleable.RoundedButton_qslib_contentPaddingTop, defaultContentPadding);
		options.contentPadding.right = a.getDimensionPixelOffset(
				R.styleable.RoundedButton_qslib_contentPaddingRight, defaultContentPadding);
		options.contentPadding.bottom = a.getDimensionPixelOffset(
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
		options.preventCornerOverlap = a.getBoolean(
				R.styleable.RoundedButton_qslib_preventCornerOverlap, options.preventCornerOverlap);
		options.translationZ = a.getDimension(R.styleable.RoundedButton_qslib_translationZ,
				options.translationZ);
		options.useCompatAnimation = a.getBoolean(
				R.styleable.RoundedButton_qslib_useCompatAnimation, options.useCompatAnimation);
		options.useCompatPadding = a.getBoolean(
				R.styleable.RoundedButton_qslib_useCompatPadding, options.useCompatPadding);
		a.recycle();
		return options;
	}

	@Override
	public void setPadding(int left, int top, int right, int bottom) {
		// NO OP
	}

	@Override
	public void setPaddingRelative(int start, int top, int end, int bottom) {
		// NO OP
	}

	public ColorStateList getColor() {
		return mImpl.getColor();
	}

	public void setColor(ColorStateList color) {
		mImpl.setColor(color);
	}

	public void setColor(int color) {
		mImpl.setColor(ColorStateList.valueOf(color));
	}

	public int getContentPaddingLeft() {
		return mImpl.getContentPadding().left;
	}

	public int getContentPaddingTop() {
		return mImpl.getContentPadding().top;
	}

	public int getContentPaddingRight() {
		return mImpl.getContentPadding().right;
	}

	public int getContentPaddingBottom() {
		return mImpl.getContentPadding().bottom;
	}

	public void setContentPadding(Rect contentPadding) {
		mImpl.setContentPadding(contentPadding);
	}

	public float getCornerRadius() {
		return mImpl.getCornerRadius();
	}

	public void setCornerRadius(float cornerRadius) {
		mImpl.setCornerRadius(cornerRadius);
		mCornerRadius = cornerRadius;
	}

	public float getSupportElevation() {
		return mImpl.getElevation();
	}

	public void setSupportElevation(float elevation) {
		mImpl.setElevation(elevation);
	}

	public int getInsetPaddingLeft() {
		return mImpl.getInsetPadding().left;
	}

	public int getInsetPaddingTop() {
		return mImpl.getInsetPadding().top;
	}

	public int getInsetPaddingRight() {
		return mImpl.getInsetPadding().right;
	}

	public int getInsetPaddingBottom() {
		return mImpl.getInsetPadding().bottom;
	}

	public void setInsetPadding(Rect insetPadding) {
		mImpl.setInsetPadding(insetPadding);
	}

	public float getMaxElevation() {
		return mImpl.getMaxElevation();
	}

	public void setMaxElevation(float maxElevation) {
		mImpl.setMaxElevation(maxElevation);
	}

	public float getPressedTranslationZ() {
		return mImpl.getPressedTranslationZ();
	}

	public void setPressedTranslationZ(float pressedTranslationZ) {
		mImpl.setPressedTranslationZ(pressedTranslationZ);
	}

	public boolean isPreventCornerOverlap() {
		return mImpl.isPreventCornerOverlap();
	}

	public void setPreventCornerOverlap(boolean preventCornerOverlap) {
		mImpl.setPreventCornerOverlap(preventCornerOverlap);
	}

	public float getSupportTranslationZ() {
		return mImpl.getTranslationZ();
	}

	public void setSupportTranslationZ(float translationZ) {
		mImpl.setTranslationZ(translationZ);
	}

	public boolean isUseCompatAnimation() {
		return mImpl.isUseCompatAnimation();
	}

	public void setUseCompatAnimation(boolean useCompatAnimation) {
		mImpl.setUseCompatAnimation(useCompatAnimation);
	}

	public boolean isUseCompatPadding() {
		return mImpl.isUseCompatPadding();
	}

	public void setUseCompatPadding(boolean useCompatPadding) {
		mImpl.setUseCompatPadding(useCompatPadding);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		getResolvedSize(this, mCornerRadius, mDrawablePadding,
				widthMeasureSpec, heightMeasureSpec, true, mResolvedSize);
		Log.d("RoundedButton", String.format("measuredWidth=%d, measuredHeight=%d, mResolvedSize=(%d, %d)",
				getMeasuredWidth(), getMeasuredHeight(), mResolvedSize.x, mResolvedSize.y));
		setMeasuredDimension(mResolvedSize.x, mResolvedSize.y);
	}

	@Override
	public void onPaddingChanged(int left, int top, int right, int bottom) {
		// TODO Get rid of drawable padding. Just have getResolvedSize take four separate ints
		mDrawablePadding.set(left, top, right, bottom);
		super.setPadding(left, top, right, bottom);
		requestLayout();
	}
}
