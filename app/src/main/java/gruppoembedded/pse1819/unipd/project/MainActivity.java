package gruppoembedded.pse1819.unipd.project;

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
import android.widget.TextView;

import com.example.test5.R;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivityPrinc";
    Dialog myDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //qui decido il titolo della tabella
        TextView tv= (TextView)findViewById(R.id.textList);
        tv.setText(titolo());

        //inserire un po' di dati nel db, PROVVISORIO
        //inserisci();

        //metodo per compilare tabella
        creat_table();
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
        String[] nameproducts =trovaProdottiConDb();

        // definisco un ArrayList
        final ArrayList<String> listp = new ArrayList<String>();
        for (int i = 0; i < nameproducts.length; ++i) {
            listp.add(nameproducts[i]);
        }

        // recupero la lista dal layout
        final ListView mylist = (ListView) findViewById(R.id.listView1);

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
    private com.example.test5.CiboDb db;
    private com.example.test5.CiboDb getDatabaseManager(){
        if(db==null)
            db= CiboDb.getDatabase(this);
        return db;
    }
    //prelevo dati
    private String[] trovaProdottiConDb(){
        //ottengo dati dal db
        List<com.example.test5.Cibo> dati=getDatabaseManager().noteModel().loadAllCibi();
        String[] lista=new String[dati.size()];

        //per ogni oggetto in List<Cibo> ricavo il testo
        for(int i=0;i<dati.size();i++){
            com.example.test5.Cibo elem=dati.get(i);
            Log.d(TAG, "trovaProdottiConDb: lista: "+elem.text);
            lista[i]=elem.text;
        }
        return lista;
    }
    //inserisco un po' di dati a caso nel db
    private void inserisci(){
        com.example.test5.Cibo elemento= new Cibo();
        elemento.text="pasta";
        getDatabaseManager().noteModel().insertCibo(elemento);
        elemento.text="carne";
        getDatabaseManager().noteModel().insertCibo(elemento);
        elemento.text="yogurt";
        getDatabaseManager().noteModel().insertCibo(elemento);
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
        TextView text1 = (TextView) dialog.findViewById(R.id.txtclose);
        text1.setText("X");

        Button dialogButton = (Button) dialog.findViewById(R.id.btnclose);
        // se viene premuto il pulsante, chiudere il pop-up
        // NB! il popup viene chiuso anche se vi si preme fuori, ma le azioni di chiusura vengono eseguite solo in caso di pressione pulsante
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: azioni prima della chiusura");
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    //metodo di accesso alla lista di selezione cibi
    public void scegli(View view){
        Button add=findViewById(R.id.add);
        Intent aggiungi=new Intent(view.getContext(), InsertAct.class);
        startActivityForResult(aggiungi,0);
    }
}
