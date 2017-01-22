package com.assistne.icondottextview;

import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.ColorInt;
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

    private static final int DEFAULT_SIZE = 36;
    @ColorInt
    private static final int DEFAULT_COLOR = Color.RED;
    private static final int DEAFULT_MAX_WIDTH = 400;

    int size = DEFAULT_SIZE;
    int maxWidth = DEAFULT_MAX_WIDTH;
    @ColorInt int color = DEFAULT_COLOR;
    String text;

    private TextPaint mTextPaint;
    private Layout mLayout;

    public TextConfig(TypedArray typedArray) {
        if (typedArray != null) {
            size = typedArray.getDimensionPixelSize(R.styleable.IconDotTextView_dot_size, DEFAULT_SIZE);
            color = typedArray.getColor(R.styleable.IconDotTextView_dot_color, DEFAULT_COLOR);
            text = typedArray.getString(R.styleable.IconDotTextView_dot_text);
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
            mLayout = new StaticLayout(text, mTextPaint, 200, Layout.Alignment.ALIGN_CENTER, 1f, 0f, true);
        }
    }


    public int getWidth() {
//        int desiredWidth = (int) Math.ceil(Layout.getDesiredWidth(text, mTextPaint));
//        return Math.min(desiredWidth, maxWidth);
        return 100;
    }

    public int getHeight() {
        return 50;
    }
}
