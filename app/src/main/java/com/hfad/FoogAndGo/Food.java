package com.hfad.FoogAndGo;

import com.hfad.FoodAndGo.R;

public class Food {
    private String name;
    private String description;
    private int imageResourceId;

    //FOODs is an array of Drinks
    public static final Food[] FOODs = {
        new Food("Latte", "A couple of espresso shots with steamed milk",
                  R.drawable.latte),
        new Food("Cappuccino", "Espresso, hot milk, and a steamed milk foam",
                  R.drawable.cappuccino),
        new Food("Filter", "Highest quality beans roasted and brewed fresh",
                  R.drawable.filter)
    };

    //Each Food has a name, description, and an image resource
    private Food(String name, String description, int imageResourceId) {
        this.name = name;
        this.description = description;
        this.imageResourceId = imageResourceId;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public int getImageResourceId() {
        return imageResourceId;
    }

    public String toString() {
        return this.name;
    }
}
