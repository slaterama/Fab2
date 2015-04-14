package com.slaterama.fab2.widget.roundedbutton;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;

import com.slaterama.fab2.R;

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

	@Override
	void initializeImpl(RoundedButtonImpl impl, Context context, AttributeSet attrs,
	                    int defStyleAttr, int defStyleRes) {
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RoundedButton,
				defStyleAttr, defStyleRes);

		final int N = a.getIndexCount();
		for (int i = 0; i < N; i++) {
			int attr = a.getIndex(i);
			switch (attr) {
				case R.styleable.FloatingActionButton_qslib_buttonColor:
					impl.setColor(a.getColorStateList(attr));
					break;
				case R.styleable.FloatingActionButton_qslib_cornerRadius:
					impl.setCornerRadius(a.getDimension(attr, 0f));
					break;
				case R.styleable.FloatingActionButton_qslib_elevation:
					impl.setElevation(a.getDimension(attr, 0f));
					break;
				case R.styleable.FloatingActionButton_qslib_maxElevation:
					impl.setMaxElevation(a.getDimension(attr, 0f));
					break;
				case R.styleable.FloatingActionButton_qslib_pressedTranslationZ:
					impl.setPressedTranslationZ(a.getDimension(attr, 0f));
					break;
				case R.styleable.FloatingActionButton_qslib_preventCornerOverlap:
					impl.setPreventCornerOverlap(a.getBoolean(attr, false));
					break;
				case R.styleable.FloatingActionButton_qslib_translationZ:
					impl.setTranslationZ(a.getDimension(attr, 0f));
					break;
				case R.styleable.FloatingActionButton_qslib_useCompatAnimation:
					impl.setUseCompatAnimation(a.getBoolean(attr, false));
					break;
				case R.styleable.FloatingActionButton_qslib_useCompatPadding:
					impl.setUseCompatPadding(a.getBoolean(attr, false));
					break;
			}
		}

		// We'll always set contentPadding & drawablePadding
		int defaultContentPadding = a.getDimensionPixelOffset(
				R.styleable.FloatingActionButton_qslib_contentPadding, 0);
		Rect contentPadding = new Rect();
		contentPadding.left = a.getDimensionPixelOffset(
				R.styleable.FloatingActionButton_qslib_contentPaddingLeft, defaultContentPadding);
		contentPadding.top = a.getDimensionPixelOffset(
				R.styleable.FloatingActionButton_qslib_contentPaddingTop, defaultContentPadding);
		contentPadding.right = a.getDimensionPixelOffset(
				R.styleable.FloatingActionButton_qslib_contentPaddingRight, defaultContentPadding);
		contentPadding.bottom = a.getDimensionPixelOffset(
				R.styleable.FloatingActionButton_qslib_contentPaddingBottom, defaultContentPadding);
		impl.setContentPadding(contentPadding);

		int defaultInsetPadding = a.getDimensionPixelOffset(
				R.styleable.FloatingActionButton_qslib_insetPadding, 0);
		Rect insetPadding = new Rect();
		insetPadding.left = a.getDimensionPixelOffset(
				R.styleable.FloatingActionButton_qslib_insetPaddingLeft, defaultInsetPadding);
		insetPadding.top = a.getDimensionPixelOffset(
				R.styleable.FloatingActionButton_qslib_insetPaddingTop, defaultInsetPadding);
		insetPadding.right = a.getDimensionPixelOffset(
				R.styleable.FloatingActionButton_qslib_insetPaddingRight, defaultInsetPadding);
		insetPadding.bottom = a.getDimensionPixelOffset(
				R.styleable.FloatingActionButton_qslib_insetPaddingBottom, defaultInsetPadding);
		impl.setInsetPadding(insetPadding);

		a.recycle();
	}

	@Override
	boolean shouldUseMeasuredSize() {
		return false;
	}
}
