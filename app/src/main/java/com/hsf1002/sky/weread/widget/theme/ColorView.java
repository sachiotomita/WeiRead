package com.hsf1002.sky.weread.widget.theme;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by hefeng on 18-5-10.
 */

public class ColorView extends View implements ColorUiInterface {
    private int attr_background = -1;

    public ColorView(Context context) {
        super(context);
    }

    public ColorView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        attr_background = ViewAttributeUtil.getBackgroundAttibute(attrs);
    }

    public ColorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        attr_background = ViewAttributeUtil.getBackgroundAttibute(attrs);
    }

    public ColorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public View getView() {
        return this;
    }

    @Override
    public void setTheme(Resources.Theme themeId) {
        if (attr_background != -1)
        {
            ViewAttributeUtil.applyBackgroundDrawable(this, themeId, attr_background);
        }
    }
}
