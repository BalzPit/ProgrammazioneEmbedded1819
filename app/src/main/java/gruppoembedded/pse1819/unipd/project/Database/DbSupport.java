package gruppoembedded.pse1819.unipd.project.Database;

import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;


public class DbSupport extends AppCompatActivity {
    private static final String TAG = "actSupport";

    private static Context context;

    public DbSupport(Context c) {
        context = c;
    }

    //  METODO PER CREARE L'ACCESSO AL DATABASE LOCALE
    public static DietDb db;
    public static DietDb getDatabaseManager(){
        if(db==null)
            db=DietDb.getDatabase(context);
        return db;
    }

    //  METODO PER INSERIRE NUOVI CIBI NEL CORRETTO PASTO DEL DATABASE
    public void inserimento(String pietanza, String pasto){
        try {
            //se il pasto non esiste viene lanciata un'eccezione
            Meal pastoAttuale = getDatabaseManager().noteModelMeal().findMealWithName(pasto).get(0);
            Log.i(TAG, "l'elemento esiste");
            //creo Json con i cibiDiOggi
            try {
                JSONObject cibo = new JSONObject();
                Log.i(TAG, "elementi nel pasto considerato: "+pastoAttuale.cibiDiOggi);

                //aggiungo nuovo elemento cibo e aggiorno il database (NB: l'elemento pastoAttuale sostituisce quello precedente)
                cibo.put("nome",pietanza);
                cibo.put("quantità","-");
                pastoAttuale.cibiDiOggi=pastoAttuale.cibiDiOggi +","+ cibo.toString();
                getDatabaseManager().noteModelMeal().insertMeal(pastoAttuale);

            } catch (Exception e) {
                //in realtà questa non dovrebbe mai essere lanciata, dal momento che il primo elemento
                //viene inserito correttamente, vedi sotto
                Log.i(TAG, "insert: eccezzione nella creazione di JSONObject: " + e);
            }

        }catch(Exception manca) {
            Log.i(TAG, "eccezione per mancanza elmento pasto: "+manca);

            //perciò ne creo uno nuovo
            Meal nuovoPasto = new Meal();
            nuovoPasto.nome = pasto;
            //una volta creato il pasto insrisco subito il primo elemento cibo
            JSONObject cibi = new JSONObject();
            try {
                cibi.put("nome", pietanza);
                cibi.put("quantità", "-");
                String cibiConvertiti=cibi.toString();
                nuovoPasto.cibiDiOggi=cibiConvertiti;
                //inserisco il pasto nella tabella corrispondente
                getDatabaseManager().noteModelMeal().insertMeal(nuovoPasto);
            }catch(Exception e){
                Log.i(TAG, "insert: eccezzione sul put: " + e);
            }
        }
    }

    public Meal identificaPasto(String nomePasto){
        //ottengo dati dal db
        List<Meal> dati=getDatabaseManager().noteModelMeal().loadAllMeals();
        Meal mioPasto=new Meal();
        Log.d(TAG, "lista pasti: "+dati);

        //cerco il nome del pasto che mi interessa nella lista
        for(int i=0;i<dati.size();i++){
            Meal elem=dati.get(i);

            //uso ignoreCase perché "pasto" inizia con la lettera maiuscola
            if(elem.nome.compareToIgnoreCase(nomePasto)==0){
                //se il nome combacia con quello che cerco lo salvo, poi estraggo tutti i cibi in esso contenuti
                mioPasto=elem;
                break;
            }
        }
        return mioPasto;
    }
}
