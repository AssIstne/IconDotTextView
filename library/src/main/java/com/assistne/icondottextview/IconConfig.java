package com.assistne.icondottextview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by assistne on 17/1/20.
 */
// TODO: 17/1/20 使用hint改变图片颜色

public class IconConfig implements Config {
    private static final String TAG = "#IconConfig";
    private static int DEFAULT_SIZE;

    private int size;
    int width;
    int height;
    @Nullable
    Drawable icon;
    private int maxWidth = Integer.MAX_VALUE;
    private int maxHeight = Integer.MAX_VALUE;

    public IconConfig(@NonNull Context context, TypedArray typedArray) {
        DEFAULT_SIZE = IconDotTextView.sDefaultIconSize;
        if (typedArray != null) {
            width = typedArray.getDimensionPixelSize(R.styleable.IconDotTextView_icon_width, DEFAULT_SIZE);
            height = typedArray.getDimensionPixelSize(R.styleable.IconDotTextView_icon_height, DEFAULT_SIZE);
            if (!hasSpecifyWidthAndHeight()) {
                size = typedArray.getDimensionPixelSize(R.styleable.IconDotTextView_icon_size, DEFAULT_SIZE);
            }
            int res = typedArray.getResourceId(R.styleable.IconDotTextView_icon, -1);
            if (res != -1) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    icon = context.getDrawable(res);
                } else {
                    icon = context.getResources().getDrawable(res);
                }
            }
        }
    }

    private boolean hasSpecifyWidthAndHeight() {
        return width != DEFAULT_SIZE || height != DEFAULT_SIZE;
    }

    public int getWidth() {
        return Math.min(maxWidth, getDesiredWidth());
    }

    public int getHeight() {
        return Math.min(maxHeight, getDesiredHeight());
    }

    @Override
    public int getDesiredWidth() {
        if (icon != null) {
            if (width != DEFAULT_SIZE) {
                return width;
            } else if (size != DEFAULT_SIZE) {
                return size;
            } else if (icon.getIntrinsicWidth() != -1) {
                return icon.getIntrinsicWidth();
            } else {
                return DEFAULT_SIZE;
            }
        } else {
            return 0;
        }
    }

    @Override
    public int getDesiredHeight() {
        if (icon != null) {
            if (height != DEFAULT_SIZE) {
                return height;
            } else if (size != DEFAULT_SIZE) {
                return size;
            } else if (icon.getIntrinsicHeight() != -1) {
                return icon.getIntrinsicHeight();
            } else {
                return DEFAULT_SIZE;
            }
        } else {
            return 0;
        }
    }

    @Override
    public void setMaxWidth(int maxWidth) {
        this.maxWidth = maxWidth;
    }

    @Override
    public void setMaxHeight(int maxHeight) {
        this.maxHeight = maxHeight;
    }

    @Override
    public boolean setState(int[] state) {
        return icon != null && icon.setState(state);
    }

    public void draw(@NonNull Canvas canvas) {
        if (icon != null) {
            icon.setBounds(0, 0, getWidth(), getHeight());
            icon.draw(canvas);
        }
    }
}
