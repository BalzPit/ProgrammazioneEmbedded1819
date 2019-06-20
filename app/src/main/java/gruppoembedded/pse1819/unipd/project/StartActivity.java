package gruppoembedded.pse1819.unipd.project;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import gruppoembedded.pse1819.unipd.project.Database.DietDb;
import gruppoembedded.pse1819.unipd.project.Database.Food;
import gruppoembedded.pse1819.unipd.project.Database.FoodDao;
import gruppoembedded.pse1819.unipd.project.Database.Meal;
import gruppoembedded.pse1819.unipd.project.Database.MealDao;

import androidx.appcompat.app.AppCompatActivity;

public class StartActivity extends AppCompatActivity {
    private static final String TAG = "StartActivityTag";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        //all'apertura dell'app verifico se la tabella Food è vuota => prima volta che apro l'app, in tal caso la riempio
        istanziaFood();
    }

    //metodi per inviare il titolo della tabella da visualizzare nel riepilogo
    public void riepPranzo(View view) {
        Button pranzo=findViewById(R.id.pranzo);
        Intent intPranzo=new Intent(view.getContext(), MealActivity.class);
        intPranzo.putExtra("username","Pranzo");
        startActivityForResult(intPranzo,0);
    }

    public void riepCena(View view) {
        Button cena=findViewById(R.id.cena);
        Intent intPranzo=new Intent(view.getContext(), MealActivity.class);
        intPranzo.putExtra("username","Cena");
        startActivityForResult(intPranzo,0);
    }

    //instanziazione db
    private DietDb db;
    private DietDb getDatabaseManager(){
        if(db==null)
            db=DietDb.getDatabase(this);
        return db;
    }

    private void istanziaFood(){
        //l'istanziazione viene fatta solo se è la prima volta che l'app viene installata, e quindi non esiste anoora il database
        if(getDatabaseManager().noteModelFood().loadAllFood().size()==0) {
            Log.i(TAG, "onCreate: sto eseguendo istanziazione");
            Food elemento = new Food();
            elemento.nome = "pasta";
            getDatabaseManager().noteModelFood().insertFood(elemento);
            elemento.nome = "pizza";
            getDatabaseManager().noteModelFood().insertFood(elemento);
            elemento.nome = "carne";
            getDatabaseManager().noteModelFood().insertFood(elemento);
            elemento.nome = "yogurt";
            getDatabaseManager().noteModelFood().insertFood(elemento);
            elemento.nome = "mela";
            getDatabaseManager().noteModelFood().insertFood(elemento);
            Log.i(TAG, "onCreate: ho crashato");
        }
    }
}
