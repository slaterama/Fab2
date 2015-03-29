package com.slaterama.fab2.widget.roundedbutton;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.Button;

import com.slaterama.fab2.R;

import static com.slaterama.fab2.widget.roundedbutton.RoundedButtonImpl.RoundedButtonDelegate;
import static com.slaterama.fab2.widget.roundedbutton.RoundedButtonImpl.RoundedButtonAttributes;
import static com.slaterama.fab2.widget.roundedbutton.RoundedButtonImpl.calculateOverlayPadding;
import static com.slaterama.fab2.widget.roundedbutton.RoundedButtonImpl.getResolvedSize;

public class RoundedButton extends Button
		implements RoundedButtonDelegate {

	RoundedButtonImpl mImpl;

	final Rect mContentPadding = new Rect();

	float mCornerRadius;

	boolean mPreventCornerOverlap;

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
		RoundedButtonAttributes attributes = newOptions(context, attrs, defStyleAttr, defStyleRes);
		mImpl = RoundedButtonImpl.newInstance(this, attributes);
		mContentPadding.set(attributes.contentPadding);
		mCornerRadius = attributes.cornerRadius;
		mPreventCornerOverlap = attributes.preventCornerOverlap;
		mImpl.invalidatePadding();
	}

	RoundedButtonAttributes newOptions(Context context, AttributeSet attrs, int defStyleAttr,
	                                int defStyleRes) {
		RoundedButtonAttributes attributes = new RoundedButtonAttributes();
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RoundedButton,
				defStyleAttr, defStyleRes);
		attributes.color = a.getColorStateList(R.styleable.RoundedButton_qslib_buttonColor);
		int defaultContentPadding = a.getDimensionPixelOffset(
				R.styleable.RoundedButton_qslib_contentPadding, 0);
		attributes.contentPadding.left = a.getDimensionPixelOffset(
				R.styleable.RoundedButton_qslib_contentPaddingLeft, defaultContentPadding);
		attributes.contentPadding.top = a.getDimensionPixelOffset(
				R.styleable.RoundedButton_qslib_contentPaddingTop, defaultContentPadding);
		attributes.contentPadding.right = a.getDimensionPixelOffset(
				R.styleable.RoundedButton_qslib_contentPaddingRight, defaultContentPadding);
		attributes.contentPadding.bottom = a.getDimensionPixelOffset(
				R.styleable.RoundedButton_qslib_contentPaddingBottom, defaultContentPadding);
		attributes.cornerRadius = a.getDimension(R.styleable.RoundedButton_qslib_cornerRadius,
				attributes.cornerRadius);
		attributes.elevation = a.getDimension(R.styleable.RoundedButton_qslib_elevation,
				attributes.elevation);
		int defaultInsetPadding = a.getDimensionPixelOffset(
				R.styleable.RoundedButton_qslib_insetPadding, 0);
		attributes.insetPadding.left = a.getDimensionPixelOffset(
				R.styleable.RoundedButton_qslib_insetPaddingLeft, defaultInsetPadding);
		attributes.insetPadding.top = a.getDimensionPixelOffset(
				R.styleable.RoundedButton_qslib_insetPaddingTop, defaultInsetPadding);
		attributes.insetPadding.right = a.getDimensionPixelOffset(
				R.styleable.RoundedButton_qslib_insetPaddingRight, defaultInsetPadding);
		attributes.insetPadding.bottom = a.getDimensionPixelOffset(
				R.styleable.RoundedButton_qslib_insetPaddingBottom, defaultInsetPadding);
		attributes.maxElevation = a.getDimension(R.styleable.RoundedButton_qslib_maxElevation,
				attributes.maxElevation);
		attributes.pressedTranslationZ = a.getDimension(
				R.styleable.RoundedButton_qslib_pressedTranslationZ, attributes.pressedTranslationZ);
		attributes.preventCornerOverlap = a.getBoolean(
				R.styleable.RoundedButton_qslib_preventCornerOverlap, attributes.preventCornerOverlap);
		attributes.translationZ = a.getDimension(R.styleable.RoundedButton_qslib_translationZ,
				attributes.translationZ);
		attributes.useCompatAnimation = a.getBoolean(
				R.styleable.RoundedButton_qslib_useCompatAnimation, attributes.useCompatAnimation);
		attributes.useCompatPadding = a.getBoolean(
				R.styleable.RoundedButton_qslib_useCompatPadding, attributes.useCompatPadding);
		a.recycle();
		return attributes;
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
		return mContentPadding.left;
	}

	public int getContentPaddingTop() {
		return mContentPadding.top;
	}

	public int getContentPaddingRight() {
		return mContentPadding.right;
	}

	public int getContentPaddingBottom() {
		return mContentPadding.bottom;
	}

	public void setContentPadding(Rect contentPadding) {
		if (contentPadding.left != mContentPadding.left || contentPadding.top != mContentPadding.top
				|| contentPadding.right != mContentPadding.right
				|| contentPadding.bottom != mContentPadding.bottom) {
			mContentPadding.set(contentPadding);
			mImpl.invalidatePadding();
		}
	}

	public float getCornerRadius() {
		return mImpl.getCornerRadius();
	}

	public void setCornerRadius(float cornerRadius) {
		mImpl.setCornerRadius(cornerRadius);
		if (cornerRadius != mCornerRadius) {
			mCornerRadius = cornerRadius;
			if (mPreventCornerOverlap) {
				mImpl.invalidatePadding();
			}
		}
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
		return mPreventCornerOverlap;
	}

	public void setPreventCornerOverlap(boolean preventCornerOverlap) {
		if (preventCornerOverlap != mPreventCornerOverlap) {
			mPreventCornerOverlap = preventCornerOverlap;
			mImpl.invalidatePadding();
		}
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
		setMeasuredDimension(mResolvedSize.x, mResolvedSize.y);
	}

	@Override
	public void onPaddingChanged(int left, int top, int right, int bottom) {
		mDrawablePadding.set(left, top, right, bottom);
		int overlayPadding = calculateOverlayPadding(mPreventCornerOverlap, mCornerRadius);
		super.setPadding(
				left + overlayPadding + mContentPadding.left,
				top + overlayPadding + mContentPadding.top,
				right + overlayPadding + mContentPadding.right,
				bottom + overlayPadding + mContentPadding.bottom);
		requestLayout();
	}
}
