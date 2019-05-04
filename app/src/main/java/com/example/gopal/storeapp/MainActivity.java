package com.example.gopal.storeapp;


import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gopal.storeapp.Database.BookContract.BookEntry;


public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{
    private BookAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listView = findViewById(R.id.list_view);
        // setting empty view in case list is empty
        TextView emptyView = findViewById(R.id.empty_view);
        listView.setEmptyView(emptyView);

        mAdapter = new BookAdapter(this, null);
        listView.setAdapter(mAdapter);
        // Initializing the Loader
        getLoaderManager().initLoader(0,null,this);
        //FAB button is clicked
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddItemActivity.class);
                startActivity(intent);
            }
        });
        // OnItemClickListener  for ListView
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
                Uri uri = Uri.withAppendedPath(BookEntry.CONTENT_URI, String.valueOf(id));
               intent.setData(uri);
                startActivity(intent);
            }
        });
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection = {
                BookEntry._ID,
                BookEntry.COLUMN_PRODUCT_NAME,
                BookEntry.COLUMN_PRODUCT_PRICE,
                BookEntry.COLUMN_PRODUCT_QUANTITY};
        return new CursorLoader(this,BookEntry.CONTENT_URI,projection,null,null,null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        mAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_activity_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.delete_all:
                showDeleteConfirmationDialog();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    // Showing Dialog before deleting items
    private void showDeleteConfirmationDialog() {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the positive and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_all_action_title);
        builder.setPositiveButton(R.string.positive_delete_action, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Delete" button, so delete the pet.
                deleteAll();
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
    // deleting all items in ListView
    public void deleteAll(){
        int rowsDeleted = getContentResolver().delete(BookEntry.CONTENT_URI,null,null);
        if (rowsDeleted!=0){
            Toast.makeText(this,"Delete is successful",Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(this,"Delete is Unsuccessful",Toast.LENGTH_SHORT).show();
        }
    }

}