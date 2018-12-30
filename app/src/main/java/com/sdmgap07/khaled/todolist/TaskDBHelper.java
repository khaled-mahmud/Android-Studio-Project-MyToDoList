package com.sdmgap07.khaled.todolist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TaskDBHelper extends SQLiteOpenHelper
{

    public static final String DATABASE_NAME = "ToDoDBHelper.db";
    public static final String CONTACTS_TABLE_NAME = "todo";


    public TaskDBHelper(Context context) { super(context, DATABASE_NAME, null, 1); }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase)
    {

        sqLiteDatabase.execSQL(
                "CREATE TABLE "+CONTACTS_TABLE_NAME +
                        "(id INTEGER PRIMARY KEY, task TEXT, dateStr INTEGER)"
        );

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion)
    {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+CONTACTS_TABLE_NAME);
        onCreate(sqLiteDatabase);

    }

    private long getDate(String day)
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        Date date = new Date();
        try{
           date = dateFormat.parse(day);
        }catch (ParseException e){

        }
        return date.getTime();
    }

    public boolean insertContact(String task, String dateStr)
    {
        Date date;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("task", task);
        contentValues.put("dateStr", getDate(dateStr));
        db.insert(CONTACTS_TABLE_NAME, null, contentValues);
        return true;
    }

    public boolean updateContact(String id, String task, String dateStr)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("task", task);
        contentValues.put("dateStr", getDate(dateStr));
        db.update(CONTACTS_TABLE_NAME, contentValues, "id = ?", new String[]{id});
        return true;
    }

    public boolean deleteContact(String id)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(CONTACTS_TABLE_NAME, "id = ?", new String[]{id});
        return true;
    }

    public Cursor getData()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result = db.rawQuery("select * from " + CONTACTS_TABLE_NAME + " order by id desc", null);
        return result;
    }

    public Cursor getDataSpecific(String id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result = db.rawQuery("select * from " + CONTACTS_TABLE_NAME + " WHERE id = '" + id + "' order by id desc", null);
        return result;
    }

    public Cursor getDataToday()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result = db.rawQuery("select * from " + CONTACTS_TABLE_NAME +
                " WHERE date(datetime(dateStr / 1000 , 'unixepoch', 'localtime')) = date('now', 'localtime') order by id desc", null);
        return result;
    }

    public Cursor getDataTomorrow()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor result =  db.rawQuery("select * from "+CONTACTS_TABLE_NAME+
                " WHERE date(datetime(dateStr / 1000 , 'unixepoch', 'localtime')) = date('now', '+1 day', 'localtime')  order by id desc", null);
        return result;
    }

    public Cursor getDataUpcoming()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor result =  db.rawQuery("select * from "+CONTACTS_TABLE_NAME+
                " WHERE date(datetime(dateStr / 1000 , 'unixepoch', 'localtime')) > date('now', '+1 day', 'localtime') order by id desc", null);
        return result;

    }
}
