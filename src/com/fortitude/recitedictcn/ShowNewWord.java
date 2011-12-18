/* -*- coding: utf-8 -*-
 *
 * Copyright (C) 2011-2011 fortitude.zhang
 * 
 * Author: fortitude.zhang@gmail.com
 */

package com.fortitude.recitedictcn;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.TextView;
import android.database.Cursor;
import android.content.Context;
import android.util.AttributeSet;
import android.graphics.Typeface;

import com.fortitude.recitedictcn.DataBase;
import com.fortitude.recitedictcn.MyTextView;

public class ShowNewWord extends Activity {    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        MyTextView wordView;
        MyTextView familiarView;
        MyTextView contentView;
        DataBase db;
        String word;
        String familiar;
        String content;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_new_word);

        String key = getIntent().getStringExtra("key");

        wordView = (MyTextView)findViewById(R.id.showNewWordName);
        familiarView = (MyTextView)findViewById(R.id.showNewWordFamiliar);
        contentView = (MyTextView)findViewById(R.id.showNewWordContent);

        db = new DataBase(this);
        db.open();

        Cursor c = db.getWord(key);
        if (null != c) {
            word = "单词   " + c.getString(0);
            familiar = "熟悉度  " + c.getString(1);
            content = "内容   " + c.getString(2);
 
            wordView.setText(word.subSequence(0, word.length()));
            familiarView.setText(familiar.subSequence(0, familiar.length()));
            contentView.setText(content.subSequence(0, content.length()));
        }
    
        db.close();
    }
}
