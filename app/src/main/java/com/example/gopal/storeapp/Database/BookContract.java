package com.example.gopal.storeapp.Database;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Gopal on 11/8/2018.
 */

public final class BookContract {
    public static final String CONTENT_AUTHORITY = "com.example.gopal.storeapp";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH = "bookStore";



    public static class BookEntry implements BaseColumns {

        public static final String TABLE_NAME = "bookStore";

        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_PRODUCT_NAME = "productName";
        public static final String COLUMN_PRODUCT_PRICE = "productPrice";
        public static final String COLUMN_PRODUCT_QUANTITY = "productQuantity";
        public static final String COLUMN_SUPPLIER_NAME = "supplierName";
        public static final String COLUMN_SUPPLIER_PHONE_N0 = "supplierPhoneNo";
        // Content uri
         public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI,PATH);

        /**
         * The MIME type of the {@link #CONTENT_URI} for a list of items.
         */
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH;

        /**
         * The MIME type of the {@link #CONTENT_URI} for a single item.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH;


    }
}