package com.example.proyecto_jnp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import model.Clothes;

public class AddMoreClothes extends AppCompatActivity {

    public final List<String> categories= new ArrayList<>(Arrays.asList(getString(R.string.category_jacket),
            getString(R.string.category_shirt),getString(R.string.category_pants),getString(R.string.category_shoes),
            getString(R.string.category_underwear),getString(R.string.category_complement)));
    private EditText etName,etColor,etSize;
    private ImageView ivNewClothe;
    private Spinner spCategory,spCollection;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_more_clothes);
        charge();
    }
    private void charge(){
        etName=findViewById(R.id.etName);
        etColor=findViewById(R.id.etColor);
        etSize=findViewById(R.id.etSize);
        ivNewClothe=findViewById(R.id.ivNewClothe);
        spCategory=findViewById(R.id.spCategory);
        spCollection=findViewById(R.id.spCollection);
    }
    public void create(View view){
        if(etName.getText().toString().isEmpty()) etName.setError(getString(R.string.edit_text_empty));
        else{
            Clothes clothes= new Clothes(null,etName.getText().toString(),
                    etColor.getText().toString(),etSize.getText().toString(),
                    null, null,null,null ,null);
        }
    }
}