package com.assistne.icondottextview;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by assistne on 17/1/20.
 */
// TODO: 17/1/24 大小不规范时的处理, 保证text的显示?
// TODO: 17/1/24 对外接口, 代码改变状态
// TODO: 17/1/25 ripple效果
public class IconDotTextView extends View {
    private static final String TAG = "#IconDotTextView";
    private static final int ALIGN_TOP = 1;
    private static final int ALIGN_RIGHT = 2;
    private static final int ALIGN_BOTTOM = 4;
    private static final int ALIGN_LEFT = 8;
    private static final int DEFAULT_SPACING = 10;
    public static final int ROW = 1;
    public static final int ROW_REVERSE = 2;
    public static final int COLUMN = 4;
    public static final int COLUMN_REVERSE = 8;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({ROW, ROW_REVERSE, COLUMN, COLUMN_REVERSE})
    public @interface Direction{}

    public static final int POSITION_LEFT_TOP = 0;
    public static final int POSITION_LEFT_BOTTOM = 1;
    public static final int POSITION_RIGHT_TOP = 2;
    public static final int POSITION_RIGHT_BOTTOM = 3;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({POSITION_LEFT_TOP, POSITION_LEFT_BOTTOM, POSITION_RIGHT_TOP, POSITION_RIGHT_BOTTOM})
    public @interface DotPosition {}
    @DotPosition private static final int DEFAULT_DOT_POSITION = POSITION_RIGHT_TOP;
    private static final int DEFAULT_DOT_MARGIN = 0;

    @DotPosition int mDotPosition = DEFAULT_DOT_POSITION;
    private int mDotMarginTop = DEFAULT_DOT_MARGIN;
    private int mDotMarginRight = DEFAULT_DOT_MARGIN;
    private int mDotMarginBottom = DEFAULT_DOT_MARGIN;
    private int mDotMarginLeft = DEFAULT_DOT_MARGIN;

    private int mSpacing;
    @Direction
    private int mDirection;
    private boolean mDotAlignToIcon = false;
    private int mIconLeft;
    private int mIconTop;
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
        mDotPosition = getPositionFromArray(typedArray);
        mDotAlignToIcon = typedArray.getBoolean(R.styleable.IconDotTextView_dot_alignToIcon, false);
        mDotMarginTop = typedArray.getDimensionPixelSize(R.styleable.IconDotTextView_dot_marginTop, DEFAULT_DOT_MARGIN);
        mDotMarginRight = typedArray.getDimensionPixelSize(R.styleable.IconDotTextView_dot_marginRight, DEFAULT_DOT_MARGIN);
        mDotMarginBottom = typedArray.getDimensionPixelSize(R.styleable.IconDotTextView_dot_marginBottom, DEFAULT_DOT_MARGIN);
        mDotMarginLeft = typedArray.getDimensionPixelSize(R.styleable.IconDotTextView_dot_marginLeft, DEFAULT_DOT_MARGIN);
        mDotConfig = new DotConfig(typedArray);
        mIconConfig = new IconConfig(context, typedArray);
        mTextConfig = new TextConfig(typedArray);
        typedArray.recycle();

