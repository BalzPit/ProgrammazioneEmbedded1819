package gruppoembedded.pse1819.unipd.project;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;

public class InsertActivity extends AppCompatActivity {

    //la lista prodotti dovrà essere implementata nel database
    private String[] listaProdotti={"pasta","bistecca","formaggio","risotto","yogurt"};
    private static final String TAG="InsertActivity";

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
        String[] nameproducts =listaProdotti;

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
                insert(parent.getItemAtPosition(position).toString());
            }
        });
    }

    //inserimento dati nel database

    //instanziazione db
    private CiboDb db;
    private CiboDb getDatabaseManager(){
        if(db==null)
            db=CiboDb.getDatabase(this);
        return db;
    }

    private void insert(String pietanza){
        //creo oggetto di tipo Cibo, il cui testo è passato come parametro e lo inserisco nel database
        Cibo elemento= new Cibo();
        elemento.text=pietanza;
        getDatabaseManager().noteModel().insertCibo(elemento);

        //in seguito torno all'activity di riepilogo
        Intent aggiungi=new Intent(this, MealActivity.class);
        startActivityForResult(aggiungi,0);
    }
}
