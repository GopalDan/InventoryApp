package com.example.gopal.storeapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.gopal.storeapp.Database.BookContract;
import com.example.gopal.storeapp.Database.BookContract.BookEntry;


public class DetailsActivity extends AppCompatActivity
                                      implements LoaderManager.LoaderCallbacks<Cursor>{

    private TextView mNameTextView;
    private TextView mNameTextViewHeader;
    private TextView mPriceTextView;
    private TextView mPriceTextViewHeader;
    private TextView mQuantityTextView;
    private TextView mQuantityTextViewHeader;
    private TextView mSupplierNameTextView;
    private TextView mSupplierNameTextViewHeader;
    private TextView mSupplierContactTextView;
    private TextView mSupplierContactTextViewHeader;
    private Uri mCurrentItemUri;
    private String mSupplierPhoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        mNameTextView = findViewById(R.id.product_name);
        mNameTextViewHeader = findViewById(R.id.product_name_header);
        mPriceTextView = findViewById(R.id.product_price);
        mPriceTextViewHeader = findViewById(R.id.product_price_header);
        mQuantityTextView = findViewById(R.id.product_quantity);
        mQuantityTextViewHeader = findViewById(R.id.product_quantity_header);
        mSupplierNameTextView = findViewById(R.id.seller_name);
        mSupplierNameTextViewHeader = findViewById(R.id.seller_name_header);
        mSupplierContactTextView = findViewById(R.id.seller_phone_no);
        mSupplierContactTextViewHeader = findViewById(R.id.seller_phone_no_header);

        mCurrentItemUri = getIntent().getData();
        if (mCurrentItemUri!=null) {
            getSupportLoaderManager().initLoader(0, null, this);
        }
        // Call button is clicked
        ImageView imageView = findViewById(R.id.call);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callSupplier();
            }
        });
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {BookContract.BookEntry._ID,
                BookContract.BookEntry.COLUMN_PRODUCT_NAME,
                BookContract.BookEntry.COLUMN_PRODUCT_PRICE,
                BookContract.BookEntry.COLUMN_PRODUCT_QUANTITY,
                BookContract.BookEntry.COLUMN_SUPPLIER_NAME,
                BookContract.BookEntry.COLUMN_SUPPLIER_PHONE_N0};

        return new CursorLoader(this,mCurrentItemUri,projection,null,null,null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if(cursor.moveToFirst()) {
            int nameColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_PRODUCT_NAME);
            int priceColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_PRODUCT_PRICE);
            int quantityColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_PRODUCT_QUANTITY);
            int sellerColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_SUPPLIER_NAME);
            int sellerContactColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_SUPPLIER_PHONE_N0);

            String name = cursor.getString(nameColumnIndex);
            String price = cursor.getString(priceColumnIndex);
            String quantity = cursor.getString(quantityColumnIndex);
            String sellerName = cursor.getString(sellerColumnIndex);
            mSupplierPhoneNumber = cursor.getString(sellerContactColumnIndex);

            //Setting the value
            mNameTextView.setText(name);
            mNameTextViewHeader.setText(R.string.product_name_header);
            mPriceTextView.setText(price);
            mPriceTextViewHeader.setText(R.string.product_price);
            mQuantityTextView.setText(quantity);
            mQuantityTextViewHeader.setText(R.string.product_quantity);
            mSupplierNameTextView.setText(sellerName);
            mSupplierNameTextViewHeader.setText(R.string.supplier_name);
            mSupplierContactTextView.setText(mSupplierPhoneNumber);
            mSupplierContactTextViewHeader.setText(R.string.supplier_phone_no_header);

        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // Reset the cursor

    }
    // calling phone to supplier
    public void callSupplier(){
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + mSupplierPhoneNumber));
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.edit:
                Intent intent = new Intent(this,EditorActivity.class);
                intent.setData(mCurrentItemUri);
                startActivity(intent);
                break;
            case R.id.delete:
                // Do delete operation
                showDeleteConfirmationDialog();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showDeleteConfirmationDialog() {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the positive and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_action_title);
        builder.setPositiveButton(R.string.positive_delete_action, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Delete" button, so delete the pet.
                deleteItem();
            }
        });
        builder.setNegativeButton(R.string.negative_delete_action, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Cancel" button, so dismiss the dialog
                // and continue editing the pet.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    // Deleting an item
    public void deleteItem(){
       int rowsDeleted = getContentResolver().delete(mCurrentItemUri,null,null);
        if (rowsDeleted!=0){
            Toast.makeText(this,"Delete is successful",Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(this,"Delete is Unsuccessful",Toast.LENGTH_SHORT).show();
        }
        finish();
    }
}
