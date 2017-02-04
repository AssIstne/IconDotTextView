package com.assistne.icondottextview;

import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextDirectionHeuristics;
import android.text.TextPaint;
import android.text.TextUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;

/**
 * Created by assistne on 17/1/20.
 */
public class TextConfig implements Config {
    private static final String TAG = "#TextConfig";

    private static int DEFAULT_SIZE;
    @ColorInt
    private static final int DEFAULT_COLOR = Color.argb(137, 0, 0, 0);// #000000 54%
    private static final int DEFAULT_MAX_WIDTH = Integer.MAX_VALUE;

    private int mSize;
    private int mMaxWidth = DEFAULT_MAX_WIDTH;
    private ColorStateList mColorStateList;
    private String mText;

    private TextPaint mTextPaint;
    private Layout mLayout;

    public TextConfig(TypedArray typedArray) {
        DEFAULT_SIZE = IconDotTextView.sDefaultTextSize;
        if (typedArray != null) {
            mSize = typedArray.getDimensionPixelSize(R.styleable.IconDotTextView_textSize, DEFAULT_SIZE);
            mColorStateList = typedArray.getColorStateList(R.styleable.IconDotTextView_textColor);
            if (mColorStateList == null) {
                mColorStateList = ColorStateList.valueOf(DEFAULT_COLOR);
            }
            mText = typedArray.getString(R.styleable.IconDotTextView_text);
        }
        init();
    }

    public TextConfig(String text, int size, @ColorInt int color) {
        this.mText = text;
        this.mSize = size;
        this.mColorStateList = ColorStateList.valueOf(color);
        init();
    }

    private void init() {
        initPaint();
        initLayout();
    }

    private void initPaint() {
        mTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setColor(DEFAULT_COLOR);
        mTextPaint.setTextSize(mSize);
    }

    @SuppressWarnings("unchecked")
    private void initLayout() {
        if (!TextUtils.isEmpty(mText)) {
            final TextPaint paint = mTextPaint;
            final CharSequence source = mText;
            final int start = 0;
            final int end = mText.length();
            final Layout.Alignment alignment = Layout.Alignment.ALIGN_CENTER;
            final float spacingAdd = 0f;
            final float spacingMult = 1f;
            final boolean includePad = true;
            final TextUtils.TruncateAt truncateAt = TextUtils.TruncateAt.END;
            int outerWidth = getWidth();
            if (getDesiredWidth() <= outerWidth) {
                // 宽度足够容纳所有文字, 不需要设置行数限制
                mLayout = new StaticLayout(source, start, end, paint, outerWidth, alignment,
                        spacingMult, spacingAdd, includePad, truncateAt, outerWidth);
            } else {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    // 使用builder限制行数为1, 并且尾部省略
                    StaticLayout.Builder builder = StaticLayout.Builder.obtain(source,
                            start, end, paint, outerWidth)
                            .setAlignment(alignment)
                            .setLineSpacing(spacingAdd, spacingMult)
                            .setIncludePad(includePad)
                            .setMaxLines(1)
                            .setEllipsize(truncateAt);
                    mLayout = builder.build();
                } else {
                    // SDK23之前利用映射使用隐藏的构造函数设置最大行数
                    try {
                        Constructor<?>[] constructorArr = StaticLayout.class.getConstructors();
                        Constructor<StaticLayout> constructor = null;
                        if (constructorArr != null && constructorArr.length != 0) {
                            for (Constructor cons : constructorArr) {
                                Class[] params = cons.getParameterTypes();
                                if (params != null && params.length == 13) {// 参数数目为13的构造函数被隐藏
                                    constructor = cons;
                                    break;
                                }
                            }
                        }
                        if (constructor != null) {
                            constructor.setAccessible(true);
                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
                                mLayout = constructor.newInstance(source, start, end, paint, outerWidth, alignment,
                                        TextDirectionHeuristics.FIRSTSTRONG_LTR, spacingMult, spacingAdd,
                                        includePad, truncateAt, outerWidth, 1);
                            } else {
                                // SDK18之前, 类TextDirectionHeuristics被隐藏, 因此需要使用反射获取静态变量FIRSTSTRONG_LTR
                                Class<?> clazzTextDirectionHeuristics = Class.forName("android.text.TextDirectionHeuristics");
                                Field fieldLTR = clazzTextDirectionHeuristics.getField("FIRSTSTRONG_LTR");
                                fieldLTR.setAccessible(true);
                                mLayout = constructor.newInstance(source, start, end, paint, outerWidth, alignment,
                                        fieldLTR.get(null), spacingMult, spacingAdd,
                                        includePad, truncateAt, outerWidth, 1);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        if (mLayout == null) {
                            // 确保初始化完成
                            mLayout = new StaticLayout(source, start, end, paint, outerWidth, alignment,
                                    spacingMult, spacingAdd, includePad, truncateAt, outerWidth);
                        }
                    }
                }
            }
        } else {
            mLayout = null;
        }
    }


    public int getWidth() {
        if (!TextUtils.isEmpty(mText)) {
            int desiredWidth = (int) Math.ceil(Layout.getDesiredWidth(mText, mTextPaint));
            return Math.min(desiredWidth, mMaxWidth);
        } else {
            return 0;
        }
    }

    @Override
    public int getDesiredHeight() {
        return mLayout == null ? 0 : mLayout.getHeight();
    }

    @Override
    public int getDesiredWidth() {
        if (!TextUtils.isEmpty(mText)) {
            return (int) Math.ceil(Layout.getDesiredWidth(mText, mTextPaint));
        } else {
            return 0;
        }
    }

    public int getHeight() {
        return mLayout == null ? 0 : mLayout.getHeight();
    }

    public void draw(@NonNull Canvas canvas) {
        if (mLayout != null) {
            mTextPaint.setColor(mColorStateList.getColorForState(mTextPaint.drawableState, DEFAULT_COLOR));
            mLayout.draw(canvas);
        }
    }

    public void setMaxWidth(int maxWidth) {
        this.mMaxWidth = maxWidth;
        mLayout = null;
        initLayout();
    }

    @Override
    public void setMaxHeight(int maxHeight) {

    }

    @Override
    public boolean setState(int[] state) {
        int[] oldState = mTextPaint.drawableState;
        if (!Arrays.equals(oldState, state) &&
                mColorStateList.getColorForState(state, DEFAULT_COLOR) != mColorStateList.getColorForState(oldState, DEFAULT_COLOR)) {
            mTextPaint.drawableState = state;
            return true;
        } else {
            return false;
        }
    }

    public void setColor(@ColorInt int color) {
        mColorStateList = ColorStateList.valueOf(color);
    }

    public void setText(String text) {
        this.mText = text;
        mLayout = null;
        initLayout();
    }

    public String getText() {
        return mText;
    }

    public int getSize() {
        return mSize;
    }

    public void setSize(int size) {
        this.mSize = size;
        mTextPaint.setTextSize(size);
    }
}
