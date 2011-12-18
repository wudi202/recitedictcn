/* -*- coding: utf-8 -*-
 *
 * Copyright (C) 2011-2011 fortitude.zhang
 * 
 * Author: fortitude.zhang@gmail.com
 */

/* 基本界面显示activity，用于显示BANNER以及字典 */
package com.fortitude.recitedictcn;

import java.util.List;
import java.util.ArrayList;
import android.app.Activity;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
import android.database.Cursor;
import android.util.Log;
import android.content.Intent;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnCreateContextMenuListener;
import android.view.View.OnLongClickListener;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;

import com.fortitude.recitedictcn.DataBase;
import com.fortitude.recitedictcn.NewWord;

public class MainDisplay extends Activity {
    ListView innerLv;
    DataBase db;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        /* 获取list view */
        innerLv = (ListView)findViewById(R.id.MainDisplayList);

        /* 注册context menu,并设置listener */
        registerForContextMenu(innerLv);

        innerLv.setOnLongClickListener(new OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Toast t = Toast.makeText(getApplicationContext(), 
                                             "关于这个软件嘛，没有啥好说的，目前仅为张东亚和老婆使用。", 
                                             Toast.LENGTH_SHORT);
                    return true;
                }
            });

        /* 暂时不用listener这种方式创建context menu */
        // innerLv.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {
        //         @Override
        //         public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        //             MenuInflater inflater = getMenuInflater();
        //             inflater.inflate(R.menu.maindisplay_context_menu, menu);
        //         }
        // });

        /* 注册listview的touch处理函数 */
        //todo，弹出菜单，进行后续处理
        /* innerLv.setOnItemLongClickListener(     
           int selectedPosition = adapterView.getSelectedItemPosition(); 
           ShowAlert(String.valueOf(selectedPosition)); 
           }; */

        /* 创建一个db */
        db = new DataBase(this);
        db.open();

        /* 调用函数，将单词数据添加到列表中 */
        refreshListView();
    }

    /* 本函数用于将数据库中的表更新到LISTVIEW上 */
    public void refreshListView() {
        Cursor c = db.getAllWords();
        List<String> data = new ArrayList<String>();

        while (c.moveToNext()) {
            String str = c.getString(0) + "熟悉度***-" + c.getString(1);
            data.add(str);
        }           

        innerLv.setAdapter(new ArrayAdapter<String>(this, R.layout.list_view_item, data));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.maindisplay_option_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.addNewword: {
            /* 启动添加新词的activity */
            Intent i = new Intent(this, NewWord.class);
            startActivityForResult(i, 0x1985);

            return true;                
        }
        case R.id.about: {
            Toast t = Toast.makeText(this, "关于这个软件嘛，没有啥好说的，目前仅为张东亚和老婆使用。", Toast.LENGTH_SHORT);
            t.show();
            return true;                    
        }
        case R.id.cleanDatabase: {
            Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("危险---清空数据库！！！");
            builder.setMessage("请根据实际情况慎重选择是否！！！");
            builder.setCancelable(true);
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        db.deleteAllWord();
                        refreshListView();
                    }
                });

            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });   
            builder.create().show();
            return true;                    
        }
        default:
            return false;
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.maindisplay_context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.showNewWord: {
            String selected = (String)innerLv.getSelectedItem();
            if (null != selected) {
                int indexCN = selected.indexOf("熟悉度");
                String key = selected.substring(0, indexCN);

                /* 启动显示新词的activity */
                Intent i = new Intent(this, ShowNewWord.class);
                i.putExtra("key", key);
                startActivityForResult(i, 0x1986);
                return true;                
            }

            return false;
        }
        case R.id.deleteFamiliarWord: {
            String selected = (String)innerLv.getSelectedItem();
            if (null != selected) {
                int indexCN = selected.indexOf("熟悉度");
                String key = selected.substring(0, indexCN);
                db.deleteWord(key);
                refreshListView();
            }

            return true;                    
        }
        case R.id.incFamiliarity: {
            String selected = (String)innerLv.getSelectedItem();
            if (null != selected) {
                int indexCN = selected.indexOf("熟悉度");
                int indexFamiliarity = selected.indexOf("-");
                String key = selected.substring(0, indexCN);
                String familiarity = selected.substring(indexFamiliarity + 1);

                int newFamiliarity = Integer.parseInt(familiarity);
                if (5 >= newFamiliarity + 1) {
                    db.updateWord(key, newFamiliarity + 1);
                    refreshListView();
                }
            }

            return true;                    
        }
        case R.id.decFamiliarity: {
            String selected = (String)innerLv.getSelectedItem();
            if (null != selected) {
                int indexCN = selected.indexOf("熟悉度");
                int indexFamiliarity = selected.indexOf("-");
                String key = selected.substring(0, indexCN);
                String familiarity = selected.substring(indexFamiliarity + 1);

                int newFamiliarity = Integer.parseInt(familiarity);
                if (1 <= newFamiliarity - 1) {
                    db.updateWord(key, newFamiliarity - 1);
                    refreshListView();
                }
            }

            return true;                    
        }
        default:
            return false;
        }
    }

    protected void onActivityResult(int requestCode, int resultCode,Intent data) {
        if (requestCode == 0x1985) {
            if (resultCode == 0) {
                /* 更新list，待补充完整 */
                refreshListView();
            } else {
                /* 啥也不做 */
            }
        }
    }
}