        mPaint = new Paint();
        mPaint.setColor(Color.DKGRAY);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(4);
    }

    @DotPosition
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
                    mIconConfig.setMaxWidth(maxContentWidth - textDesiredWidth);
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

        return Math.max(getSuggestedMinimumWidth(), res);
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

        return Math.max(getSuggestedMinimumHeight(), res);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawIcon(canvas);
        drawText(canvas);
        drawDot(canvas);
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
        mIconLeft = tLeft;
        mIconTop = tTop;
        canvas.save();
        canvas.translate(tLeft, tTop);
        mIconConfig.draw(canvas);
        canvas.drawRect(0, 0, mIconConfig.getWidth(), mIconConfig.getHeight(), mPaint);
        canvas.restore();
    }

    private void drawText(Canvas canvas) {
        mTextConfig.setState(getDrawableState());
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

    private void drawDot(Canvas canvas) {
        int tLeft = 0;
        int tTop = 0;
        if (mDotAlignToIcon) {
            switch (mDotPosition) {
                case POSITION_LEFT_TOP:
                    tLeft = mIconLeft - mDotMarginRight - mDotConfig.getWidth();
                    tTop = mIconTop + mDotMarginTop;
                    break;
                case POSITION_RIGHT_TOP:
                    tLeft = mIconLeft + mIconConfig.getWidth() + mDotMarginLeft;
                    tTop = mIconTop + mDotMarginTop;
                    break;
                case POSITION_LEFT_BOTTOM:
                    tLeft = mIconLeft - mDotMarginRight - mDotConfig.getWidth();
                    tTop = mIconTop + mIconConfig.getHeight() - mDotMarginBottom - mIconConfig.getHeight();
                    break;
                case POSITION_RIGHT_BOTTOM:
                    tLeft = mIconLeft + mIconConfig.getWidth() + mDotMarginLeft;
                    tTop = mIconTop + mIconConfig.getHeight() - mDotMarginBottom - mIconConfig.getHeight();
                    break;
            }
        } else {
            switch (mDotPosition) {
                case POSITION_LEFT_TOP:
                    tLeft = getPaddingLeft() + mDotMarginLeft;
                    tTop = getPaddingTop() + mDotMarginTop;
                    break;
                case POSITION_RIGHT_TOP:
                    tLeft = getWidth() - getPaddingRight() - mDotMarginRight - mDotConfig.getWidth();
                    tTop = getPaddingTop() + mDotMarginTop;
                    break;
                case POSITION_LEFT_BOTTOM:
                    tLeft = getPaddingLeft() + mDotMarginLeft;
                    tTop = getHeight() - getPaddingBottom() - mDotMarginBottom - mDotConfig.getHeight();
                    break;
                case POSITION_RIGHT_BOTTOM:
                    tLeft = getWidth() - getPaddingRight() - mDotMarginRight - mDotConfig.getWidth();
                    tTop = getHeight() - getPaddingBottom() - mDotMarginBottom - mDotConfig.getHeight();
                    break;
            }
        }
        canvas.save();
        canvas.translate(tLeft, tTop);
        canvas.drawRect(0, 0, mDotConfig.getWidth(), mDotConfig.getHeight(), mPaint);
        mDotConfig.draw(canvas);
        canvas.restore();
    }
    
    @Override
    protected void drawableStateChanged() {
        int[] state = getDrawableState();
        boolean changed = mIconConfig.setState(state);
        changed |= mTextConfig.setState(state);
        changed |= mDotConfig.setState(state);
        super.drawableStateChanged();
        if (changed) {
            invalidate();
        }
    }

    public void setText(@StringRes int res) {
        setText(getContext().getResources().getString(res));
    }

    public void setText(@Nullable CharSequence text) {
        String oldText = mTextConfig.getText();
        if (TextUtils.isEmpty(text) && !TextUtils.isEmpty(oldText) || !TextUtils.isEmpty(text) && !text.equals(mTextConfig.getText())) {
            int oldDesiredWidth = mTextConfig.getDesiredWidth();
            mTextConfig.setText(TextUtils.isEmpty(text) ? null : text.toString());
            if (oldDesiredWidth != mTextConfig.getDesiredWidth()) {
                requestLayout();
            } else {
                invalidate();
            }
        }
    }

    public void setTextColor(@ColorRes int colorRes) {
        mTextConfig.setColor(getContext().getResources().getColor(colorRes));
        invalidate();
    }

    public void setTextSize(float size) {
        setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
    }

    public void setTextSize(int unit, float size) {
        int newSize = (int) getRawTextSize(unit, size);
        if (mTextConfig.getSize() != newSize) {
            mTextConfig.setSize(newSize);
            requestLayout();
        }
    }

    private float getRawTextSize(int unit, float size) {
        Context context = getContext();
        Resources resources = context == null ? Resources.getSystem() : context.getResources();
        return TypedValue.applyDimension(unit, size, resources.getDisplayMetrics());
    }

    public void setIcon(@DrawableRes int drawableRes) throws Resources.NotFoundException {
        if (getContext() != null) {
            setIcon(getContext().getResources().getDrawable(drawableRes));
        }
    }

    // TODO: 17/1/25 如果设置的图片大小变化同时没有指定Icon具体的大小, 此时需要考虑整个View的大小变化
    public void setIcon(@Nullable Drawable drawable) {
        if (drawable == null && mIconConfig.icon == null || drawable != null && !drawable.equals(mIconConfig.icon)) {
            mIconConfig.icon = drawable;
            invalidate();
        }
    }

    public void setIconSize(int width, int height) {
        boolean needRefresh = false;
        if (mIconConfig.width != width) {
            mIconConfig.width = width;
            needRefresh = true;
        }
        if (mIconConfig.height != height) {
            mIconConfig.height = height;
            needRefresh = true;
        }

        if (needRefresh) {
            requestLayout();
        }
    }

    public void setDirection(@Direction int direction) {
        if (mDirection != direction) {
            mDirection = direction;
            requestLayout();
        }
    }

    public void setDotPosition(@DotPosition int dotPosition) {
        if (mDotPosition != dotPosition) {
            mDotPosition = dotPosition;
            invalidate();
        }
    }

    public void setDotAlignToIcon(boolean alignToIcon) {
        if (mDotAlignToIcon != alignToIcon) {
            mDotAlignToIcon = alignToIcon;
            invalidate();
        }
    }

    public void setSpacing(int spacing) {
        if (mSpacing != spacing) {
            mSpacing = spacing;
            requestLayout();
        }
    }

    public void setDotMargin(int left, int top, int right, int bottom) {
        boolean needRefresh = false;
        if (mDotMarginLeft != left) {
            mDotMarginLeft = left;
            needRefresh = true;
        }
        if (mDotMarginTop != top) {
            mDotMarginTop = top;
            needRefresh = true;
        }
        if (mDotMarginRight != right) {
            mDotMarginRight = right;
            needRefresh = true;
        }
        if (mDotMarginBottom != bottom) {
            mDotMarginBottom = bottom;
            needRefresh = true;
        }

        if (needRefresh) {
            invalidate();
        }
    }

    public void setDotSize(int size) {
        if (mDotConfig.getSize() != size) {
            mDotConfig.setSize(size);
            invalidate();
        }
    }

    public void setDotColor(@ColorInt int color) {
        mDotConfig.setColor(color);
        invalidate();
    }

    public void setDotText(int number) {
        setDotText(String.valueOf(number));
    }

    public void setDotText(CharSequence text) {
        if (mDotConfig.textConfig != null) {
            mDotConfig.textConfig.setText(text == null ? null : text.toString());
            invalidate();
        }
    }
}
