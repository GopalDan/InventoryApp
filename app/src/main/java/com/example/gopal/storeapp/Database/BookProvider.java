package com.example.gopal.storeapp.Database;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.Toast;

/**
 * Created by Gopal on 11/8/2018.
 */
import com.example.gopal.storeapp.Database.BookContract.BookEntry;

public class BookProvider  extends ContentProvider{
    private BookDbHelper mDbHelper;
    private static final int PATH = 101;
    private static final int PATH_ID = 103;
    private static UriMatcher mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static
    {
        mUriMatcher.addURI(BookContract.CONTENT_AUTHORITY,BookContract.PATH,PATH);
        mUriMatcher.addURI  (BookContract.CONTENT_AUTHORITY,BookContract.PATH + "/#",PATH_ID );
    };

    @Override
    public boolean onCreate() {
        // creating DbHelper so that later SQLite object can be created
        mDbHelper = new BookDbHelper(getContext());
        return false;
    }

    @Nullable
    @Override
    public Cursor query( Uri uri,String[] projection, String selection, String[] selectionArgs, String sorBy) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Cursor cursor = null;
        int match = mUriMatcher.match(uri);
        switch(match){
            case PATH:
               cursor= db.query(BookEntry.TABLE_NAME,projection,selection,selectionArgs,null,null,null);
               break;
            case PATH_ID:
                selection = BookEntry._ID + "=?";
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};
                cursor = db.query(BookEntry.TABLE_NAME,
                        projection,selection,selectionArgs,null,null,null);
                break;
                default:
                    throw new IllegalArgumentException("Can't query unknown uri " + uri);

        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public Uri insert( Uri uri,  ContentValues contentValues) {
        int match = mUriMatcher.match(uri);
        switch (match){
            case PATH:
                return insertItem(uri, contentValues);
                default:
                    throw new IllegalArgumentException("Problem in insertion");
        }

    }
    public Uri insertItem(Uri uri, ContentValues values){
        //Sanity Checking for name
        String name = values.getAsString(BookEntry.COLUMN_PRODUCT_NAME);
        if(TextUtils.isEmpty(name)){
            Toast.makeText(getContext(),"Enter valid name", Toast.LENGTH_SHORT).show();
            return null;
        }
        //Sanity Checking for price
        String price = values.getAsString(BookEntry.COLUMN_PRODUCT_PRICE);
        if(TextUtils.isEmpty(price)){
            Toast.makeText(getContext(),"Enter price", Toast.LENGTH_SHORT).show();
            return null;
        }
        //Sanity Checking for quantity
        String quantity = values.getAsString(BookEntry.COLUMN_PRODUCT_QUANTITY);
        if(TextUtils.isEmpty(quantity)){
            Toast.makeText(getContext(),"Enter quantity", Toast.LENGTH_SHORT).show();
            return null;
        }
        //Sanity Checking for supplierName
        String supplierName = values.getAsString(BookEntry.COLUMN_SUPPLIER_NAME);
        if(TextUtils.isEmpty(supplierName)){
            Toast.makeText(getContext(),"Enter supplierName", Toast.LENGTH_SHORT).show();
            return null;
        }
        //Sanity Checking for phone number
        String phoneNumber = values.getAsString(BookEntry.COLUMN_SUPPLIER_PHONE_N0);
        if(TextUtils.isEmpty(phoneNumber)){
            Toast.makeText(getContext(),"Enter phone number", Toast.LENGTH_SHORT).show();
            return null;
        }
        else if(phoneNumber.trim().length()>10 || phoneNumber.trim().length()<10){
            Toast.makeText(getContext(),"Enter valid phone number", Toast.LENGTH_SHORT).show();
            return null;
        }
        // Inserting data into database
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        Long rowId;
        rowId = db.insert(BookEntry.TABLE_NAME,null,values);
        // notifyChange
        getContext().getContentResolver().notifyChange(uri,null);
        return ContentUris.withAppendedId(uri,rowId);
    }

    @Override
    public int delete(Uri uri, String selection,String[] selectionArgs) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        int rowsDeleted =0;
        int match = mUriMatcher.match(uri);
        switch (match){
            case PATH:
                rowsDeleted = db.delete(BookEntry.TABLE_NAME,selection,selectionArgs);
                break;
            case PATH_ID :
                selection = BookEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted = db.delete(BookEntry.TABLE_NAME, selection, selectionArgs);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri,ContentValues contentValues, String selection, String[] selectionArgs) {

        int match = mUriMatcher.match(uri);
        switch (match){
            case PATH:
                return updateItem(uri,contentValues,selection,selectionArgs);
            case PATH_ID :
                selection = BookEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updateItem(uri,contentValues,selection,selectionArgs);

            // why if remove default case gives error missing return statement
            default:
                throw new IllegalArgumentException("Problem in insertion");

        }
    }
    public int updateItem( Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        //Sanity Checking for name
        if (values.containsKey(BookEntry.COLUMN_PRODUCT_NAME)) {
            String name = values.getAsString(BookEntry.COLUMN_PRODUCT_NAME);
            if (TextUtils.isEmpty(name)) {
                Toast.makeText(getContext(), "Enter valid name", Toast.LENGTH_SHORT).show();
                return 0;
            }
        }
        //Sanity Checking for price
        if (values.containsKey(BookEntry.COLUMN_PRODUCT_PRICE)) {
            String price = values.getAsString(BookEntry.COLUMN_PRODUCT_PRICE);
            if (TextUtils.isEmpty(price)) {
                Toast.makeText(getContext(), "Enter price", Toast.LENGTH_SHORT).show();
                return 0;
            }
        }
        //Sanity Checking for quantity
        if (values.containsKey(BookEntry.COLUMN_PRODUCT_QUANTITY)) {
            String quantity = values.getAsString(BookEntry.COLUMN_PRODUCT_QUANTITY);
            if (TextUtils.isEmpty(quantity)) {
                Toast.makeText(getContext(), "Enter quantity", Toast.LENGTH_SHORT).show();
                return 0;
            }
        }
        //Sanity Checking for supplierName
        if (values.containsKey(BookEntry.COLUMN_SUPPLIER_NAME)) {
            String supplierName = values.getAsString(BookEntry.COLUMN_SUPPLIER_NAME);
            if (TextUtils.isEmpty(supplierName)) {
                Toast.makeText(getContext(), "Enter supplierName", Toast.LENGTH_SHORT).show();
                return 0;}
        }
        //Sanity Checking for phone number
        if(values.containsKey(BookEntry.COLUMN_SUPPLIER_PHONE_N0)) {
            String phoneNumber = values.getAsString(BookEntry.COLUMN_SUPPLIER_PHONE_N0);
        if (TextUtils.isEmpty(phoneNumber)) {
            Toast.makeText(getContext(), "Enter phone number", Toast.LENGTH_SHORT).show();
            return 0;
        } else if (phoneNumber.trim().length() > 10 || phoneNumber.trim().length() < 10) {
            Toast.makeText(getContext(), "Enter valid phone number", Toast.LENGTH_SHORT).show();
            return 0;
        }
    }

        // updating database
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        int rowsUpdated;
        rowsUpdated= db.update(BookEntry.TABLE_NAME, values, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsUpdated;

    }
    @Nullable
    @Override
    public String getType(Uri uri) {
        final int match = mUriMatcher.match(uri);
        switch (match) {
            case PATH:
                return BookEntry.CONTENT_LIST_TYPE;
            case PATH_ID:
                return BookEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }
}
