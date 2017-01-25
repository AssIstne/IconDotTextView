package com.assistne.icondottextview;

import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;

/**
 * Created by assistne on 17/1/20.
 */
// TODO: 17/1/20 文本长度
// TODO: 17/1/20 区分有文字和无文字
// TODO: 17/1/24 文本不能超出dot范围
public class DotConfig implements Config {
    private static final int DEFAULT_SIZE = 10;
    @ColorInt private static final int DEFAULT_COLOR = Color.RED;
    private static final int DEFAULT_TEXT_SIZE = 16;
    @ColorInt private static final int DEFAULT_TEXT_COLOR = Color.WHITE;

    int size = DEFAULT_SIZE;
    @ColorInt int color = DEFAULT_COLOR;

    TextConfig textConfig;

    public DotConfig(TypedArray typedArray) {
        if (typedArray != null) {
            size = typedArray.getDimensionPixelSize(R.styleable.IconDotTextView_dot_size, DEFAULT_SIZE);
            color = typedArray.getColor(R.styleable.IconDotTextView_dot_color, DEFAULT_COLOR);
            String text = typedArray.getString(R.styleable.IconDotTextView_dot_text);
            int textSize = typedArray.getDimensionPixelSize(R.styleable.IconDotTextView_dot_textSize, DEFAULT_TEXT_SIZE);
            int textColor = typedArray.getColor(R.styleable.IconDotTextView_dot_textColor, DEFAULT_TEXT_COLOR);
            textConfig = new TextConfig(text, textSize, textColor);
        }
    }

    @Override
    public int getHeight() {
        return getDesiredHeight();
    }

    @Override
    public int getWidth() {
        return getDesiredHeight();
    }

    @Override
    public int getDesiredHeight() {
        return 30;
    }

    @Override
    public int getDesiredWidth() {
        return 30;
    }

    @Override
    public void setMaxWidth(int maxWidth) {

    }

    @Override
    public void setMaxHeight(int maxHeight) {

    }

    // TODO: 17/1/25 状态没改变时返回false
    @Override
    public boolean setState(int[] state) {
        return false;
    }

    @Override
    public void draw(@NonNull Canvas canvas) {

    }
}
