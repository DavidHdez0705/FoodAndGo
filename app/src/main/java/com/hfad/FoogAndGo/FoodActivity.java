package com.hfad.FoogAndGo;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.view.View;
import android.widget.CheckBox;
import android.content.ContentValues;
import android.os.AsyncTask;

import com.hfad.FoodAndGo.R;

public class FoodActivity extends Activity {

    public static final String EXTRA_FOODNO = "foodNo";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food);
        
        //Get the drink from the intent
        int foodNo = (Integer)getIntent().getExtras().get(EXTRA_FOODNO);

        //Create a cursor
        try {
            SQLiteOpenHelper foodAndGoDatabaseHelper = new FoodAndGoDatabaseHelper(this);
            SQLiteDatabase db = foodAndGoDatabaseHelper.getReadableDatabase();
            Cursor cursor = db.query ("FOOD",
                                      new String[] {"NAME","PRICE", "DESCRIPTION", "IMAGE_RESOURCE_ID", "FAVORITE"},
                                      "_id = ?",
                                      new String[] {Integer.toString(foodNo)},
                                      null, null,null);

            //Move to the first record in the Cursor
            if (cursor.moveToFirst()) {

                //Get the drink details from the cursor
                String nameText = cursor.getString(0);
                int price = cursor.getInt(1);
                String descriptionText = cursor.getString(2);
                int photoId = cursor.getInt(3);
                boolean isFavorite = (cursor.getInt(4) == 1);

                //Populate the drink name
                TextView name = (TextView)findViewById(R.id.name);
                name.setText(nameText);

                TextView TPrice = (TextView)findViewById(R.id.price);
                TPrice.setText("$"+price);
                //Populate the drink description
                TextView description = (TextView)findViewById(R.id.description);
                description.setText(descriptionText);

                //Populate the drink image
                ImageView photo = (ImageView)findViewById(R.id.photo);
                photo.setImageResource(photoId);
                photo.setContentDescription(nameText);

                //Populate the favorite checkbox
                CheckBox favorite = (CheckBox)findViewById(R.id.favorite);
                favorite.setChecked(isFavorite);
            }
            cursor.close();
            db.close();
        } catch(SQLiteException e) {
            Toast toast = Toast.makeText(this, "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    //Update the database when the checkbox is clicked
    public void onFavoriteClicked(View view){
        int foodNo = (Integer)getIntent().getExtras().get("foodNo");
        new UpdateDrinkTask().execute(foodNo);
    }

    //Inner class to update the drink.
    private class UpdateDrinkTask extends AsyncTask<Integer, Void, Boolean> {
        ContentValues foodValues;
        protected void onPreExecute() {
            CheckBox favorite = (CheckBox)findViewById(R.id.favorite);
            foodValues = new ContentValues();
            foodValues.put("FAVORITE", favorite.isChecked());
        }
        protected Boolean doInBackground(Integer... foods) {
            int foodNo = foods[0];
            SQLiteOpenHelper foodAndGpDatabaseHelper = new FoodAndGoDatabaseHelper(FoodActivity.this);
            try {
                SQLiteDatabase db = foodAndGpDatabaseHelper.getWritableDatabase();
                db.update("FOOD", foodValues,
                          "_id = ?", new String[] {Integer.toString(foodNo)});
                db.close();
                return true;
            } catch(SQLiteException e) {
                return false;
            }
        }
        protected void onPostExecute(Boolean success) {
            if (!success) {
                Toast toast = Toast.makeText(FoodActivity.this,
                                             "Database unavailable", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }
}
