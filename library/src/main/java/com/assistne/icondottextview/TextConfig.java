package com.assistne.icondottextview;

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

/**
 * Created by assistne on 17/1/20.
 */
// TODO: 17/1/20 BoringLayout计算文本长度
// TODO: 17/1/20 限制文本长度, 过长省略
// TODO: 17/1/20 文本改变的情况

public class TextConfig implements Config {
    private static final String TAG = "#TextConfig";

    private static final int DEFAULT_SIZE = 36;
    @ColorInt
    private static final int DEFAULT_COLOR = Color.RED;
    private static final int DEFAULT_MAX_WIDTH = Integer.MAX_VALUE;

    int size = DEFAULT_SIZE;
    int maxWidth = DEFAULT_MAX_WIDTH;
    @ColorInt int color = DEFAULT_COLOR;
    String text;

    private TextPaint mTextPaint;
    private Layout mLayout;

    public TextConfig(TypedArray typedArray) {
        if (typedArray != null) {
            size = typedArray.getDimensionPixelSize(R.styleable.IconDotTextView_textSize, DEFAULT_SIZE);
            color = typedArray.getColor(R.styleable.IconDotTextView_textColor, DEFAULT_COLOR);
            text = typedArray.getString(R.styleable.IconDotTextView_text);
        }
        init();
    }

    public TextConfig(String text, int size, @ColorInt int color) {
        this.text = text;
        this.size = size;
        this.color = color;
        init();
    }

    private void init() {
        initPaint();
        initLayout();
    }

    private void initPaint() {
        mTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setColor(color);
        mTextPaint.setTextSize(size);
    }

    private void initLayout() {
        if (!TextUtils.isEmpty(text)) {
            int outerWidth = getWidth();
            mLayout = new StaticLayout(text, 0, text.length(),
                    mTextPaint, outerWidth, Layout.Alignment.ALIGN_CENTER,
                    1f, 0f, true,
                    TextUtils.TruncateAt.MIDDLE, outerWidth);
        }
    }


    public int getWidth() {
        if (!TextUtils.isEmpty(text)) {
            int desiredWidth = (int) Math.ceil(Layout.getDesiredWidth(text, mTextPaint));
            return Math.min(desiredWidth, maxWidth);
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
        if (!TextUtils.isEmpty(text)) {
            return (int) Math.ceil(Layout.getDesiredWidth(text, mTextPaint));
        } else {
            return 0;
        }
    }

    public int getHeight() {
        return mLayout == null ? 0 : mLayout.getHeight();
    }

    public void draw(@NonNull Canvas canvas) {
        if (mLayout != null) {
            mLayout.draw(canvas);
        }
    }

    public void setMaxWidth(int maxWidth) {
        this.maxWidth = maxWidth;
        mLayout = null;
        initLayout();
    }

    @Override
    public void setMaxHeight(int maxHeight) {

    }

    @Override
    public void setState(int[] state) {
    }
}
