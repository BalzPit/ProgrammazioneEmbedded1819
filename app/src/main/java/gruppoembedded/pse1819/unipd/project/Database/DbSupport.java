package gruppoembedded.pse1819.unipd.project.Database;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import org.json.JSONObject;

import java.sql.Date;
import java.util.Calendar;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;


public class DbSupport extends AppCompatActivity {
    private static final String TAG = "DbSupport";

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
    public void inserimento(String pietanza, String pasto, Date date){
        //tiro fuori le informazioni dalla data
        int year= date.getYear();
        int month= date.getMonth();
        int day= date.getDay();
        try {
            //se il pasto non esiste viene lanciata un'eccezione
            Meal pastoAttuale = getDatabaseManager().noteModelMeal()
                    .findMealWithName(pasto, year, month, day).get(0);
            Log.i(TAG, "l'elemento esiste");

            //creo Json con i cibiDiOggi
            JSONObject cibo = new JSONObject();
            Log.i(TAG, "elementi nel pasto considerato: "+pastoAttuale.cibiDiOggi);

            //aggiungo nuovo elemento cibo e aggiorno il database (NB: l'elemento pastoAttuale sostituisce quello precedente)
            cibo.put("nome",pietanza);
            cibo.put("quantità","-");

            //esiste la possibilità che la lista sia vuota, ad esempio dopo una rimozione
            if(pastoAttuale.cibiDiOggi.equals("")){
                pastoAttuale.cibiDiOggi = cibo.toString();
            }
            else{
                pastoAttuale.cibiDiOggi=pastoAttuale.cibiDiOggi +","+ cibo.toString();
            }
            getDatabaseManager().noteModelMeal().insertMeal(pastoAttuale);

            } catch (Exception e) {
                //in realtà questa non dovrebbe mai essere lanciata, dal momento che il primo elemento
                //viene inserito correttamente, vedi sotto
                Log.i(TAG, "insert: eccezzione nella creazione di JSONObject: " + e);
            }
        Log.i(TAG, "inserita la pietanza");
    }



    // this method is used to safely get a meal from the database and, in case it doesn't exist,
    // create a new one with the specified attributes.
    public Meal identificaPasto(String pasto, Date date){
        Meal meal = null;

        //tiro fuori le informazioni dalla data
        int year= date.getYear();
        int month= date.getMonth();
        int day= date.getDay();
        Log.i(TAG, "data: "+date.toString()+" -> "+year+"/"+month+"/"+day);
        try {
            //se il pasto non esiste viene lanciata un'eccezione
            meal = getDatabaseManager().noteModelMeal()
                    .findMealWithName(pasto, year, month, day).get(0);
            Log.i(TAG, "l'elemento esiste");
        }
        catch(Exception manca) {
            Log.i(TAG, "eccezione per mancanza elmento pasto: "+manca);

            //perciò ne creo uno nuovo
            meal = new Meal();
            meal.nome = pasto;
            meal.year = year;
            meal.month = month;
            meal.day = day;
            meal.cibiDiOggi="";
            //inserisco il pasto nella tabella corrispondente
            getDatabaseManager().noteModelMeal().insertMeal(meal);

            Log.i(TAG, "creato nuovo pasto e inserito nel db: "
                    +meal.nome+" del giorno "+meal.year+"/"+meal.month+"/"+meal.day+", data: "+date.toString());
        }
        Log.i(TAG, "pasto trovato: "+meal);
        return meal;
    }

    // this method checks if the date of a given day is
    // in the database or not: if not the date gets added.
    public void dateControl(Date todaysDate){

        int year= todaysDate.getYear();
        int month= todaysDate.getMonth();
        int day= todaysDate.getDay();

        Day newDay;

        //check if it is in the database
        try {

            //se non esiste un Day relativo a quella data viene lanciata un'eccezione
            newDay= getDatabaseManager().noteModelDay().findDayWithName(year,month,day).get(0);

        }catch(Exception notfound){
            Log.i(TAG, "eccezione per mancanza Day: "+notfound);

            //inserisco il nuovo elemento nel db

            newDay = new Day();
            newDay.anno = year;
            newDay.mese = month;
            newDay.giorno = day;

            getDatabaseManager().noteModelDay().insertDay(newDay);
        }
    }
}
