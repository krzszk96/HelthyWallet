package com.example.helthywallet;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.ContactsContract;
import android.view.View;

public class Database {

    public static final String KEY_ROWID = "_id";
    public static final String KEY_CATEGORY = "category_name";
    public static final String KEY_AMOUNT = "_amount";
    public static final String KEY_TITLE = "_title";
    public static final String KEY_DATE = "_date";

    private final String DATABASE_NAME = "DatabaseDB";
    private final String DATABASE_TABLE = "DatabaseTable";
    private final int DATABASE_VERSION = 3;

    private DBHelper ourHelper;
    private final Context ourContext;
    private SQLiteDatabase ourDatabase;

    public Database(Context context){
        ourContext = context;
    }
    private class DBHelper extends SQLiteOpenHelper{

        public DBHelper (Context context){
            super(context, DATABASE_NAME, null, DATABASE_VERSION); //construct new database
        }
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
            onCreate(db);
        }
        @Override
        public void onCreate(SQLiteDatabase db) {

            String sqlCode = "CREATE TABLE " + DATABASE_TABLE + " (" +
                    KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    KEY_TITLE + " TEXT NOT NULL, " +
                    KEY_CATEGORY + " TEXT NOT NULL, " +
                    KEY_AMOUNT + " TEXT NOT NULL, " +
                    KEY_DATE + " TEXT NOT NULL);";
            db.execSQL(sqlCode);
        }
    }
    public Database open() throws SQLException{

        ourHelper = new DBHelper(ourContext);
        ourDatabase = ourHelper.getWritableDatabase();
        return this;
    }
    public void close(){

        ourHelper.close();
    }
    public long createEntry(String title, String amount,String date, String category){

        ContentValues cv = new ContentValues();
        cv.put(KEY_TITLE, title);
        cv.put(KEY_AMOUNT, amount);
        cv.put(KEY_DATE, date);
        cv.put(KEY_CATEGORY, category);
        return ourDatabase.insert(DATABASE_TABLE, null, cv);
    }
    public String getData(){

        String [] columns = new String[] {KEY_ROWID, KEY_TITLE, KEY_CATEGORY, KEY_AMOUNT, KEY_DATE};
        Cursor c = ourDatabase.query(DATABASE_TABLE, columns, null, null,null, null,null, null);
        String result = "";
        int iRowID = c.getColumnIndex(KEY_ROWID);
        int iTitle = c.getColumnIndex(KEY_TITLE);
        int iDate = c.getColumnIndex(KEY_DATE);
        int iAmount = c.getColumnIndex(KEY_AMOUNT);
        int iCategory = c.getColumnIndex(KEY_CATEGORY);

        for(c.moveToFirst();!c.isAfterLast();c.moveToNext()){

            result = result + c.getString(iRowID) + ": " + c.getString(iTitle) + ": " + c.getString(iCategory) + ": " + c.getString(iAmount) + ": " + c.getString(iDate) + "\n";
        }
        c.close();
        return result;
    }
    public long deleteEntry(String rowId){

        return ourDatabase.delete(DATABASE_TABLE, KEY_ROWID + "=?", new String[]{rowId});
    }
    public long updateEntry(String rowId,String title, String amount, String date, String category){

        ContentValues cv = new ContentValues();
        cv.put(KEY_TITLE, title);
        cv.put(KEY_AMOUNT, amount);
        cv.put(KEY_DATE, date);
        cv.put(KEY_CATEGORY, category);

        return  ourDatabase.update(DATABASE_TABLE, cv, KEY_ROWID + "=?", new String[]{rowId});
    }
}
