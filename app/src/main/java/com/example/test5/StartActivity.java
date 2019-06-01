package com.example.test5;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class StartActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
    }

    //metodi per inviare il titolo della tabella da visualizzare nel riepilogo
        public void riepPranzo(View view) {
        Button pranzo=findViewById(R.id.pranzo);
        Intent intPranzo=new Intent(view.getContext(), MainActivity.class);
        intPranzo.putExtra("username","Pranzo");
        startActivityForResult(intPranzo,0);
    }

    public void riepCena(View view) {
        Button cena=findViewById(R.id.cena);
        Intent intPranzo=new Intent(view.getContext(), MainActivity.class);
        intPranzo.putExtra("username","Cena");
        startActivityForResult(intPranzo,0);
    }
}
