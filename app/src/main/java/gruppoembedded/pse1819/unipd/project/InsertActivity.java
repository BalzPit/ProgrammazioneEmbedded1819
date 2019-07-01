package gruppoembedded.pse1819.unipd.project;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
//quelli in inglese
import gruppoembedded.pse1819.unipd.project.Database.DbSupport;
import gruppoembedded.pse1819.unipd.project.Database.DietDb;
import gruppoembedded.pse1819.unipd.project.Database.Food;
import gruppoembedded.pse1819.unipd.project.Database.Meal;

public class InsertActivity extends AppCompatActivity {

    //la lista prodotti dovrà essere implementata nel database
    //private String[] listaProdotti={"pasta","bistecca","formaggio","risotto","yogurt"};
    private static final String TAG="InsertActivityMine";
    public DbSupport support= new DbSupport(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);

        //qui decido il titolo della tabella
        TextView tv= (TextView)findViewById(R.id.textList);
        tv.setText("Lista cibi");

        //metodo per compilare tabella
        creat_table();
    }

    private void creat_table() {
        // definisco un array di stringhe
        String[] nameproducts =listaProdotti();

        // definisco un ArrayList
        final ArrayList<String> listp = new ArrayList<String>();
        for (int i = 0; i < nameproducts.length; ++i) {
            listp.add(nameproducts[i]);
        }

        // recupero la lista dal layout
        final ListView mylist = (ListView) findViewById(R.id.listView);

        // creo e istruisco l'adattatore
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listp);

        // inietto i dati
        mylist.setAdapter(adapter);

        //gestisco il tocco sulle righe
        mylist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "onItemClick: cliccato"+"  "+parent.getItemAtPosition(position));
                //qui metodo per modificare righe, usare i metodi dell'adattatore per accedere ai dati delle righe
                //insert(parent.getItemAtPosition(position).toString());

                //scopro qual è l'elemento della tabella pasti al quale aggiungere i cibi
                String pasto = riceviIntent();
                support.inserimento(parent.getItemAtPosition(position).toString(),pasto);

                ritorna();
            }
        });
    }
    public void ritorna(){
        // notify the calling activity of the result (it will open the Dialog used to insert
        // grams of the food selected in this activity) and close this one
        Intent aggiungi=new Intent(this, MealActivity.class);
        aggiungi.putExtra("piet", "nome");
        setResult(Activity.RESULT_OK, aggiungi);
        finish();
    }

    private String riceviIntent(){
        Intent intent=getIntent();
        String pasto=intent.getStringExtra("nome");
        Log.d(TAG, "elemento pasto "+pasto);
        return pasto;
    }

    private String[] listaProdotti(){
        //ottengo dati dal db
        List<Food> dati=support.getDatabaseManager().noteModelFood().loadAllFood();
        String[] lista=new String[dati.size()];

        //per ogni oggetto in List<Cibo> ricavo il testo
        for(int i=0;i<dati.size();i++){
            Food elem=dati.get(i);
            lista[i]=elem.nome;
        }
        return lista;
    }
}
