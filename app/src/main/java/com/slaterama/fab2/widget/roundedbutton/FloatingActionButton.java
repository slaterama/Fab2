package com.slaterama.fab2.widget.roundedbutton;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;

import com.slaterama.fab2.R;
import com.slaterama.fab2.widget.roundedbutton.RoundedButtonImpl.RoundedButtonAttributes;

public class FloatingActionButton extends RoundedImageButton {

	public FloatingActionButton(Context context) {
		this(context, null);
	}

	public FloatingActionButton(Context context, AttributeSet attrs) {
		this(context, attrs, R.attr.floatingActionButtonStyle);
	}

	public FloatingActionButton(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	public FloatingActionButton(Context context, AttributeSet attrs, int defStyleAttr,
								int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
	}

	RoundedButtonAttributes fillAttributes(Context context, AttributeSet attrs, int defStyleAttr,
										   int defStyleRes) {
		RoundedButtonAttributes attributes = new RoundedButtonAttributes();
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RoundedImageButton,
				defStyleAttr, defStyleRes);
		attributes.color = a.getColorStateList(R.styleable.FloatingActionButton_qslib_buttonColor);
		int defaultContentPadding = a.getDimensionPixelOffset(
				R.styleable.FloatingActionButton_qslib_contentPadding, 0);
		attributes.contentPadding.left = a.getDimensionPixelOffset(
				R.styleable.FloatingActionButton_qslib_contentPaddingLeft, defaultContentPadding);
		attributes.contentPadding.top = a.getDimensionPixelOffset(
				R.styleable.FloatingActionButton_qslib_contentPaddingTop, defaultContentPadding);
		attributes.contentPadding.right = a.getDimensionPixelOffset(
				R.styleable.FloatingActionButton_qslib_contentPaddingRight, defaultContentPadding);
		attributes.contentPadding.bottom = a.getDimensionPixelOffset(
				R.styleable.FloatingActionButton_qslib_contentPaddingBottom, defaultContentPadding);
		attributes.cornerRadius = a.getDimension(R.styleable.FloatingActionButton_qslib_cornerRadius,
				attributes.cornerRadius);
		attributes.elevation = a.getDimension(R.styleable.FloatingActionButton_qslib_elevation,
				attributes.elevation);
		int defaultInsetPadding = a.getDimensionPixelOffset(
				R.styleable.FloatingActionButton_qslib_insetPadding, 0);
		attributes.insetPadding.left = a.getDimensionPixelOffset(
				R.styleable.FloatingActionButton_qslib_insetPaddingLeft, defaultInsetPadding);
		attributes.insetPadding.top = a.getDimensionPixelOffset(
				R.styleable.FloatingActionButton_qslib_insetPaddingTop, defaultInsetPadding);
		attributes.insetPadding.right = a.getDimensionPixelOffset(
				R.styleable.FloatingActionButton_qslib_insetPaddingRight, defaultInsetPadding);
		attributes.insetPadding.bottom = a.getDimensionPixelOffset(
				R.styleable.FloatingActionButton_qslib_insetPaddingBottom, defaultInsetPadding);
		attributes.maxElevation = a.getDimension(R.styleable.FloatingActionButton_qslib_maxElevation,
				attributes.maxElevation);
		attributes.pressedTranslationZ = a.getDimension(
				R.styleable.FloatingActionButton_qslib_pressedTranslationZ, attributes.pressedTranslationZ);
		attributes.preventCornerOverlap = a.getBoolean(
				R.styleable.FloatingActionButton_qslib_preventCornerOverlap, attributes.preventCornerOverlap);
		attributes.translationZ = a.getDimension(R.styleable.FloatingActionButton_qslib_translationZ,
				attributes.translationZ);
		attributes.useCompatAnimation = a.getBoolean(
				R.styleable.FloatingActionButton_qslib_useCompatAnimation, attributes.useCompatAnimation);
		attributes.useCompatPadding = a.getBoolean(
				R.styleable.FloatingActionButton_qslib_useCompatPadding, attributes.useCompatPadding);
		a.recycle();
		return attributes;
	}


	@Override
	boolean shouldUseMeasuredSize() {
		return false;
	}
}
