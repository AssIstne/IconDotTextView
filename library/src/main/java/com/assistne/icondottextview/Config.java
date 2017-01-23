package com.assistne.icondottextview;

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
}
