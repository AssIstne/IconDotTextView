package com.assistne.icondottextview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by assistne on 17/1/20.
 */

public class IconDotTextView extends View {
    private static final String TAG = "#IconDotTextView";
    private static final int DEFAULT_SPACING = 10;
    private static final int ROW = 1;
    private static final int ROW_REVERSE = 2;
    private static final int COLUMN = 4;
    private static final int COLUMN_REVERSE = 8;

    private int mSpacing;
    private int mDirection;
    private DotConfig mDotConfig;
    private IconConfig mIconConfig;
    private TextConfig mTextConfig;

    public IconDotTextView(Context context) {
        this(context, null);
    }

    public IconDotTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IconDotTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.IconDotTextView);
        mSpacing = typedArray.getDimensionPixelSize(R.styleable.IconDotTextView_spacing, DEFAULT_SPACING);
        mDirection = typedArray.getInt(R.styleable.IconDotTextView_direction, COLUMN);
        mDotConfig = new DotConfig(typedArray);
        mIconConfig = new IconConfig(typedArray);
        mTextConfig = new TextConfig(typedArray);
        typedArray.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        final int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        final int minWidth = getWidthAccordingContent();
        final int minHeight = getHeightAccordingContent();
        if (widthMode != MeasureSpec.EXACTLY) {
            widthMeasureSpec = minWidth;
        } else if (MeasureSpec.getSize(widthMeasureSpec) < MeasureSpec.getSize(minWidth)){
            throw new IllegalStateException("According to content, width of IconDotTextView should larger than "
                    + MeasureSpec.getSize(minWidth) + " px.");
        }

        if (heightMode != MeasureSpec.EXACTLY) {
            heightMeasureSpec = getHeightAccordingContent();
        } else if (MeasureSpec.getSize(heightMeasureSpec) < MeasureSpec.getSize(minHeight)){
            throw new IllegalStateException("According to content, height of IconDotTextView should larger than "
                    + MeasureSpec.getSize(minHeight)  + " px.");
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private int getWidthAccordingContent() {
        final int padding = getPaddingLeft() + getPaddingRight();
        switch (mDirection) {
            case ROW:
            case ROW_REVERSE:
                return MeasureSpec.makeMeasureSpec(
                    mIconConfig.getWidth() + mTextConfig.getWidth() + padding + mSpacing,
                    MeasureSpec.EXACTLY);
            default:
                int maxWidth = Math.max(mIconConfig.getWidth(), mTextConfig.getWidth());
                return MeasureSpec.makeMeasureSpec(maxWidth + padding, MeasureSpec.EXACTLY);
        }
    }

    private int getHeightAccordingContent() {
        final int padding = getPaddingTop() + getPaddingBottom();
        switch (mDirection) {
            case ROW:
            case ROW_REVERSE:
                int maxHeight = Math.max(mIconConfig.getHeight(), mTextConfig.getHeight());
                return MeasureSpec.makeMeasureSpec(maxHeight + padding, MeasureSpec.EXACTLY);
            default:
                return MeasureSpec.makeMeasureSpec(
                        mIconConfig.getHeight() + mTextConfig.getHeight() + padding + mSpacing,
                        MeasureSpec.EXACTLY);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
}
