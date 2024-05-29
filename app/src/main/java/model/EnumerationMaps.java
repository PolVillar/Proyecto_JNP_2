package model;


import android.content.Context;

import com.example.proyecto_jnp.R;

import java.util.HashMap;

public class EnumerationMaps {
    private Context context;
    private String winter=context.getString(R.string.collection_winter);
    private String spring=context.getString(R.string.collection_spring);
    private String summer=context.getString(R.string.collection_summer);
    private String autumn=context.getString(R.string.collection_autumn);
    private String jacket=context.getString(R.string.category_jacket);
    private String shirt=context.getString(R.string.category_shirt);
    private String pants=context.getString(R.string.category_pants);
    private String shoes=context.getString(R.string.category_shoes);
    private String underwear=context.getString(R.string.category_underwear);
    private String complement=context.getString(R.string.category_complement);
    public static final HashMap<String, Clothes.Collection> collections= new HashMap<>();
    public static final HashMap<String, Clothes.Category> categories= new HashMap<>();

    public EnumerationMaps(Context context) {
        this.context = context;
        collections.put(winter, Clothes.Collection.WINTER);
        collections.put(spring, Clothes.Collection.SPRING);
        collections.put(summer, Clothes.Collection.SUMMER);
        collections.put(autumn, Clothes.Collection.AUTUMN);
        categories.put(jacket, Clothes.Category.JACKET);
        categories.put(shirt, Clothes.Category.SWEATER_SHIRT);
        categories.put(pants, Clothes.Category.PANTS_SKIRT);
        categories.put(shoes, Clothes.Category.SHOES);
        categories.put(underwear, Clothes.Category.UNDERWEAR);
        categories.put(complement, Clothes.Category.COMPLEMENT);
    }
}
