package com.assistne.icondottextview;

import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by assistne on 17/1/20.
 */
// TODO: 17/1/20 文本长度
// TODO: 17/1/20 区分有文字和无文字
// TODO: 17/1/20 文本属性抽象出来, 一起使用TextConfig
public class DotConfig {
    private static final int ALIGN_TOP = 1;
    private static final int ALIGN_RIGHT = 2;
    private static final int ALIGN_BOTTOM = 4;
    private static final int ALIGN_LEFT = 8;

    public static final int POSITION_LEFT_TOP = 0;
    public static final int POSITION_LEFT_BOTTOM = 1;
    public static final int POSITION_RIGHT_TOP = 2;
    public static final int POSITION_RIGHT_BOTTOM = 3;
    @Retention(RetentionPolicy.SOURCE)
    @IntDef({POSITION_LEFT_TOP, POSITION_LEFT_BOTTOM, POSITION_RIGHT_TOP, POSITION_RIGHT_BOTTOM})
    public @interface DotPosition {}

    private static final int DEFAULT_SIZE = 10;
    @ColorInt private static final int DEFAULT_COLOR = Color.RED;
    @DotPosition private static final int DEFAULT_POSITION = POSITION_RIGHT_TOP;
    private static final int DEFAULT_MARGIN = 10;
    private static final int DEFAULT_TEXT_SIZE = 16;
    @ColorInt private static final int DEFAULT_TEXT_COLOR = Color.WHITE;

    int size = DEFAULT_SIZE;
    @ColorInt int color = DEFAULT_COLOR;
    @DotPosition int position = DEFAULT_POSITION;
    int marginTop = DEFAULT_MARGIN;
    int marginRight = DEFAULT_MARGIN;
    int marginBottom = DEFAULT_MARGIN;
    int marginLeft = DEFAULT_MARGIN;

    String text;
    int textSize = DEFAULT_TEXT_SIZE;
    @ColorInt int textColor = DEFAULT_TEXT_COLOR;

    public DotConfig(TypedArray typedArray) {
        if (typedArray != null) {
            size = typedArray.getDimensionPixelSize(R.styleable.IconDotTextView_dot_size, DEFAULT_SIZE);
            color = typedArray.getColor(R.styleable.IconDotTextView_dot_color, DEFAULT_COLOR);
            position = getPositionFromArray(typedArray);
            marginTop = typedArray.getDimensionPixelSize(R.styleable.IconDotTextView_dot_marginTop, DEFAULT_MARGIN);
            marginRight = typedArray.getDimensionPixelSize(R.styleable.IconDotTextView_dot_marginRight, DEFAULT_MARGIN);
            marginBottom = typedArray.getDimensionPixelSize(R.styleable.IconDotTextView_dot_marginBottom, DEFAULT_MARGIN);
            marginLeft = typedArray.getDimensionPixelSize(R.styleable.IconDotTextView_dot_marginLeft, DEFAULT_MARGIN);
            text = typedArray.getString(R.styleable.IconDotTextView_dot_text);
            textSize = typedArray.getDimensionPixelSize(R.styleable.IconDotTextView_dot_textSize, DEFAULT_TEXT_SIZE);
            textColor = typedArray.getColor(R.styleable.IconDotTextView_dot_textColor, DEFAULT_TEXT_COLOR);
        }
    }

    @DotConfig.DotPosition
    private int getPositionFromArray(TypedArray typedArray) {
        int alignInfo = typedArray.getInt(R.styleable.IconDotTextView_dot_alignTo, ALIGN_TOP | ALIGN_RIGHT);
        if ((alignInfo & ALIGN_TOP) != 0) {
            if ((alignInfo & ALIGN_RIGHT) != 0) {
                return POSITION_RIGHT_TOP;
            } else {
                return POSITION_LEFT_TOP;
            }
        } else {
            if ((alignInfo & ALIGN_RIGHT) != 0) {
                return POSITION_RIGHT_BOTTOM;
            } else {
                return POSITION_LEFT_BOTTOM;
            }
        }
    }
}
