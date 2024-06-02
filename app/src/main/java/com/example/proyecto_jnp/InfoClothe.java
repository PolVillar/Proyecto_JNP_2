package com.example.proyecto_jnp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

public class InfoClothe extends AppCompatActivity {

    private Spinner spCategory,spCollection,spContainer;
    private EditText nameEdit, colorEdit, sizeEdit;
    private Button editBt;
    private ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_clothe);
        charge();
        Intent intent = getIntent();
    }
    private void charge(){
        spCategory=findViewById(R.id.spCategoryEdit);
        spContainer=findViewById(R.id.spinner2);
        spCollection=findViewById(R.id.spCollectionEdit);
        nameEdit=findViewById(R.id.etNameEdit);
        colorEdit=findViewById(R.id.etColorEdit);
        sizeEdit=findViewById(R.id.etSizeEdit);
        editBt=findViewById(R.id.bEditClothe);
        image = findViewById(R.id.ivClotheEdit);
    }
}