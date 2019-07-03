package gruppoembedded.pse1819.unipd.project;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import gruppoembedded.pse1819.unipd.project.Database.Day;
import gruppoembedded.pse1819.unipd.project.Database.DbSupport;
import gruppoembedded.pse1819.unipd.project.Database.DietDb;
import gruppoembedded.pse1819.unipd.project.Database.Food;
import gruppoembedded.pse1819.unipd.project.Database.Meal;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.widget.Toolbar;

import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;


import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Date;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final int GET_DATE = 1;
    public String title;
    public DbSupport support= new DbSupport(this);

    //the user has the ability to select different days
    public Date currentDate = new Date (Calendar.getInstance().getTimeInMillis());
    DateParcelable dateParcelable;


    private int totcalories;
    private int assumedcalories;
    protected int caloriestoget;

    String Labels[]= {"ASSUNTE", "RESTANTI"};

    protected EditText number; // inizializzo il numero di calorie

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //all'apertura dell'app verifico se la tabella Food è vuota => prima volta che apro l'app, in tal caso la riempio
        istantiateFood();

        setContentView(R.layout.activity_main);
        Log.i(TAG, "Data corrente: "+currentDate.toString());

        //insert date into the database if it is not already
        support.dateControl(currentDate);

        //get calories of that day
        Day selectedDay = support.getDatabaseManager().noteModelDay()
                .findDayWithName(currentDate.getYear(), currentDate.getMonth(), currentDate.getDay()).get(0);

        totcalories = selectedDay.calorie;

        // prendo il numero di calorie dalla EditText
        number = (EditText)findViewById(R.id.editcalories);
        number.setText(Integer.toString(totcalories));

        number.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event){
                if (actionId== EditorInfo.IME_ACTION_DONE){
                    //salva nel database il numero delle calorie appena inserito
                    int calorie = Integer.parseInt(number.getText().toString());
                    Day selectedDay = support.getDatabaseManager().noteModelDay()
                            .findDayWithName(currentDate.getYear(), currentDate.getMonth(), currentDate.getDay()).get(0);
                    selectedDay.calorie = calorie;
                    support.getDatabaseManager().noteModelDay().insertDay(selectedDay);
                    //relaunch the activity with a new instance
                    recreate();
                    return true;
                }
                return false;
            }
        });

        String valuecalories = number.getText().toString();

        title = currentDate.toString();
        setTitle(title);

        // chiamo il metodo che riempie il grafico
        setupPieChart();

        //imposta l'ActionBar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.calendar_24dp);

        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(MainActivity.this, DateSelectionActivity.class);
                startActivityForResult(intent,GET_DATE);
            }
        });

        //gestione del passaggiodella data alle altre activities
        //pass the date so the system can decide which meal to add the selected food to
        dateParcelable= new DateParcelable(currentDate);


        //inizializzazione dei bottoni
        Button pranzo = findViewById(R.id.pranzo);
        Button cena = findViewById(R.id.cena);
        Button colazione = findViewById(R.id.colazione);
        Button snacks = findViewById(R.id.snack);

        //collego i bottoni
        pranzo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intPranzo=new Intent(v.getContext(), MealActivity.class);
                intPranzo.putExtra("username","Pranzo");
                intPranzo.putExtra("date", dateParcelable);
                startActivity(intPranzo);
            }
        });

        colazione.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intColazione=new Intent(v.getContext(), MealActivity.class);
                intColazione.putExtra("username","Colazione");
                intColazione.putExtra("date", dateParcelable);
                startActivity(intColazione);
            }
        });

        cena.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intCena=new Intent(v.getContext(), MealActivity.class);
                intCena.putExtra("username","Cena");
                intCena.putExtra("date", dateParcelable);
                startActivity(intCena);
            }
        });

        snacks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intSnacks=new Intent(v.getContext(), MealActivity.class);
                intSnacks.putExtra("username","Snacks");
                intSnacks.putExtra("date", dateParcelable);
                startActivity(intSnacks);
            }
        });
    }





    private void setupPieChart() {
        //prendo le informazioni
        assumedcalories=calories();
        caloriestoget = totcalories-assumedcalories;
        if (caloriestoget<0){
            //per non buggare il grafico
            caloriestoget = 0;
        }
        int Values[] = { assumedcalories , caloriestoget };

        //Popolo una lista di PieEntries
        List<PieEntry> pieEntries = new ArrayList<>();
        //devo popolare ora il mio Chart con i dati, quindi per ogni pezzo del mio grafico a torta devo avere un'entry.
        // o nominato le mie entries (in riga 14 e 15 ) devo fare il pair per ogni elemento del vettore (riga 14) con ogni
        // elemento del vettore di riga 15
        for (int i = 0; i < Values.length; i++ ) {
            pieEntries.add(new PieEntry(Values[i],Labels[i]));
        }

        //int[] colors = {R.color.colorPrimary, R.color.colorAccent};

        PieDataSet dataSet = new PieDataSet(pieEntries, "" );
        //cambio il colore di ogni "fetta" del grafico a torta, altrimenti resterebbero tutte con colori uguali.
        dataSet.setColors(ColorTemplate.JOYFUL_COLORS);
        PieData data = new PieData(dataSet);


        //Per costruire l'interfaccia
        PieChart chart = (PieChart) findViewById(R.id.chart);
        chart.setUsePercentValues(false); //voglio che compaia il valore effettivo delle calorie
        chart.getDescription().setText(title); // specifico la descrizione del grafico
        chart.getDescription().setTextSize(20f); // ne imposto la dimensione
        chart.setData(data);
        data.setValueTextSize(20);
        chart.setEntryLabelTextSize(12f); // settaggio della dimensione del testo riferito al valore della fetta di torta
        chart.setEntryLabelColor(-1); // il valore -1 corrisponde al colore bianco
        chart.setCenterText("TOTALE CALORIE"); //testo che compare all'internod del buco del grafico
        chart.setCenterTextColor(-16777216); //il valore -16777216 corrisponde al colore nero
        chart.setCenterTextSize(16f); //dimensione del testo all'internod el buco
        //creo un'animazione all'apertura del grafico
        chart.animateY(1500);
        chart.invalidate();
    }





    public int calories(){
        //creo una matrice con tutti i cibi consumati in un giorno, il loro nome e la quantità
        String[][] cibiDelGiorno=findFoods();

        //per ogni cibo prendo la quantità ed eseguo il calcolo delle calorie
        int calTot=0;
        int quantità=0;
        for(int i=0; i<cibiDelGiorno.length; i++){
            try{
                //questo metodo può lanciare eccezzione quando un cibo viene salvato senza la quantità
                //in tal caso viene inserito il carattere "-", che non può essere trasformato in numero
                //tale pietanza non rientra quindi nel computo delle calorie
                quantità=Integer.parseInt(cibiDelGiorno[i][1]);
            }catch(Exception e){
                quantità=0;
            }

            Log.i(TAG, "nome cibo trovato: " + cibiDelGiorno[i][0]);

            //trovo il valore nutrizionale del cibo in esame
            if(cibiDelGiorno[i][0]!=null) {  //è null quando ho un pasto vuoto
                int valoreNutrizionale = findCalories(cibiDelGiorno[i][0]);
                //eseguo calcolo calorie
                calTot= (int) (calTot+(quantità*valoreNutrizionale)/100);
            }
        }
        //Log.i(TAG, "elemento di prova: "+support.getDatabaseManager().noteModelFood().findFoodWithName("sushi").get(0).nome);

        return calTot;
    }





    private String[][] findFoods(){
        //prendo tutti i pasti della giornata
        List<Meal> dati= support.getDatabaseManager().noteModelMeal()
                .findMealsOfDay(currentDate.getYear(), currentDate.getMonth(),currentDate.getDay());

        //per ognuno prelevo tutti i cibi
        String tuttiCibi="";
        //fondo tutte le stringhe di cibi dei pasti di oggi per elaborarle in una volta sola
        for (int i = 0; i < dati.size(); i++) {
            if(tuttiCibi.equals("")){
                tuttiCibi=dati.get(i).cibiDiOggi;
            }
            else{
                tuttiCibi=tuttiCibi + "," + dati.get(i).cibiDiOggi;
            }
        }
        Log.i(TAG, "creata lista completa: " + tuttiCibi);

        //inserisco tutti i cibi e le rispettive quantità in un array
        String[][] cibiDelGiorno=new String[0][0];
        try { //da catturare l'eccezione del JSONArray
            JSONArray cibiArr = new JSONArray("[" + tuttiCibi + "]");
            cibiDelGiorno=new String[cibiArr.length()][2];
            //inserimento
            for(int i=0;i<cibiArr.length();i++){
                JSONObject obj = new JSONObject(cibiArr.get(i).toString());
                //ogni elemento x della lista è un array di 2 elementi
                String[] x = new String[2];
                x[0] = obj.getString("nome");
                x[1] = obj.getString("quantità");
                cibiDelGiorno[i] = x;
            }
        }catch(Exception e){}

        return cibiDelGiorno;
    }




    private int findCalories(String nome){
        //carico la lista di tutti i cibi salvati a database
        /*List<Food> listaCibi=support.getDatabaseManager().noteModelFood().loadAllFood();
        Food mioCibo=new Food();

        //cerco il nome del cibo che mi interessa nella lista
        for(int n=0;n<listaCibi.size();n++){
            Food elem=listaCibi.get(n);

            if(elem.nome.compareToIgnoreCase(nome)==0){
                //se il nome combacia con quello che cerco lo salvo
                mioCibo=elem;
                Log.i(TAG, "calorie elem trovato: " + mioCibo.KcalPerUnit + "  nome: "+ mioCibo.nome);
                break;
            }
        }*/

        //cerco il cibo corretto nel db ed estraggo le Kcal... sistema molto più semplice
        Food elem = support.getDatabaseManager().noteModelFood().findFoodWithName(nome);
        Log.i(TAG, "test su cibi: " + elem.KcalPerUnit);

        return (int)elem.KcalPerUnit;
    }





    @Override
    protected void onResume() {
        super.onResume();

        //aggiornamento grafico con nuovo valore calorie
        setupPieChart();
    }





    // override of OnActivityResult to get the selected date
    // that was passed by DateSelectionActivity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GET_DATE){
            if (resultCode == RESULT_OK){

                dateParcelable = data.getParcelableExtra("date");
                currentDate = dateParcelable.getDate();

                title = currentDate.toString();
                setTitle(title);

                Day selectedDay = support.getDatabaseManager().noteModelDay()
                        .findDayWithName(currentDate.getYear(), currentDate.getMonth(), currentDate.getDay()).get(0);

                totcalories = selectedDay.calorie;

                number.setText(Integer.toString(totcalories));

                Log.i(TAG, "Data corrente: "+currentDate.toString());
            }
        }
    }





    private void istantiateFood(){
        //l'istanziazione viene fatta solo se è la prima volta che l'app viene installata, e quindi non esiste anoora il database
        if(support.getDatabaseManager().noteModelFood().loadAllFood().size()==0) {
            Log.i(TAG, "onCreate: sto eseguendo istanziazione");

            Food elemento = new Food();
            elemento.nome = "chocolate_cake";
            elemento.KcalPerUnit = 371;
            support.getDatabaseManager().noteModelFood().insertFood(elemento);
            elemento.nome = "chicken_wings";
            elemento.KcalPerUnit = 203;
            support.getDatabaseManager().noteModelFood().insertFood(elemento);
            elemento.nome = "cheescake";
            elemento.KcalPerUnit = 321;
            support.getDatabaseManager().noteModelFood().insertFood(elemento);
            elemento.nome = "donuts";
            elemento.KcalPerUnit = 452;
            support.getDatabaseManager().noteModelFood().insertFood(elemento);
            elemento.nome = "pizza";
            elemento.KcalPerUnit = 266;
            support.getDatabaseManager().noteModelFood().insertFood(elemento);
            elemento.nome = "ramen";
            elemento.KcalPerUnit = 436;
            support.getDatabaseManager().noteModelFood().insertFood(elemento);
            elemento.nome = "risotto";
            elemento.KcalPerUnit = 359;
            support.getDatabaseManager().noteModelFood().insertFood(elemento);
            elemento.nome = "spaghetti_bolognese";
            elemento.KcalPerUnit = 430;
            support.getDatabaseManager().noteModelFood().insertFood(elemento);
            elemento.nome = "steak";
            elemento.KcalPerUnit = 271;
            support.getDatabaseManager().noteModelFood().insertFood(elemento);
            elemento.nome = "sushi";
            elemento.KcalPerUnit = 100;
            support.getDatabaseManager().noteModelFood().insertFood(elemento);
        }
    }

}
