package com.slaterama.fab2.widget.roundedbutton;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
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
		options.disabledElevation = a.getDimension(
				R.styleable.RoundedButton_qslib_disabledElevation, options.disabledElevation);
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
		mImpl = newRoundedButtonImpl(this, options);
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
	public void onPaddingChanged(int left, int top, int right, int bottom) {
		super.setPadding(left, top, right, bottom);
	}
}
