package com.assistne.icondottextview;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
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
// TODO: 17/1/25 ripple效果
public class IconDotTextView extends View {
    private static final String TAG = "#IconDotTextView";
    private static final int ALIGN_TOP = 1;
    private static final int ALIGN_RIGHT = 2;
    private static final int ALIGN_BOTTOM = 4;
    private static final int ALIGN_LEFT = 8;
    private static final int DEFAULT_SPACING = 0;
    /**
     * the icon and the text will be ordered in a row and the icon comes first.
     * That means the icon will be left of the text.
     */
    public static final int ROW = 1;
    /**
     * the icon and the text will be ordered in a row and the text comes first.
     */
    public static final int ROW_REVERSE = 2;
    /**
     * the icon and the text will be ordered in a column and the icon comes first.
     * That means the icon will be above of the text.
     */
    public static final int COLUMN = 4;
    /**
     * the icon and the text will be ordered in a column and the text comes first.
     */
    public static final int COLUMN_REVERSE = 8;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({ROW, ROW_REVERSE, COLUMN, COLUMN_REVERSE})
    public @interface Direction {
    }

    public static final int POSITION_LEFT_TOP = 0;
    public static final int POSITION_LEFT_BOTTOM = 1;
    public static final int POSITION_RIGHT_TOP = 2;
    public static final int POSITION_RIGHT_BOTTOM = 3;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({POSITION_LEFT_TOP, POSITION_LEFT_BOTTOM, POSITION_RIGHT_TOP, POSITION_RIGHT_BOTTOM})
    public @interface DotPosition {
    }

    @DotPosition
    private static final int DEFAULT_DOT_POSITION = POSITION_RIGHT_TOP;
    private static final int DEFAULT_DOT_MARGIN = -15;

    @DotPosition
    int mDotPosition = DEFAULT_DOT_POSITION;
    private int mDotMarginTop;
    private int mDotMarginRight;
    private int mDotMarginBottom;
    private int mDotMarginLeft;

    private int mSpacing;
    @Direction
    private int mDirection;
    private boolean mDotAlignToIcon;
    private int mIconLeft;
    private int mIconTop;
    private boolean mIsDotVisible;
    private DotConfig mDotConfig;
    private IconConfig mIconConfig;
    private TextConfig mTextConfig;

    static int sDefaultIconSize;
    static int sDefaultDotSize;
    static int sDefaultDotTextSize;
    static int sDefaultTextSize;

    public IconDotTextView(Context context) {
        this(context, null);
    }

