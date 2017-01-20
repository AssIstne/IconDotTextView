package com.assistne.icondottextview;

import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.ColorInt;

/**
 * Created by assistne on 17/1/20.
 */
// TODO: 17/1/20 BoringLayout计算文本长度
// TODO: 17/1/20 限制文本长度, 过长省略
// TODO: 17/1/20 文本改变的情况

public class TextConfig {

    private static final int DEFAULT_SIZE = 10;
    @ColorInt
    private static final int DEFAULT_COLOR = Color.RED;

    int size = DEFAULT_SIZE;
    @ColorInt int color = DEFAULT_COLOR;
    String text;

    public TextConfig(TypedArray typedArray) {
        if (typedArray != null) {
            size = typedArray.getDimensionPixelSize(R.styleable.IconDotTextView_dot_size, DEFAULT_SIZE);
            color = typedArray.getColor(R.styleable.IconDotTextView_dot_color, DEFAULT_COLOR);
            text = typedArray.getString(R.styleable.IconDotTextView_dot_text);
        }
    }

    public int getWidth() {
        return 100;
    }

    public int getHeight() {
        return 50;
    }
}
