package com.hfad.FoogAndGo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ListView;
import android.view.View;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteDatabase;
import android.widget.SimpleCursorAdapter;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.hfad.FoodAndGo.R;

public class TopLevelActivity extends Activity {

    private SQLiteDatabase db;
    private Cursor favoritesCursor;
    private String order;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_level);
        //Create an OnItemClickListener
        AdapterView.OnItemClickListener itemClickListener =
                new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> listView,
                                            View v,
                                            int position,
                                            long id) {
                        switch (position) {
                            case 0:
                                Intent intent = new Intent(TopLevelActivity.this,
                                        SushiCategoryActivity.class);
                                startActivity(intent);
                                break;
                            case 1:
                                Intent intent1 = new Intent(TopLevelActivity.this,
                                        DrinksCategoryActivity.class);
                                startActivity(intent1);
                                break;
                            case 2:
                                Intent intent2 = new Intent(TopLevelActivity.this,
                                        DessertsCategoryActivity.class);
                                startActivity(intent2);
                                break;

                        }

                    }
                };
        //Add the listener to the list view
        ListView listView = (ListView) findViewById(R.id.list_options);
        listView.setOnItemClickListener(itemClickListener);


        //Populate the list_favorites ListView from a cursor
        ListView listFavorites = (ListView) findViewById(R.id.list_favorites);
        try {
            SQLiteOpenHelper FoodAndGoDatabaseHelper = new FoodAndGoDatabaseHelper(this);
            db = FoodAndGoDatabaseHelper.getReadableDatabase();
            favoritesCursor = db.query("FOOD",
                    new String[]{"_id", "NAME", "PRICE"}, "FAVORITE = 1",
                    null, null, null, null);
            CursorAdapter favoriteAdapter =
                    new SimpleCursorAdapter(TopLevelActivity.this,
                            android.R.layout.simple_list_item_2,
                            favoritesCursor,
                            new String[]{"NAME", "PRICE"},
                            new int[]{android.R.id.text1, android.R.id.text2}, 0);
            listFavorites.setAdapter(favoriteAdapter);
            int total = 0;
            order="Orden\r\n";
            if (favoritesCursor.moveToFirst()) {
                //do{
                for (int i=0;i<favoritesCursor.getCount();i++) {
                    order+="\r\n"+favoritesCursor.getString(1)+"------$"+favoritesCursor.getInt(2);
                    total += favoritesCursor.getInt(2);
                    favoritesCursor.moveToNext();
                }
                //}while(favoritesCursor.moveToNext());

            }
            TextView price = (TextView) findViewById(R.id.price);
            price.setText("$"+ total);
        } catch (SQLiteException e) {
            Toast toast = Toast.makeText(this, "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }

        //Navigate to FoodActivity if a food is clicked
        listFavorites.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> listView, View v, int position, long id) {
                Intent intent = new Intent(TopLevelActivity.this, FoodActivity.class);
                intent.putExtra(FoodActivity.EXTRA_FOODNO, (int) id);
                startActivity(intent);
            }
        });
        // Get location


    }

    //Close the cursor and database in the onDestroy() method
    @Override
    public void onDestroy(){
        super.onDestroy();
        favoritesCursor.close();
        db.close();
    }
    // Confirm button
    public void confirm(View view){
        if (favoritesCursor.getCount() !=0){
            Intent intent = new Intent(TopLevelActivity.this, OrderActivity.class);
            startActivity(intent);
        }else{
            Toast toast = Toast.makeText(this, "Orden vacia", Toast.LENGTH_SHORT);
            toast.show();
        }


    }
    public void onRestart() {
        super.onRestart();
        try {
            FoodAndGoDatabaseHelper foodAndGoDatabaseHelper = new FoodAndGoDatabaseHelper(this);
            db = foodAndGoDatabaseHelper.getReadableDatabase();
            Cursor newCursor = db.query("FOOD",
                                        new String[] { "_id", "NAME","PRICE"},
                                        "FAVORITE = 1",
                                        null, null, null, null);
            ListView listFavorites = (ListView)findViewById(R.id.list_favorites);
            CursorAdapter adapter = (CursorAdapter) listFavorites.getAdapter();
            adapter.changeCursor(newCursor);
            favoritesCursor = newCursor;
            int total = 0;
            order="Orden:\r\n";
            if (favoritesCursor.moveToFirst()) {
                //do{
                for (int i=0;i<favoritesCursor.getCount();i++) {
                    order+="\r\n"+favoritesCursor.getString(1)+"----------$"+favoritesCursor.getInt(2);
                    total += favoritesCursor.getInt(2);
                    favoritesCursor.moveToNext();
                }

                //}while(favoritesCursor.moveToNext());

            }
            TextView price = (TextView) findViewById(R.id.price);
            price.setText("$"+ total);

        } catch(SQLiteException e) {
            Toast toast = Toast.makeText(this, "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}
