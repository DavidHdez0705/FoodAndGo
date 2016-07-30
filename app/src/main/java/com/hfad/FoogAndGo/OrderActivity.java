package com.hfad.FoogAndGo;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.ListView;
import android.view.View;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteDatabase;
import android.widget.ShareActionProvider;
import android.widget.SimpleCursorAdapter;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.hfad.FoodAndGo.R;

public class OrderActivity extends Activity implements LocationListener{

    private SQLiteDatabase db;
    private Cursor favoritesCursor;
    private String order;
    private ShareActionProvider shareActionProvider;
    protected LocationListener locationListener;
    protected Context context;
    protected LocationManager locationManager;
    private Location locate;

    @Override
    public void onLocationChanged(Location location) {
        ///txtLat = (TextView) findViewById(R.id.textview1);
        //txtLat.setText("Latitude:" + location.getLatitude() + ", Longitude:" + location.getLongitude());
        locate = location;
        Log.d("Latitude", "Latitude:" + location.getLatitude() + ", Longitude:" + location.getLongitude());
        //toast.show();
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d("Latitude","disable");
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d("Latitude","enable");
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d("Latitude","status");
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Populate the list_favorites ListView from a cursor
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        ListView listFavorites = (ListView) findViewById(R.id.list_favorites);
        try {
            SQLiteOpenHelper FoodAndGoDatabaseHelper = new FoodAndGoDatabaseHelper(this);
            db = FoodAndGoDatabaseHelper.getReadableDatabase();
            favoritesCursor = db.query("FOOD",
                    new String[]{"_id", "NAME", "PRICE"}, "FAVORITE = 1",
                    null, null, null, null);
            CursorAdapter favoriteAdapter =
                    new SimpleCursorAdapter(OrderActivity.this,
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
                order+="\r\n\r\n"+"Total: $"+total;

            }
            TextView price = (TextView) findViewById(R.id.price);
            price.setText("$"+ total);
        } catch (SQLiteException e) {
            Toast toast = Toast.makeText(this, "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        //Navigate to FoodActivity if a food is clicked
        /*listFavorites.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> listView, View v, int position, long id) {
                Intent intent = new Intent(OrderActivity.this, FoodActivity.class);
                intent.putExtra(FoodActivity.EXTRA_FOODNO, (int) id);
                startActivity(intent);
            }
        });*/

    }

    //Close the cursor and database in the onDestroy() method
    @Override
    public void onDestroy(){
        super.onDestroy();
        favoritesCursor.close();
        db.close();
    }
    // Order button
    boolean BLocate=false;

    public void makeOrder(View view) throws InterruptedException {
        String phoneNo;
        if (locate!= null) {
            if (BLocate==false) {
                order += "\r\nUbicación: " + "Latitude:" + locate.getLatitude() + ", Longitude:" + locate.getLongitude();
                BLocate=true;

                Cursor newCursor = db.query("PHONEBOOK",
                        new String[]{"_id", "LOCAL", "PHONE"},
                        null,
                        null, null, null, null);
                if (newCursor.getCount() == 0)
                    //phoneNo = "6644449173";
                    Toast.makeText(getApplicationContext(),"Telefono no disponible.", Toast.LENGTH_LONG).show();
                else {
                    newCursor.moveToFirst();
                    phoneNo = newCursor.getString(2);
                    sendSMSMessage(phoneNo, order);
                    db.execSQL("UPDATE FOOD SET FAVORITE=0 WHERE FAVORITE=1");

                }

                finish();
            }else{
                Toast.makeText(getApplicationContext(),"Orden enviada", Toast.LENGTH_LONG).show();
            }

        }else{
            Toast.makeText(getApplicationContext(),"Esperando ubicación", Toast.LENGTH_LONG).show();
            //makeOrder(view);
        }
    }
    protected void sendSMSMessage(String phoneNo,String message) {


        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo, null, message, null, null);
            Log.d("test",phoneNo);
            Toast.makeText(getApplicationContext(), "SMS enviado.", Toast.LENGTH_LONG).show();
            Toast.makeText(getApplicationContext(), order, Toast.LENGTH_LONG).show();
        }

        catch (Exception e) {
            Toast.makeText(getApplicationContext(), "SMS faild, please try again.", Toast.LENGTH_LONG).show();
            e.printStackTrace();
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
                order+="\r\n\t"+"Total:     $"+total;
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
