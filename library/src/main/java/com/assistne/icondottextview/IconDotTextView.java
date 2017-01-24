package com.assistne.icondottextview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.IntDef;
import android.util.AttributeSet;
import android.view.View;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

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

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({ROW, ROW_REVERSE, COLUMN, COLUMN_REVERSE})
    public @interface Direction{}

    private int mSpacing;
    @Direction
    private int mDirection;
    private DotConfig mDotConfig;
    private IconConfig mIconConfig;
    private TextConfig mTextConfig;

    private Paint mPaint;

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
        @Direction int direction = typedArray.getInt(R.styleable.IconDotTextView_direction, COLUMN);
        mDirection = direction;
        mDotConfig = new DotConfig(typedArray);
        mIconConfig = new IconConfig(context, typedArray);
        mTextConfig = new TextConfig(typedArray);
        typedArray.recycle();

        mPaint = new Paint();
        mPaint.setColor(Color.DKGRAY);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(4);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measureWidth(widthMeasureSpec), measureHeight(heightMeasureSpec));
    }

    private int measureWidth(int widthMeasureSpec) {
        final int mode = MeasureSpec.getMode(widthMeasureSpec);
        final int width = MeasureSpec.getSize(widthMeasureSpec);
        final int maxContentWidth = width - getPaddingLeft() - getPaddingRight();
        int contentWidth = maxContentWidth;
        int res = width;
        final int iconDesiredWidth = mIconConfig.getDesiredWidth();
        final int textDesiredWidth = mTextConfig.getDesiredWidth();
        if (mDirection == ROW || mDirection == ROW_REVERSE) {
            int desiredWidth = iconDesiredWidth + textDesiredWidth + mSpacing;
            switch (mode) {
                case MeasureSpec.AT_MOST:
                    mIconConfig.setMaxWidth(maxContentWidth - textDesiredWidth);
                    contentWidth = Math.min(maxContentWidth, desiredWidth);
                    break;
                case MeasureSpec.EXACTLY:
                    mIconConfig.setMaxHeight(maxContentWidth - textDesiredWidth);
                    contentWidth = maxContentWidth;
                    break;
                case MeasureSpec.UNSPECIFIED:
                    contentWidth = desiredWidth;
                    break;
            }
            res = contentWidth + getPaddingLeft() + getPaddingRight();
        } else if (mDirection == COLUMN || mDirection == COLUMN_REVERSE) {
            int desiredWidth = Math.max(iconDesiredWidth, textDesiredWidth);
            switch (mode) {
                case MeasureSpec.AT_MOST:
                    mTextConfig.setMaxWidth(maxContentWidth);
                    mIconConfig.setMaxWidth(maxContentWidth);
                    contentWidth = Math.min(desiredWidth, maxContentWidth);
                    break;
                case MeasureSpec.EXACTLY:
                    mTextConfig.setMaxWidth(maxContentWidth);
                    mIconConfig.setMaxWidth(maxContentWidth);
                    contentWidth = maxContentWidth;
                    break;
                case MeasureSpec.UNSPECIFIED:
                    contentWidth = desiredWidth;
                    break;
            }
            res = contentWidth + getPaddingLeft() + getPaddingRight();
        }

        return res;
    }

    private int measureHeight(int heightMeasureSpec) {
        final int mode = MeasureSpec.getMode(heightMeasureSpec);
        final int height = MeasureSpec.getSize(heightMeasureSpec);
        final int maxContentHeight = height - getPaddingTop() - getPaddingBottom();
        int contentHeight = maxContentHeight;
        int res = height;
        final int iconDesiredHeight = mIconConfig.getDesiredHeight();
        final int textDesiredHeight = mTextConfig.getDesiredHeight();
        if (mDirection == ROW || mDirection == ROW_REVERSE) {
            int desiredHeight = Math.max(iconDesiredHeight, textDesiredHeight);
            switch (mode) {
                case MeasureSpec.AT_MOST:
                    mIconConfig.setMaxHeight(maxContentHeight);
                    contentHeight = Math.min(desiredHeight, maxContentHeight);
                    break;
                case MeasureSpec.EXACTLY:
                    mTextConfig.setMaxHeight(maxContentHeight);
                    mIconConfig.setMaxHeight(maxContentHeight);
                    contentHeight = maxContentHeight;
                    break;
                case MeasureSpec.UNSPECIFIED:
                    contentHeight = desiredHeight;
                    break;
            }
            res = contentHeight + getPaddingTop() + getPaddingRight();
        } else if (mDirection == COLUMN || mDirection == COLUMN_REVERSE) {
            int desiredHeight = iconDesiredHeight + textDesiredHeight + mSpacing;
            switch (mode) {
                case MeasureSpec.AT_MOST:
                    mIconConfig.setMaxHeight(maxContentHeight - textDesiredHeight);
                    contentHeight = Math.min(maxContentHeight, desiredHeight);
                    break;
                case MeasureSpec.EXACTLY:
                    mIconConfig.setMaxHeight(maxContentHeight - textDesiredHeight);
                    contentHeight = maxContentHeight;
                    break;
                case MeasureSpec.UNSPECIFIED:
                    contentHeight = desiredHeight;
                    break;
            }

            res = contentHeight + getPaddingTop() + getPaddingBottom();
        }

        return res;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawIcon(canvas);
        drawText(canvas);
    }

    private void drawIcon(Canvas canvas) {
        final int contentHeight = mIconConfig.getHeight() + mSpacing + mTextConfig.getHeight();
        final int height = getMeasuredHeight() - getPaddingTop() - getPaddingBottom();
        final int width = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
        int tLeft = 0;
        int tTop = 0;
        switch (mDirection) {
            case ROW:
                tTop = (height - mIconConfig.getHeight()) / 2 + getPaddingTop();
                tLeft = (width - mIconConfig.getWidth() - mSpacing - mTextConfig.getWidth()) /2 + getPaddingLeft();
                break;
            case ROW_REVERSE:
                tTop = (height - mIconConfig.getHeight()) / 2 + getPaddingTop();
                tLeft = getMeasuredWidth() - (width - mIconConfig.getWidth() - mSpacing - mTextConfig.getWidth()) /2 - mIconConfig.getWidth() - getPaddingRight();
                break;
            case COLUMN:
                tTop = (height - contentHeight) / 2 + getPaddingTop();
                tLeft = (width - mIconConfig.getWidth()) / 2 + getPaddingLeft();
                break;
            case COLUMN_REVERSE:
                tTop = (height - contentHeight) / 2 + getPaddingTop() + mTextConfig.getHeight() + mSpacing;
                tLeft = (width - mIconConfig.getWidth()) / 2 + getPaddingLeft();
                break;
        }
        canvas.save();
        canvas.translate(tLeft, tTop);
        mIconConfig.draw(canvas);
        canvas.drawRect(0, 0, mIconConfig.getWidth(), mIconConfig.getHeight(), mPaint);
        canvas.restore();
    }

    private void drawText(Canvas canvas) {
        final int contentHeight = mIconConfig.getHeight() + mSpacing + mTextConfig.getHeight();
        final int height = getMeasuredHeight() - getPaddingTop() - getPaddingBottom();
        final int width = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
        int tLeft = 0;
        int tTop = 0;
        switch (mDirection) {
            case ROW:
                tTop = (height - mTextConfig.getHeight()) / 2 + getPaddingTop();
                tLeft = getMeasuredWidth() - (width - mIconConfig.getWidth() - mSpacing - mTextConfig.getWidth()) /2 - mTextConfig.getWidth() - getPaddingLeft();
                break;
            case ROW_REVERSE:
                tTop = (height - mTextConfig.getHeight()) / 2 + getPaddingTop();
                tLeft = (width - mIconConfig.getWidth() - mSpacing - mTextConfig.getWidth()) /2 + getPaddingLeft();
                break;
            case COLUMN:
                tTop = (height - contentHeight) / 2 + getPaddingTop() + mIconConfig.getHeight() + mSpacing;
                tLeft = (width - mTextConfig.getWidth()) / 2 + getPaddingLeft();
                break;
            case COLUMN_REVERSE:
                tTop = (height - contentHeight) / 2 + getPaddingTop();
                tLeft = (width - mTextConfig.getWidth()) / 2 + getPaddingLeft();
                break;
        }
        canvas.save();
        canvas.translate(tLeft, tTop);
        mTextConfig.draw(canvas);
        canvas.drawRect(0, 0, mTextConfig.getWidth(), mTextConfig.getHeight(), mPaint);
        canvas.restore();
    }

    @Override
    public void refreshDrawableState() {
        super.refreshDrawableState();
        int[] state = getDrawableState();
        mIconConfig.setState(state);
    }
}
