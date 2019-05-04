package com.example.gopal.storeapp.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.gopal.storeapp.Database.BookContract.BookEntry;

/**
 * Created by Gopal on 11/8/2018.
 */

public class BookDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "bookStore.db";
    private static final int DATABASE_VERSION = 1;

    public BookDbHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_TABLE = "CREATE TABLE " + BookEntry.TABLE_NAME + "(" +
                BookEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                BookEntry.COLUMN_PRODUCT_NAME + " TEXT NOT NULL," +
                BookEntry.COLUMN_PRODUCT_PRICE + " INTEGER NOT NULL," +
                BookEntry.COLUMN_PRODUCT_QUANTITY + " INTEGER NOT NULL," +
                BookEntry.COLUMN_SUPPLIER_NAME + " TEXT," +
                BookEntry.COLUMN_SUPPLIER_PHONE_N0 + " TEXT )";
        sqLiteDatabase.execSQL(CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
