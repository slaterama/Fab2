package com.slaterama.fab2.widget2.roundedbutton;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.Button;

import com.slaterama.fab2.R;

import static com.slaterama.fab2.widget2.roundedbutton.RoundedButtonImpl.setViewBackground;

public class RoundedButton extends Button
		implements RoundedButtonImpl.OnPaddingChangeListener {

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

	void initialize(Context context, AttributeSet attrs, int defStyleAttr,
	                             int defStyleRes) {
		mImpl = new RoundedButtonImpl(this);
		initializeImpl(mImpl, context, attrs, defStyleAttr, defStyleRes);
		setViewBackground(this, mImpl);
	}

	void initializeImpl(RoundedButtonImpl impl, Context context, AttributeSet attrs,
	                    int defStyleAttr, int defStyleRes) {
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RoundedButton,
				defStyleAttr, defStyleRes);
		
		final int N = a.getIndexCount();
		for (int i = 0; i < N; i++) {
			int attr = a.getIndex(i);
			switch (attr) {
				case R.styleable.RoundedButton_qslib_buttonColor:
					impl.setColor(a.getColorStateList(attr));
					break;
				case R.styleable.RoundedButton_qslib_cornerRadius:
					impl.setCornerRadius(a.getDimension(attr, 0f));
					break;
				case R.styleable.RoundedButton_qslib_elevation:
					impl.setElevation(a.getDimension(attr, 0f));
					break;
				case R.styleable.RoundedButton_qslib_maxElevation:
					impl.setMaxElevation(a.getDimension(attr, 0f));
					break;
				case R.styleable.RoundedButton_qslib_pressedTranslationZ:
					impl.setPressedTranslationZ(a.getDimension(attr, 0f));
					break;
				case R.styleable.RoundedButton_qslib_preventCornerOverlap:
					impl.setPreventCornerOverlap(a.getBoolean(attr, false));
					break;
				case R.styleable.RoundedButton_qslib_translationZ:
					impl.setTranslationZ(a.getDimension(attr, 0f));
					break;
				case R.styleable.RoundedButton_qslib_useCompatAnimation:
					impl.setUseCompatAnimation(a.getBoolean(attr, false));
					break;
				case R.styleable.RoundedButton_qslib_useCompatPadding:
					impl.setUseCompatPadding(a.getBoolean(attr, false));
					break;
			}
		}

		// We'll always set contentPadding & drawablePadding
		int defaultContentPadding = a.getDimensionPixelOffset(
				R.styleable.RoundedButton_qslib_contentPadding, 0);
		Rect contentPadding = new Rect();
		contentPadding.left = a.getDimensionPixelOffset(
				R.styleable.RoundedButton_qslib_contentPaddingLeft, defaultContentPadding);
		contentPadding.top = a.getDimensionPixelOffset(
				R.styleable.RoundedButton_qslib_contentPaddingTop, defaultContentPadding);
		contentPadding.right = a.getDimensionPixelOffset(
				R.styleable.RoundedButton_qslib_contentPaddingRight, defaultContentPadding);
		contentPadding.bottom = a.getDimensionPixelOffset(
				R.styleable.RoundedButton_qslib_contentPaddingBottom, defaultContentPadding);
		impl.setContentPadding(contentPadding);

		int defaultInsetPadding = a.getDimensionPixelOffset(
				R.styleable.RoundedButton_qslib_insetPadding, 0);
		Rect insetPadding = new Rect();
		insetPadding.left = a.getDimensionPixelOffset(
				R.styleable.RoundedButton_qslib_insetPaddingLeft, defaultInsetPadding);
		insetPadding.top = a.getDimensionPixelOffset(
				R.styleable.RoundedButton_qslib_insetPaddingTop, defaultInsetPadding);
		insetPadding.right = a.getDimensionPixelOffset(
				R.styleable.RoundedButton_qslib_insetPaddingRight, defaultInsetPadding);
		insetPadding.bottom = a.getDimensionPixelOffset(
				R.styleable.RoundedButton_qslib_insetPaddingBottom, defaultInsetPadding);
		impl.setInsetPadding(insetPadding);

		a.recycle();
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

	public float getSupportElevation() {
		return mImpl.getElevation();
	}

	public void setSupportElevation(float elevation) {
		mImpl.setElevation(elevation);
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
		// TODO
	}

	@Override
	public void onPaddingChange(int left, int top, int right, int bottom,
	                            int horizontalShadowPadding, int verticalShadowPadding) {
		super.setPadding(left, top, right, bottom);
		setMinimumWidth(getSuggestedMinimumWidth() + horizontalShadowPadding);
		setMinimumHeight(getSuggestedMinimumHeight() + verticalShadowPadding);
	}
}
