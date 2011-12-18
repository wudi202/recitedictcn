/* -*- coding: utf-8 -*-
 *
 * Copyright (C) 2011-2011 fortitude.zhang
 * 
 * Author: fortitude.zhang@gmail.com
 */

package com.fortitude.recitedictcn;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/* text view 封装，避免每次设置typeface */
public class MyTextView extends TextView {
    private void setFont(Context context) {
        Typeface tf = Typeface.createFromAsset(context.getAssets(), "font/segoeui.ttf");   
        setTypeface(tf);
    }

    public MyTextView(Context context) {
        super(context);
        setFont(context);
    }

    public MyTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFont(context);
    }

    public MyTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setFont(context);
    }
}