    public IconDotTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @SuppressWarnings("ResourceType")
    public IconDotTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        Resources resources = context.getResources();
        sDefaultIconSize = resources.getDimensionPixelSize(R.dimen.default_icon_size);
        sDefaultDotSize = resources.getDimensionPixelSize(R.dimen.default_dot_size);
        sDefaultDotTextSize = resources.getDimensionPixelSize(R.dimen.default_dot_text_size);
        sDefaultTextSize = resources.getDimensionPixelSize(R.dimen.default_text_size);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.IconDotTextView);
        mSpacing = typedArray.getDimensionPixelSize(R.styleable.IconDotTextView_spacing, DEFAULT_SPACING);
        @Direction int direction = typedArray.getInt(R.styleable.IconDotTextView_direction, COLUMN);
        mDirection = direction;
        mDotPosition = getPositionFromArray(typedArray);
        mDotAlignToIcon = typedArray.getBoolean(R.styleable.IconDotTextView_dot_alignToIcon, true);
        mDotMarginTop = typedArray.getDimensionPixelSize(R.styleable.IconDotTextView_dot_marginTop, DEFAULT_DOT_MARGIN);
        mDotMarginRight = typedArray.getDimensionPixelSize(R.styleable.IconDotTextView_dot_marginRight, DEFAULT_DOT_MARGIN);
        mDotMarginBottom = typedArray.getDimensionPixelSize(R.styleable.IconDotTextView_dot_marginBottom, DEFAULT_DOT_MARGIN);
        mDotMarginLeft = typedArray.getDimensionPixelSize(R.styleable.IconDotTextView_dot_marginLeft, DEFAULT_DOT_MARGIN);
        mIsDotVisible = typedArray.getBoolean(R.styleable.IconDotTextView_dot_visible, false);
        mDotConfig = new DotConfig(typedArray);
        mIconConfig = new IconConfig(context, typedArray);
        mTextConfig = new TextConfig(typedArray);
        typedArray.recycle();
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
            if (mDotAlignToIcon) {
                int delta = res / 2 - (mDotConfig.getWidth() + getDotHorizontalMargin() + mIconConfig.getWidth() / 2);
                if (delta < 0) {
                    res -= (delta * 2);
                }
            }
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

            if (mDotAlignToIcon && getDotVerticalMargin() < 0) {
                int delta = getPaddingTop() + getDotVerticalMargin();
                if (delta < 0) {
                    res -= (delta * 2);
                }
            }
        }

        return Math.max(getSuggestedMinimumHeight(), res);
    }

    private int getDotHorizontalMargin() {
        switch (mDotPosition) {
            case POSITION_LEFT_TOP:
            case POSITION_LEFT_BOTTOM:
                return mDotAlignToIcon ? mDotMarginRight : mDotMarginLeft;
            case POSITION_RIGHT_TOP:
            case POSITION_RIGHT_BOTTOM:
                return !mDotAlignToIcon ? mDotMarginRight : mDotMarginLeft;
        }
        return 0;
    }

    private int getDotVerticalMargin() {
        switch (mDotPosition) {
            case POSITION_LEFT_TOP:
            case POSITION_RIGHT_TOP:
                return mDotMarginTop;
            case POSITION_LEFT_BOTTOM:
            case POSITION_RIGHT_BOTTOM:
                return mDotMarginBottom;
        }
        return 0;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawIcon(canvas);
        drawText(canvas);
        if (mIsDotVisible) {
            drawDot(canvas);
        }
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
                tLeft = (width - mIconConfig.getWidth() - mSpacing - mTextConfig.getWidth()) / 2 + getPaddingLeft();
                break;
            case ROW_REVERSE:
                tTop = (height - mIconConfig.getHeight()) / 2 + getPaddingTop();
                tLeft = getMeasuredWidth() - (width - mIconConfig.getWidth() - mSpacing - mTextConfig.getWidth()) / 2 - mIconConfig.getWidth() - getPaddingRight();
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
                tLeft = getMeasuredWidth() - (width - mIconConfig.getWidth() - mSpacing - mTextConfig.getWidth()) / 2 - mTextConfig.getWidth() - getPaddingLeft();
                break;
            case ROW_REVERSE:
                tTop = (height - mTextConfig.getHeight()) / 2 + getPaddingTop();
                tLeft = (width - mIconConfig.getWidth() - mSpacing - mTextConfig.getWidth()) / 2 + getPaddingLeft();
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
                    tTop = mIconTop + mIconConfig.getHeight() - mDotConfig.getHeight() - mDotMarginBottom;
                    break;
                case POSITION_RIGHT_BOTTOM:
                    tLeft = mIconLeft + mIconConfig.getWidth() + mDotMarginLeft;
                    tTop = mIconTop + mIconConfig.getHeight() - mDotConfig.getHeight() - mDotMarginBottom;
                    break;
            }
        } else {
            switch (mDotPosition) {// 相对边界位置时margin不能为负
                case POSITION_LEFT_TOP:
                    tLeft = getPaddingLeft() + Math.max(0, mDotMarginLeft);
                    tTop = getPaddingTop() + Math.max(0, mDotMarginTop);
                    break;
                case POSITION_RIGHT_TOP:
                    tLeft = getWidth() - getPaddingRight() - Math.max(0, mDotMarginRight) - mDotConfig.getWidth();
                    tTop = getPaddingTop() + Math.max(0, mDotMarginTop);
                    break;
                case POSITION_LEFT_BOTTOM:
                    tLeft = getPaddingLeft() + Math.max(0, mDotMarginLeft);
                    tTop = getHeight() - getPaddingBottom() - Math.max(0, mDotMarginBottom) - mDotConfig.getHeight();
                    break;
                case POSITION_RIGHT_BOTTOM:
                    tLeft = getWidth() - getPaddingRight() - Math.max(0, mDotMarginRight) - mDotConfig.getWidth();
                    tTop = getHeight() - getPaddingBottom() - Math.max(0, mDotMarginBottom) - mDotConfig.getHeight();
                    break;
            }
        }
        canvas.save();
        canvas.translate(tLeft, tTop);
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

    /**
     * set the text of the view.
     *
     * @param res the string resource id.
     */
    public void setText(@StringRes int res) throws Resources.NotFoundException {
        setText(getContext().getResources().getString(res));
    }

    /**
     * set the text of the view.
     *
     * @param text the target text.
     */
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

    /**
     * set the color of the text.
     *
     * @param colorRes the color resource id.
     */
    public void setTextColor(@ColorRes int colorRes) {
        mTextConfig.setColor(getContext().getResources().getColor(colorRes));
        invalidate();
    }

    /**
     * set the text size of the text, interpreted as "scaled pixel" units.
     *
     * @param size The scaled pixel size.
     */
    public void setTextSize(float size) {
        setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
    }

    /**
     * set the text size of the text. See {@link
     * TypedValue} for the possible dimension units.
     *
     * @param unit The desired dimension unit.
     * @param size The desired size in the given units.
     */
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

    /**
     * set the icon of the view.
     *
     * @param drawableRes the drawable resource id.
     */
    public void setIcon(@DrawableRes int drawableRes) throws Resources.NotFoundException {
        if (getContext() != null) {
            setIcon(getContext().getResources().getDrawable(drawableRes));
        }
    }

    /**
     * set the icon of the view.
     *
     * @param drawable the drawable.
     */
    // TODO: 17/1/25 如果设置的图片大小变化同时没有指定Icon具体的大小, 此时需要考虑整个View的大小变化
    public void setIcon(@Nullable Drawable drawable) {
        if (drawable == null && mIconConfig.icon == null || drawable != null && !drawable.equals(mIconConfig.icon)) {
            mIconConfig.icon = drawable;
            invalidate();
        }
    }

    /**
     * set the width and height of the icon.
     *
     * @param width  the desired width of the icon.
     * @param height the desired height of the icon.
     */
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

    /**
     * set the direction of the view. Default is {@link #COLUMN}.
     *
     * @param direction one of the {@link #ROW}, {@link #ROW_REVERSE}, {@link #COLUMN}, {@link #COLUMN_REVERSE}
     */
    public void setDirection(@Direction int direction) {
        if (mDirection != direction) {
            mDirection = direction;
            requestLayout();
        }
    }

    /**
     * set the position of the dot. Default the dot is at the right-top position.
     *
     * @param dotPosition one of the {@link #POSITION_LEFT_TOP}, {@link #POSITION_RIGHT_TOP},
     *                    {@link #POSITION_RIGHT_BOTTOM}, {@link #POSITION_LEFT_BOTTOM}
     */
    public void setDotPosition(@DotPosition int dotPosition) {
        if (mDotPosition != dotPosition) {
            mDotPosition = dotPosition;
            invalidate();
        }
    }

    /**
     * if the dot align to icon or to the boarder of the view. Default the dot is align to the icon.
     *
     * @param alignToIcon true means align to the boarder of icon, false to the view.
     */
    public void setDotAlignToIcon(boolean alignToIcon) {
        if (mDotAlignToIcon != alignToIcon) {
            mDotAlignToIcon = alignToIcon;
            invalidate();
        }
    }

    /**
     * set the spacing between the icon and the text.
     *
     * @param spacing the desired spacing. In pixel.
     */
    public void setSpacing(int spacing) {
        if (mSpacing != spacing) {
            mSpacing = spacing;
            requestLayout();
        }
    }

    /**
     * set the margin around the dot. ONLY the margin between the dot and the boarder will work.
     *
     * @param left   the desired margin between the dot and the left boarder.
     * @param top    the desired margin between the dot and the top boarder.
     * @param right  the desired margin between the dot and the right boarder.
     * @param bottom the desired margin between the dot and the bottom boarder.
     */
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

    /**
     * set the size of the dot.
     *
     * @param size the desired size. In pixel.
     */
    public void setDotSize(int size) {
        if (mDotConfig.getSize() != size) {
            mDotConfig.setSize(size);
            invalidate();
        }
    }

    /**
     * set the color of the dot.
     *
     * @param color the desired color.
     */
    public void setDotColor(@ColorInt int color) {
        mDotConfig.setColor(color);
        invalidate();
    }

    /**
     * set the number in the dot.
     *
     * @param number the number showing in the dot.
     */
    public void setDotText(int number) {
        setDotText(String.valueOf(number));
    }

    /**
     * set the text in the dot.
     *
     * @param text the text showing in the dot. It will be added an ellipsis if the text
     *             is too long.
     */
    public void setDotText(CharSequence text) {
        if (mDotConfig.textConfig != null) {
            mDotConfig.textConfig.setText(text == null ? null : text.toString());
            invalidate();
        }
    }

    /**
     * set if the dot is visible. Default is not visible.
     *
     * @param visible true for visible.
     */
    public void setDotVisibility(boolean visible) {
        if (mIsDotVisible != visible) {
            mIsDotVisible = visible;
            invalidate();
        }
    }
}
