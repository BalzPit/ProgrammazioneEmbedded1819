package gruppoembedded.pse1819.unipd.project;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
//quelli in inglese
import gruppoembedded.pse1819.unipd.project.Database.DietDb;
import gruppoembedded.pse1819.unipd.project.Database.Meal;
import gruppoembedded.pse1819.unipd.project.tensorflowlite.ClassifierActivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MealActivity extends AppCompatActivity {

    private static final String TAG = "MainActivityPrinc";
    private static final int GET_FOOD = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal);

        //qui decido il titolo della tabella
        TextView tv= (TextView)findViewById(R.id.textList);
        tv.setText(titolo());

        //metodo per compilare tabella
        creat_table();
    }

    //when the user selects a food, using either InsertActivity or ClassifierActiviy, open
    //the dialog to insert the quantity of the food that was just chosen. (click the last selected item of the listview)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //used to update the list
        onResume();

        if (requestCode == GET_FOOD) {
            if (resultCode == Activity.RESULT_OK) {
                String[] nameproducts = trovaProdottiConDb(titolo());
                int position = nameproducts.length - 1;
                ListView mylist = findViewById(R.id.slectedfoodslistView);

                //create illusion of having the list item selected
                mylist.requestFocusFromTouch();
                mylist.setSelection(position);

                //perform a click on the correct item
                mylist.performItemClick(mylist.getChildAt(position),
                        position,
                        mylist.getAdapter().getItemId(position));
            }
        }
    }

    //metodo per decidere il titolo
    private String titolo() {
        //qui prelevo l'intent e il titolo in esso contenuto
        Intent intent=getIntent();
        String titolo=intent.getStringExtra("username");
        return titolo;
    }

    private void creat_table() {
        // definisco un array di stringhe
        String[] nameproducts =trovaProdottiConDb(titolo());

        // definisco un ArrayList
        final ArrayList<String> listp = new ArrayList<String>();
        for (int i = 0; i < nameproducts.length; ++i) {
            listp.add(nameproducts[i]);
        }

        // recupero la lista dal layout
        final ListView mylist = (ListView) findViewById(R.id.slectedfoodslistView);

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
                onClick(view, parent.getItemAtPosition(position).toString());
                //ShowPopup(view, parent.getItemAtPosition(position).toString());
            }
        });
    }

    //metodo per prelevare dati dal database

    //instanziazione db

    //creo accesso a db
    private DietDb db;
    private DietDb getDatabaseManager(){
        if(db==null)
            db=DietDb.getDatabase(this);
        return db;
    }
    //prelevo dati
    /*private String[] trovaProdottiConDb(String pasto){
        //ottengo dati dal db
        List<Cibo> dati=getDatabaseManager().noteModel().loadAllCibi();
        String[] lista=new String[dati.size()];

        //per ogni oggetto in List<Cibo> ricavo il testo
        for(int i=0;i<dati.size();i++){
            Cibo elem=dati.get(i);
            Log.d(TAG, "trovaProdottiConDb: lista: "+elem.text);
            lista[i]=elem.text;
        }
        return lista;
    }*/


    //prelevo dati dei cibi associati al pasto di un giorno
    private String[] trovaProdottiConDb(String pasto){
        //ottengo dati dal db
        List<Meal> dati=getDatabaseManager().noteModelMeal().loadAllMeals();
        Meal mioPasto=new Meal();
        Log.d(TAG, "lista pasti: "+dati);

        //cerco il nome del pasto che mi interessa nella lista
        for(int i=0;i<dati.size();i++){
            Meal elem=dati.get(i);

            //uso ignoreCase perché "pasto" inizia con la lettera maiuscola
            if(elem.nome.compareToIgnoreCase(pasto)==0){
                //se il nome combacia con quello che cerco lo salvo, poi estraggo tutti i cibi in esso contenuti
                mioPasto=elem;
                break;
            }
        }

        //il compilatore vuole necessariamente una pre-inizializzazione dell'elemento lista
        String[] lista=new String[0];

        //estrazione cibi
        try {
            //JSONObject cibi = new JSONObject(mioPasto.cibiDiOggi);
            if(mioPasto.cibiDiOggi!=null) {
                Log.d(TAG, "entrato "+ mioPasto.cibiDiOggi);
                JSONArray cibiArr=new JSONArray("[" + mioPasto.cibiDiOggi + "]");
                Log.d(TAG, "cibiArr: " + cibiArr);
                lista=new String[cibiArr.length()];

                for (int i = 0; i < cibiArr.length(); i++) {
                    JSONObject obj = new JSONObject(cibiArr.get(i).toString());
                    String x = obj.getString("nome");
                    lista[i] = x;

                }
            }else { //messo solo per debug
                Log.d(TAG, "lista di cibi: "+lista.toString());
            }
        }catch(Exception e){
            Log.d(TAG, "eccezzione: "+e);
        }
        return lista;
    }



    //metodo per il dialog che funziona
    final Context context = this;
    public void onClick(View arg0, String testo) {

        // custom dialog
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.pop_up_layout);
        dialog.setTitle("Title...");

        // settaggio componenti pop-up
        TextView text = (TextView) dialog.findViewById(R.id.testo);
        text.setText(testo);

        final NumberPicker gramsPicker= dialog.findViewById(R.id.grams_picker);
        gramsPicker.setMinValue(0);
        gramsPicker.setMaxValue(1000);

        //!!!!=========  GET GRAMS FROM DATABASE====================!!!!

        // int grams = ?
        //gramsPicker.setValue(grams);

        Button confirmButton = (Button) dialog.findViewById(R.id.btnconfirm);
        // se viene premuto il pulsante, chiudere il pop-up
        // NB! il popup viene chiuso anche se vi si preme fuori, ma le azioni di chiusura vengono eseguite solo in caso di pressione pulsante
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: azioni prima della chiusura");

                int selectedgrams = gramsPicker.getValue();

                //!!!!======= SAVE SELECTED GRAMS OF THAT FOOD TO DATABASE =====!!!!

                dialog.dismiss();
            }
        });

        final Button deleteFood = dialog.findViewById(R.id.btndelete);

        //delete the selected food from the list (and update it)
        deleteFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "cancella cibo dal db");

                //!!!!!===== DELETE the selected FOOD FROM this meal =====!!!!!!

                //update the list on the activity and close the dialog window
                creat_table();
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    //metodo di accesso alla lista di selezione cibi
    public void scegli(View view){
        Button add=findViewById(R.id.add);
        Intent aggiungi=new Intent(view.getContext(),InsertActivity.class);
        aggiungi.putExtra("nome", titolo());
        startActivityForResult(aggiungi,GET_FOOD);
    }

    public void scegliPhoto(View view){
        Button add=findViewById(R.id.add);
        Intent aggiungi=new Intent(view.getContext(), ClassifierActivity.class);
        aggiungi.putExtra("nome", titolo());
        startActivityForResult(aggiungi,GET_FOOD);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "Resuming activity");
        //update the list of foods with possible new entries
        creat_table();
    }
}
