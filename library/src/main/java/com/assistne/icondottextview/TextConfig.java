package com.assistne.icondottextview;

import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;

import java.util.Arrays;

/**
 * Created by assistne on 17/1/20.
 */
// TODO: 17/1/20 限制文本长度, 过长省略
public class TextConfig implements Config {
    private static final String TAG = "#TextConfig";

    private static final int DEFAULT_SIZE = 36;
    @ColorInt
    private static final int DEFAULT_COLOR = Color.argb(137, 0, 0, 0);// #000000 54%
    private static final int DEFAULT_MAX_WIDTH = Integer.MAX_VALUE;

    private int mSize = DEFAULT_SIZE;
    private int mMaxWidth = DEFAULT_MAX_WIDTH;
    private ColorStateList mColorStateList;
    private String mText;

    private TextPaint mTextPaint;
    private Layout mLayout;

    public TextConfig(TypedArray typedArray) {
        if (typedArray != null) {
            mSize = typedArray.getDimensionPixelSize(R.styleable.IconDotTextView_textSize, DEFAULT_SIZE);
            mColorStateList = typedArray.getColorStateList(R.styleable.IconDotTextView_textColor);
            if (mColorStateList == null) {
                mColorStateList = ColorStateList.valueOf(DEFAULT_COLOR);
            }
            mText = typedArray.getString(R.styleable.IconDotTextView_text);
        }
        init();
    }

    public TextConfig(String text, int size, @ColorInt int color) {
        this.mText = text;
        this.mSize = size;
        this.mColorStateList = ColorStateList.valueOf(color);
        init();
    }

    private void init() {
        initPaint();
        initLayout();
    }

    private void initPaint() {
        mTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setColor(DEFAULT_COLOR);
        mTextPaint.setTextSize(mSize);
    }

    private void initLayout() {
        if (!TextUtils.isEmpty(mText)) {
            int outerWidth = getWidth();
            mLayout = new StaticLayout(mText, 0, mText.length(),
                    mTextPaint, outerWidth, Layout.Alignment.ALIGN_CENTER,
                    1f, 0f, true,
                    TextUtils.TruncateAt.MIDDLE, outerWidth);
        } else {
            mLayout = null;
        }
    }


    public int getWidth() {
        if (!TextUtils.isEmpty(mText)) {
            int desiredWidth = (int) Math.ceil(Layout.getDesiredWidth(mText, mTextPaint));
            return Math.min(desiredWidth, mMaxWidth);
        } else {
            return 0;
        }
    }

    @Override
    public int getDesiredHeight() {
        return mLayout == null ? 0 : mLayout.getHeight();
    }

    @Override
    public int getDesiredWidth() {
        if (!TextUtils.isEmpty(mText)) {
            return (int) Math.ceil(Layout.getDesiredWidth(mText, mTextPaint));
        } else {
            return 0;
        }
    }

    public int getHeight() {
        return mLayout == null ? 0 : mLayout.getHeight();
    }

    public void draw(@NonNull Canvas canvas) {
        if (mLayout != null) {
            mTextPaint.setColor(mColorStateList.getColorForState(mTextPaint.drawableState, DEFAULT_COLOR));
            mLayout.draw(canvas);
        }
    }

    public void setMaxWidth(int maxWidth) {
        this.mMaxWidth = maxWidth;
        mLayout = null;
        initLayout();
    }

    @Override
    public void setMaxHeight(int maxHeight) {

    }

    @Override
    public boolean setState(int[] state) {
        int[] oldState = mTextPaint.drawableState;
        if (!Arrays.equals(oldState, state) &&
                mColorStateList.getColorForState(state, DEFAULT_COLOR) != mColorStateList.getColorForState(oldState, DEFAULT_COLOR)) {
            mTextPaint.drawableState = state;
            return true;
        } else {
            return false;
        }
    }

    public void setColor(@ColorInt int color) {
        mColorStateList = ColorStateList.valueOf(color);
    }

    public void setText(String text) {
        this.mText = text;
        mLayout = null;
        initLayout();
    }

    public String getText() {
        return mText;
    }

    public int getSize() {
        return mSize;
    }

    public void setSize(int size) {
        this.mSize = size;
        mTextPaint.setTextSize(size);
    }
}
