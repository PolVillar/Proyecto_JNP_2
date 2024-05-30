package model;


import android.content.Context;

import com.example.proyecto_jnp.R;

import java.util.HashMap;

public class EnumerationMaps {
    private Context context;
    private String winter,spring,summer,autumn,jacket,shirt,pants,shoes,underwear,complement;
    private final HashMap<String, Clothes.Collection> collections= new HashMap<>();
    private final HashMap<String, Clothes.Category> categories= new HashMap<>();

    public EnumerationMaps(Context context) {
        this.context = context;
        initialize();
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
    private void initialize(){
        winter=context.getString(R.string.collection_winter);
        spring=context.getString(R.string.collection_spring);
        summer=context.getString(R.string.collection_summer);
        autumn=context.getString(R.string.collection_autumn);
        jacket=context.getString(R.string.category_jacket);
        shirt=context.getString(R.string.category_shirt);
        pants=context.getString(R.string.category_pants);
        shoes=context.getString(R.string.category_shoes);
        underwear=context.getString(R.string.category_underwear);
        complement=context.getString(R.string.category_complement);
    }

    public HashMap<String, Clothes.Collection> getCollections() {
        return collections;
    }

    public HashMap<String, Clothes.Category> getCategories() {
        return categories;
    }
}
