package com.assistne.icondottextview;

import android.graphics.Canvas;
import android.support.annotation.NonNull;

/**
 * Created by assistne on 17/1/22.
 */

public interface Config {
    int getHeight();

    int getWidth();

    int getDesiredHeight();

    int getDesiredWidth();

    void setMaxWidth(int maxWidth);

    void setMaxHeight(int maxHeight);

    boolean setState(int[] state);

    void draw(@NonNull Canvas canvas);
}
