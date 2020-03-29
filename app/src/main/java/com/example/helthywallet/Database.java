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

    private final String DATABASE_NAME = "DatabaseDB";
    private final String DATABASE_TABLE = "DatabaseTable";
    private final int DATABASE_VERSION = 1;

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
            //CREATE TABLE DatabaseTable (_id INTEGER PRIMARY KEY AUTOINCREMENT,
            //                             category_name TEXT NOT NULL, _amount TEXT NOT NULL);
            String sqlCode = "CREATE TABLE " + DATABASE_TABLE + " (" +
                    KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    KEY_CATEGORY + " TEXT NOT NULL, " +
                    KEY_AMOUNT + " TEXT NOT NULL);";
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
    public long createEntry(String category, String amount){

        ContentValues cv = new ContentValues();
        cv.put(KEY_CATEGORY, category);
        cv.put(KEY_AMOUNT, amount);
        return ourDatabase.insert(DATABASE_TABLE, null, cv);
    }
    public String getData(){

        String [] columns = new String[] {KEY_ROWID, KEY_CATEGORY, KEY_AMOUNT};
        Cursor c = ourDatabase.query(DATABASE_TABLE, columns, null, null,null, null,null, null);
        String result = "";
        int iRowID = c.getColumnIndex(KEY_ROWID);
        int iCategory = c.getColumnIndex(KEY_CATEGORY);
        int iAmount = c.getColumnIndex(KEY_AMOUNT);

        for(c.moveToFirst();!c.isAfterLast();c.moveToNext()){

            result = result + c.getString(iRowID) + ": " + c.getString(iCategory) + ": " + c.getString(iAmount) + "\n";
        }
        c.close();
        return result;
    }
    public long deleteEntry(String rowId){

        return ourDatabase.delete(DATABASE_TABLE, KEY_ROWID + "=?", new String[]{rowId});
    }
    public long updateEntry(String rowId, String category, String amount){

        ContentValues cv = new ContentValues();
        cv.put(KEY_CATEGORY, category);
        cv.put(KEY_AMOUNT, amount);

        return  ourDatabase.update(DATABASE_TABLE, cv, KEY_ROWID + "=?", new String[]{rowId});
    }
}
