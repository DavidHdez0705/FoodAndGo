package com.hfad.FoogAndGo;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.hfad.FoodAndGo.R;

class FoodAndGoDatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "FoodAndGo"; // the name of our database
    private static final int DB_VERSION = 2; // the version of the database

    FoodAndGoDatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        updateMyDatabase(db, 0, DB_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        updateMyDatabase(db, oldVersion, newVersion);
    }

    private void updateMyDatabase(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 1) {
            db.execSQL("CREATE TABLE PHONEBOOK (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                            + "LOCAL TEXT, "
                            + "PHONE TEXT);"
            );
            insertPhoneNo(db, "sushi factory", "6642701211");

            db.execSQL("CREATE TABLE FOOD (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "NAME TEXT, "
                    + "PRICE INTEGER,"
                    + "DESCRIPTION TEXT, "
                    + "IMAGE_RESOURCE_ID INTEGER, "
                    + "FAVORITE NUMERIC,"
                    + "TYPE INTEGER);");

            insertFood(db, "Mar y Tierra", 94, "El tradicional de philadelphia, pepino, aguacate, res y camarón; servido con un espejo de aderezo miso y ajonjolí.", R.drawable.mar_y_tierra,0,1);
            insertFood(db, "Crispy Chicken Roll",100, "Pollo teriyaki crispy y ajonjolí arriba; philadelphia, aguacate y pepino por dentro; bañado con salsa de anguila.",
                        R.drawable.crispy_chicken_roll,0,1);

        }
        insertFood(db, "Aguachile Roll", 110, "Rollo de camarón en aguachile, pepino, philadelphia, salsa roja, ajonjolí y aguacate; salpicado con gotas de salsa de anguila y servido con nuestra famosa salsa de aguachile. *Tropical (mango o kiwi)", R.drawable.aguachile_tropical,0,1);
        insertFood(db, "RockAnd Roll", 100, "Por dentro aguacate, pepino, tampico y arroz. Alga capeada por fuera con un topping de calamar frito, furikake de camarón y ume, callo de hacha, aderezo spicy, ajonjolí, cebollín y salsa de Jamaica.", R.drawable.rockand_roll,0,1);
        insertFood(db, "Jugo de naranja", 65, "Jugo de naranja natural recien exprimido.", R.drawable.jugo_naranja,0, 2);
        insertFood(db, "Té", 40, "Té natural del dia sobre las rocas.", R.drawable.te,0, 2);
        insertFood(db, "Limonada", 40, "Hecha de limones naturales frescos ligeramente endulzada con azucar morena.", R.drawable.limonada,0, 2);
        insertFood(db, "Tempura helado", 40, "Helado \"frito\" de fresa, vainilla o chocolate a elegir con corteza crujiente.", R.drawable.tempura_helado,0, 3);
        insertFood(db, "Pay de queso", 40, "Pastel de queso casero endulzado con azucarmorena e ingredientes frescos.", R.drawable.pay_de_queso,0, 3);
        /*if (oldVersion < 2) {
            db.execSQL("ALTER TABLE FOOD ADD COLUMN FAVORITE NUMERIC;");
        }*/
    }

    private static void insertFood(SQLiteDatabase db, String name,
                                    int price,String description, int resourceId,int favorite,int type) {
        ContentValues foodValues = new ContentValues();
        foodValues.put("NAME", name);
        foodValues.put("PRICE",price);
        foodValues.put("DESCRIPTION", description);
        foodValues.put("IMAGE_RESOURCE_ID", resourceId);
        foodValues.put("FAVORITE", favorite);
        foodValues.put("TYPE", type);
        db.insert("FOOD", null, foodValues);
    }
    private static void insertPhoneNo(SQLiteDatabase db, String local,String phone) {
        ContentValues localValues = new ContentValues();
        localValues.put("LOCAL", local);
        localValues.put("PHONE",phone);
        db.insert("PHONEBOOK", null, localValues);
    }
}
