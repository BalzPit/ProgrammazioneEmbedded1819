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
                String[][] nameproducts = trovaProdottiConDb(titolo());
                int position = nameproducts.length - 1;
                ListView mylist = findViewById(R.id.selectedfoodslistView);

                //create illusion of having the list item selected
                mylist.requestFocusFromTouch();
                mylist.setSelection(position);

                //perform a click on the correct item
                mylist.performItemClick(mylist.getChildAt(position),
                        position,
                        mylist.getAdapter().getItemId(position));

                //con il nome della pietanza posso accedere al metodo che attua il salvataggio
                String pietanza = data.getStringExtra("piet");
                Log.d(TAG, "qui posso prendere l'intent: "+ pietanza);
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
        //String[] nameproducts =trovaProdottiConDb(titolo());
        String[][] nameproducts = trovaProdottiConDb(titolo());

        // definisco un ArrayList
        /*final ArrayList<String> listp = new ArrayList<String>();
        for (int i = 0; i < nameproducts.length; ++i) {
            listp.add(nameproducts[i]);
        }*/

        ArrayList<Object> lista= new ArrayList<>();
        //inserisco gli elementi nella lista da passare all'adapter
        for(int i=0; i< nameproducts.length; i++){
            lista.add(nameproducts[i]);
        }

        // recupero la lista dal layout
        final ListView mylist = (ListView) findViewById(R.id.selectedfoodslistView);

        // creo e istruisco l'adattatore
        //final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listp);
        final FoodListAdapter adapter= new FoodListAdapter(this, R.layout.adapter_layout, lista);

        // inietto i dati
        mylist.setAdapter(adapter);

        //gestisco il tocco sulle righe
        mylist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //qui metodo per modificare righe, usare i metodi dell'adattatore per accedere ai dati delle righe
                String a= adapter.name(position);
                Log.d(TAG, "onItemClick: cliccato"+"  "+a);
                onClick(view, a, position);
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

    //prelevo dati dei cibi associati al pasto di un giorno
    private String[][] trovaProdottiConDb(String pasto){
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
        String[][] lista=new String[0][0];

        //estrazione cibi
        try {
            //JSONObject cibi = new JSONObject(mioPasto.cibiDiOggi);
            if(mioPasto.cibiDiOggi!=null) {
                Log.d(TAG, "entrato "+ mioPasto.cibiDiOggi);
                JSONArray cibiArr=new JSONArray("[" + mioPasto.cibiDiOggi + "]");
                Log.d(TAG, "cibiArr: " + cibiArr);
                lista=new String[cibiArr.length()][2];

                for (int i = 0; i < cibiArr.length(); i++) {
                    JSONObject obj = new JSONObject(cibiArr.get(i).toString());
                    //ogni elemento x della lista è un array di 2 elementi
                    String[] x = new String[2];
                    x[0] = obj.getString("nome");
                    x[1] = obj.getString("quantità");
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
    public void onClick(View arg0, final String pasto, final int posizione) {

        // custom dialog
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.pop_up_layout);
        dialog.setTitle("Title...");

        // settaggio componenti pop-up
        TextView text = (TextView) dialog.findViewById(R.id.testo);
        text.setText(pasto);

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

                int selectedgrams = gramsPicker.getValue();

                //!!!!======= SAVE SELECTED GRAMS OF THAT FOOD TO DATABASE =====!!!!
                aggiornaDati(pasto, posizione, selectedgrams);

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
                delete(pasto, posizione);

                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public void aggiornaDati(String pasto, int posiz, int grammi){
        //ottengo dati dal db
        List<Meal> dati=getDatabaseManager().noteModelMeal().loadAllMeals();
        Meal mioPasto=new Meal();

        //cerco il nome del pasto che mi interessa nella lista
        for(int i=0;i<dati.size();i++){
            Meal elem=dati.get(i);
            Log.d(TAG, "inizio modifica, dati: "+titolo());

            //uso ignoreCase perché "pasto" inizia con la lettera maiuscola
            if(elem.nome.compareToIgnoreCase(titolo())==0){
                //se il nome combacia con quello che cerco lo salvo, poi estraggo tutti i cibi in esso contenuti
                mioPasto=elem;
                break;
            }
        }

        //estrazione cibi
        try {
            if(mioPasto.cibiDiOggi!=null) {

                JSONArray cibiArr=new JSONArray("[" + mioPasto.cibiDiOggi + "]");
                //NB! non salvo direttamente la stringa con le parentesi per evitare di fare un parsing
                // ogni volta che devo inserire un nuovo elemento
                Log.d(TAG, "fatto cibiArr: "+cibiArr);

                //per il nuovo elemento devo tenere lo stesso nome
                JSONObject obj = new JSONObject(cibiArr.get(posiz).toString());
                String pietanza = obj.getString("nome");

                //creo un elemento nuovo, con i grammi modificati e aggiorno i dati nell'array
                JSONObject cibo = new JSONObject();
                cibo.put("nome",pietanza);
                cibo.put("quantità",grammi);
                cibiArr.put(posiz,cibo);

                //ora devo inserire la stringa modificata nel database
                String modifica= cibiArr.toString();
                int lung=modifica.length();
                //prima bisogna togliere le parentesi quadre
                modifica=modifica.substring(1,lung-1);
                Log.d(TAG, "modifica eseguita: "+modifica);

                mioPasto.cibiDiOggi = modifica;
                getDatabaseManager().noteModelMeal().insertMeal(mioPasto);
                //invoco create_table per aggiornare la lista
                creat_table();

            }else {
                Log.d(TAG, "qualcosa non va");
            }
        }catch(Exception e){
            Log.d(TAG, "eccezzione: "+e);
        }
    }

    public void delete(String pasto, int pos){
        //ottengo dati dal db
        List<Meal> dati=getDatabaseManager().noteModelMeal().loadAllMeals();
        Meal mioPasto=new Meal();

        //cerco il nome del pasto che mi interessa nella lista
        for(int i=0;i<dati.size();i++){
            Meal elem=dati.get(i);
            Log.d(TAG, "inizio modifica, dati: "+titolo());

            //uso ignoreCase perché "pasto" inizia con la lettera maiuscola
            if(elem.nome.compareToIgnoreCase(titolo())==0){
                //se il nome combacia con quello che cerco lo salvo, poi estraggo tutti i cibi in esso contenuti
                mioPasto=elem;
                break;
            }
        }

        //estrazione cibi
        try {
            if(mioPasto.cibiDiOggi!=null) {

                JSONArray cibiArr=new JSONArray("[" + mioPasto.cibiDiOggi + "]");
                //NB! non salvo direttamente la stringa con le parentesi per evitare di fare un parsing
                // ogni volta che devo inserire un nuovo elemento
                Log.d(TAG, "fatto cibiArr: "+cibiArr);

                //per il nuovo elemento devo tenere lo stesso nome
                cibiArr.remove(pos);

                //ora devo inserire la stringa modificata nel database
                String modifica= cibiArr.toString();
                int lung=modifica.length();
                //prima bisogna togliere le parentesi quadre
                modifica=modifica.substring(1,lung-1);
                Log.d(TAG, "eliminazione eseguita: "+modifica);

                mioPasto.cibiDiOggi = modifica;
                getDatabaseManager().noteModelMeal().insertMeal(mioPasto);
                //invoco create_table per aggiornare la lista
                creat_table();

            }else {
                Log.d(TAG, "qualcosa non va");
            }
        }catch(Exception e){
            Log.d(TAG, "eccezzione: "+e);
        }
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
