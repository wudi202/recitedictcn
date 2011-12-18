/* -*- coding: utf-8 -*-
 *
 * Copyright (C) 2011-2011 fortitude.zhang
 * 
 * Author: fortitude.zhang@gmail.com
 */

/* 本文件用于维护添加新词的activity */
package com.fortitude.recitedictcn;

import java.util.List;
import java.util.ArrayList;
import java.net.URL;
import android.app.Activity;
import android.os.Bundle;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import android.database.Cursor;
import android.util.Log;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.parsers.SAXParser;
import org.xml.sax.SAXException;
import org.xml.sax.Parser;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.XMLReader;
import org.xml.sax.InputSource;

import com.fortitude.recitedictcn.DataBase;
import com.fortitude.recitedictcn.DictcnXMLHandler;

public class NewWord extends Activity {
    DataBase db;
    EditText wordTv;
    EditText familiarTv;

    private OnClickListener buttonListener = new OnClickListener() {
            public void onClick(View v) {
                String word = wordTv.getText().toString();
                String familiar = familiarTv.getText().toString();

                Integer nFamiliar = Integer.parseInt(familiar);
                if ((1 > nFamiliar) || (5 < nFamiliar)) {
                    Toast t = Toast.makeText(NewWord.this, "兄弟，不带你这么玩的，熟悉度只支持1~5", Toast.LENGTH_SHORT);
                    t.show();
                    return;
                }

                try {
                    /* 测试XML读取 */
                    URL url = new URL("http://dict.cn/ws.php?utf8=true&q=" + word);

                    /* Get a SAXParser from the SAXPArserFactory. */
                    SAXParserFactory spf = SAXParserFactory.newInstance();
                    SAXParser sp = spf.newSAXParser();

                    /* Get the XMLReader of the SAXParser we created. */
                    XMLReader xr = sp.getXMLReader();
                    /* Create a new ContentHandler and apply it to the XML-Reader*/
                    DictcnXMLHandler myHandler = new DictcnXMLHandler();
                    xr.setContentHandler(myHandler);

                    /* Parse the xml-data from our URL. */
                    xr.parse(new InputSource(url.openStream()));
                    /* Parsing has finished. */ 

                    /* 取出解析结果 */
                    String text = myHandler.getWordContent();

                    /* 将结果存入db */
                    db.insertWord(word, nFamiliar, text);

                    /* 设置activity结果，返回主activity */
                    NewWord.this.setResult(0);

                    db.close();

                    finish();
                } catch (Exception e) {
                    Toast t = Toast.makeText(NewWord.this, "处理XML出现错误，请重试，错误信息：" + e.getMessage(), Toast.LENGTH_SHORT);
                    t.show();
                    Log.e("NewWordActivity", "XMLParse error", e);

                    /* 设置activity结果，返回主activity */
                    NewWord.this.setResult(0);
                    finish();
                }
            }
        };

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_word);

        /* 创建database实例，并打开数据库，等待用户查询结果写入*/
        db = new DataBase(this);
        db.open();

        Button button = (Button)findViewById(R.id.AddNewButton);
        button.setOnClickListener(buttonListener);

        wordTv = (EditText)findViewById(R.id.WordNameInput);
        familiarTv = (EditText)findViewById(R.id.WordFamiliarityInput);
    }


}
