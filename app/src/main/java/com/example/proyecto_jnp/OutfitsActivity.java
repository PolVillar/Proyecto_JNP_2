package com.example.proyecto_jnp;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import java.util.Calendar;
import java.util.List;

import model.Clothes;
//import model.OnItemClickListener;
import model.Outfit;
import model.OutfitsRecylecViewAdapter;

public class OutfitsActivity extends AppCompatActivity {
    private List<Outfit> outfits;
    private Calendar calendar;
    private OutfitsRecylecViewAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outfits);
        setBackCallback();
        charge();
       /* adapter.setOnItemClickListener(pos -> {
            Outfit outfit= outfits.get(pos);
        });*/
    }
    private void setBackCallback() {
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                finish();
            }
        };
        this.getOnBackPressedDispatcher().addCallback(this, callback);
    }
    private void charge(){
        calendar = Calendar.getInstance();
        adapter= new OutfitsRecylecViewAdapter(this,outfits);
    }
    public void onClickShowCalendar(View view){
        showDatePickerDialog((EditText) view);
    }
    private void showDatePickerDialog(EditText date) {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year1, month1, dayOfMonth1) -> {
                    // Aquí puedes manejar la fecha seleccionada
                    String selectedDate = dayOfMonth1 + "/" + (month1 + 1) + "/" + year1;
                    date.setText(selectedDate);
                },
                year,
                month,
                dayOfMonth);

        datePickerDialog.show();
    }
}