/* -*- coding: utf-8 -*-
 *
 * Copyright (C) 2011-2011 fortitude.zhang
 * 
 * Author: fortitude.zhang@gmail.com
 */

/* 数据库接口，实现DataBase相关API功能 */
package com.fortitude.recitedictcn;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DataBase {
	public static final String KEY_WORD = "newword";
	public static final String KEY_FAMILIAR = "familiarity";
	public static final String KEY_CONTENT = "definition";
	private static final String TAG = "DataBase";
	private static final String DATABASE_NAME = "recitedictcn_db";
	private static final String DATABASE_TABLE = "recitedictcn_table";
	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_CREATE = "create table recitedictcn_table (newword TEXT PRIMARY KEY, "
        + "familiarity INTEGER, "
        + "definition TEXT not null);";
	private final Context context;
	private DataBasehelper dbHelper;
	private SQLiteDatabase db;
	
	public DataBase(Context ctx)
	{
		this.context = ctx;
		dbHelper = new DataBasehelper(context);
	}
	
	private static class DataBasehelper extends SQLiteOpenHelper
	{
		DataBasehelper(Context context)
		{
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}
		
		@Override
		public void onCreate(SQLiteDatabase db)
		{
			db.execSQL(DATABASE_CREATE);
		}
		
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
		{
			Log.w(TAG, "Upgrading DataBase from version " + oldVersion
                  + " to "
                  + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS recitedictcn_table");
            onCreate(db);
		}
	}
	

	/* 打开数据库*/
	public DataBase open() throws SQLException
	{
		db = dbHelper.getWritableDatabase();		
		return this;
	}
	
	/* 关闭数据库 */
	public void close()
	{
		dbHelper.close();
	}
	
	/* 插入新的单词 */
	public long insertWord(String word, Integer familiar, String content)
	{		
		ContentValues initialValues = new ContentValues();

		initialValues.put(KEY_WORD, word);
		initialValues.put(KEY_FAMILIAR, familiar);
		initialValues.put(KEY_CONTENT, content);

		return db.insert(DATABASE_TABLE, null, initialValues);
	}


	/* 删除旧的单词 */
    public boolean deleteWord(String word)
    {
        String[] whereValue = {word};
        try {
            db.delete(DATABASE_TABLE, KEY_WORD + "=?", whereValue);
        } catch (Exception e) {
            //do nothing currently
        }

        return true;
    }

    /* 删除所有的单词记录 */
    public boolean deleteAllWord()
    {
        try {
            db.delete(DATABASE_TABLE, "1", null);
        } catch (Exception e) {
            //do nothing currently
        }

        return true;
    }

    /* 取得所有单词 */
    public Cursor getAllWords()
    {
        return db.query(DATABASE_TABLE, 
                        new String[] {KEY_WORD, KEY_FAMILIAR, KEY_CONTENT},
                        null,
                        null,
                        null,
                        null,
                        KEY_FAMILIAR);
    }


    /* 取得指定单词 */
    public Cursor getWord(String word) throws SQLException
    {
        Cursor mCursor = db.query(true, 
                                  DATABASE_TABLE, 
                                  new String[] {KEY_WORD, KEY_FAMILIAR, KEY_CONTENT},
                                  KEY_WORD + "=" + "\"" + word + "\"",
                                  null,
                                  null,
                                  null,
                                  null,
                                  null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
         
        return mCursor;
    }

    /* 更新一个标题 */
    public boolean updateWord(String word, Integer familiar)
    {
        ContentValues initialValues = new ContentValues();
        String[] whereValue = {word};
        
        initialValues.put(KEY_WORD, word);
        initialValues.put(KEY_FAMILIAR, familiar);

        return db.update(DATABASE_TABLE, initialValues, KEY_WORD + "=?", whereValue) > 0;
    }
}
