package com.example.gopal.storeapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gopal.storeapp.Database.BookContract.BookEntry;

/**
 * Created by Gopal on 11/8/2018.
 */

public class BookAdapter extends CursorAdapter{

    public BookAdapter(Context context, Cursor c){
        super(context,c,false);

    }
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parents) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, parents,false);
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        //Getting the view
        TextView nameTextView = view.findViewById(R.id.product_name);
        TextView priceTextView = view.findViewById(R.id.price);
        final TextView quantityTextView = view.findViewById(R.id.quantity);
        Button sailButton = view.findViewById(R.id.sale);

        // setting value in views
        int nameColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_PRODUCT_NAME);
        int priceColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_PRODUCT_PRICE);
        int quantityColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_PRODUCT_QUANTITY);
        int idColumnIndex = cursor.getColumnIndex(BookEntry._ID);

        String name = cursor.getString(nameColumnIndex);
        String price = cursor.getString(priceColumnIndex);
        final String mQuantity = cursor.getString(quantityColumnIndex);
        final String  rowId = cursor.getString(idColumnIndex);

        nameTextView.setText(name);
        priceTextView.setText(price);
        quantityTextView.setText( "Qty: " + mQuantity);

         sailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int updateQuantity = Integer.parseInt(mQuantity);
                if(updateQuantity == 0){
                    Toast.makeText(context,"Quantity reached at its minimum level",Toast.LENGTH_SHORT).show();
                }
                else {
                    updateQuantity = updateQuantity - 1;
                    ContentValues values =new ContentValues();
                    values.put(BookEntry.COLUMN_PRODUCT_QUANTITY, updateQuantity);
                    Uri uri = Uri.withAppendedPath(BookEntry.CONTENT_URI, rowId);
                     int rowsUpdate = context.getContentResolver().update(uri, values, null,null);
                    if (rowsUpdate!=0){
                        Toast.makeText(context,"Update is successful",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(context,"Update is Unsuccessful",Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });

    }
}
