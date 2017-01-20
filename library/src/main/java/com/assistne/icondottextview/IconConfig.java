package com.assistne.icondottextview;

import android.content.res.TypedArray;
import android.support.annotation.DrawableRes;

/**
 * Created by assistne on 17/1/20.
 */
// TODO: 17/1/20 单张图片和图片selector
// TODO: 17/1/20 不指定尺寸使用图片自身的尺寸
// TODO: 17/1/20 改变了图片的情况
// TODO: 17/1/20 使用hint改变图片颜色

public class IconConfig {
    private static final int DEFAULT_SIZE = 10;

    private int size = DEFAULT_SIZE;
    private int width = DEFAULT_SIZE;
    private int height = DEFAULT_SIZE;
    @DrawableRes int res;

    public IconConfig(TypedArray typedArray) {
        if (typedArray != null) {
            width = typedArray.getDimensionPixelSize(R.styleable.IconDotTextView_icon_width, DEFAULT_SIZE);
            height = typedArray.getDimensionPixelSize(R.styleable.IconDotTextView_icon_height, DEFAULT_SIZE);
            if (!hasSpecifyWidthAndHeight()) {
                size = typedArray.getDimensionPixelSize(R.styleable.IconDotTextView_dot_size, DEFAULT_SIZE);
            }
            res = typedArray.getResourceId(R.styleable.IconDotTextView_dot_color, -1);
        }
    }

    private boolean hasSpecifyWidthAndHeight() {
        return width != DEFAULT_SIZE || height != DEFAULT_SIZE;
    }

    public int getWidth() {
        return width != DEFAULT_SIZE ? width : size;
    }

    public int getHeight() {
        return height != DEFAULT_SIZE ? height : size;
    }
}
