package com.example.proyecto_jnp;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import model.Clothes;
import model.Container;
import model.EnumerationMaps;

public class AddMoreClothes extends AppCompatActivity {

    public List<String> categories,collections;
    private EditText etName,etColor,etSize;
    private ImageView ivNewClothe;
    private Spinner spCategory,spCollection;
    private EnumerationMaps maps;
    private Container container;
    ActivityResultLauncher<Intent> launcher;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_more_clothes);
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                finish();
            }
        };
        this.getOnBackPressedDispatcher().addCallback(this, callback);
        charge();
        setAdapters();
    }
    private void charge(){
        etName=findViewById(R.id.etName);
        etColor=findViewById(R.id.etColor);
        etSize=findViewById(R.id.etSize);
        ivNewClothe=findViewById(R.id.ivNewClothe);
        spCategory=findViewById(R.id.spCategory);
        spCollection=findViewById(R.id.spCollection);
        categories= new ArrayList<>(Arrays.asList(getString(R.string.category_jacket),
                getString(R.string.category_shirt),getString(R.string.category_pants),getString(R.string.category_shoes),
                getString(R.string.category_underwear),getString(R.string.category_complement)));
        collections= new ArrayList<>(Arrays.asList(getString(R.string.collection_winter),
                getString(R.string.collection_spring),getString(R.string.collection_summer),getString(R.string.collection_autumn)));
        maps= new EnumerationMaps(this);
        initializeLauncher();
    }
    private void setAdapters(){
        ArrayAdapter<String> collectionsAdapter =
                new ArrayAdapter<>(this,
                        android.R.layout.simple_list_item_1, collections);
        spCollection.setAdapter(collectionsAdapter);
        ArrayAdapter<String> categoriesAdapter =
                new ArrayAdapter<>(this,
                        android.R.layout.simple_list_item_1, categories);
        spCategory.setAdapter(categoriesAdapter);
    }
    private void initializeLauncher(){
        launcher= registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if(result.getData()!=null){
                Bitmap photoBitmap= (Bitmap) result.getData().getExtras().get("data");
                if(photoBitmap!=null) ivNewClothe.setImageBitmap(photoBitmap);
                else{
                    Uri photoUri = result.getData().getData();
                    if(photoUri!=null) {
                        try {
                            ivNewClothe.setImageBitmap(MediaStore.Images.Media.getBitmap(this.getContentResolver(), photoUri));
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        });
    }
    public void onClickCreate(View view){
        if(etName.getText().toString().isEmpty()) etName.setError(getString(R.string.edit_text_empty));
        else{
            Bitmap bitmap = ((BitmapDrawable) ivNewClothe.getDrawable()).getBitmap();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();
            Clothes clothes= new Clothes(null,etName.getText().toString(),
                    etColor.getText().toString(),etSize.getText().toString(),
                    byteArray, maps.getCollections().get(spCollection.getSelectedItem().toString()),
                    maps.getCategories().get(spCategory.getSelectedItem().toString()),null ,null);
        }
    }
    public void onClickAddImage(View view){
        Intent galleryIntent = new Intent();
        galleryIntent.setType("image/*");
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Intent chooser = new Intent(Intent.ACTION_CHOOSER);
        chooser.putExtra(Intent.EXTRA_INTENT, galleryIntent);
        chooser.putExtra(Intent.EXTRA_TITLE, "Select from:");
        Intent[] intentArray = { cameraIntent };
        chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray);
        launcher.launch(chooser);
    }
}