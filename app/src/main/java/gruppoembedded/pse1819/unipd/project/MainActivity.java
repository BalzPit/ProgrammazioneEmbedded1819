package gruppoembedded.pse1819.unipd.project;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import gruppoembedded.pse1819.unipd.project.Database.DbSupport;
import gruppoembedded.pse1819.unipd.project.Database.DietDb;
import gruppoembedded.pse1819.unipd.project.Database.Food;
import gruppoembedded.pse1819.unipd.project.Database.Meal;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity /*implements View.OnClickListener*/{

    public String title = "Situazione Attuale";
    public DbSupport support= new DbSupport(this);

    protected int totcalories = 2700;
    private int assumedcalories;
    protected int caloriestoget;

    String Labels[]= {"ASSUNTE", "RESTANTI"};

    private static final String TAG = "MainActivityTag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle(title);
        assumedcalories=calories();
        caloriestoget = totcalories-assumedcalories;
        int Values[] = { assumedcalories , caloriestoget };
        setupPieChart(Values); // chiamo il metodo che poi creerò

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
                startActivity(intPranzo);
            }
        });

        colazione.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intColazione=new Intent(v.getContext(), MealActivity.class);
                intColazione.putExtra("username","Colazione");
                startActivity(intColazione);
            }
        });

        cena.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intCena=new Intent(v.getContext(), MealActivity.class);
                intCena.putExtra("username","Cena");
                startActivity(intCena);
            }
        });

        snacks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intSnacks=new Intent(v.getContext(), MealActivity.class);
                intSnacks.putExtra("username","Snacks");
                startActivity(intSnacks);
            }
        });

        //all'apertura dell'app verifico se la tabella Food è vuota => prima volta che apro l'app, in tal caso la riempio
        istantiateFood();
    }

    private void setupPieChart(int Values[]) {
        //creazione metodo
       //Popolo una lista di PieEntries
        List<PieEntry> pieEntries = new ArrayList<>();
        //devo popolare ora il mio Chart con i dati, quindi per ogni pezzo del mio grafico a torta devo avere un'entry.
        // o nominato le mie entries (in riga 14 e 15 ) devo fare il pair per ogni elemento del vettore (riga 14) con ogni
        // elemento del vettore di riga 15
        for (int i = 0; i < Values.length; i++ ) {
            pieEntries.add(new PieEntry(Values[i],Labels[i]));
        }

        PieDataSet dataSet = new PieDataSet(pieEntries, "" );
        //cambio il colore di ogni "fetta" del grafico a torta, altrimenti resterebbero tutte con colori uguali.
        dataSet.setColors( ColorTemplate.JOYFUL_COLORS );
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
        String totcaloriesstring = Integer.toString(totcalories);
        chart.setCenterText("TOTALE CALORIE" + " " + totcaloriesstring); //testo che compare all'internod del buco del grafico
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
            int valoreNutrizionale=findCalories(cibiDelGiorno[i][0]);

            //eseguo calcolo calorie
            calTot= (int) (calTot+(quantità*valoreNutrizionale)/1000);
        }


        //Log.i(TAG, "elemento di prova: "+support.getDatabaseManager().noteModelFood().findFoodWithName("sushi").get(0).nome);

        return calTot;
    }

    private String[][] findFoods(){
        //prendo tutti i pasti della giornata

        List<Meal> dati=support.getDatabaseManager().noteModelMeal().loadAllMeals();

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
        List<Food> listaCibi=support.getDatabaseManager().noteModelFood().loadAllFood();
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
        }
        return (int)mioCibo.KcalPerUnit;
    }

    private void istantiateFood(){
        //l'istanziazione viene fatta solo se è la prima volta che l'app viene installata, e quindi non esiste anoora il database
        if(support.getDatabaseManager().noteModelFood().loadAllFood().size()==0) {
            Log.i(TAG, "onCreate: sto eseguendo istanziazione");

            Food elemento = new Food();
            elemento.nome = "chocolate_cake";
            elemento.KcalPerUnit = 150;
            support.getDatabaseManager().noteModelFood().insertFood(elemento);
            elemento.nome = "chicken_wings";
            elemento.KcalPerUnit = 90;
            support.getDatabaseManager().noteModelFood().insertFood(elemento);
            elemento.nome = "cheescake";
            elemento.KcalPerUnit = 400;
            support.getDatabaseManager().noteModelFood().insertFood(elemento);
            elemento.nome = "donuts";
            elemento.KcalPerUnit = 50;
            support.getDatabaseManager().noteModelFood().insertFood(elemento);
            elemento.nome = "pizza";
            elemento.KcalPerUnit = 900;
            support.getDatabaseManager().noteModelFood().insertFood(elemento);
            elemento.nome = "risotto";
            elemento.KcalPerUnit = 200;
            support.getDatabaseManager().noteModelFood().insertFood(elemento);
            elemento.nome = "spaghetti_bolognese";
            elemento.KcalPerUnit = 130;
            support.getDatabaseManager().noteModelFood().insertFood(elemento);
            elemento.nome = "sushi";
            elemento.KcalPerUnit = 100;
            support.getDatabaseManager().noteModelFood().insertFood(elemento);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //aggiornamento grafico con nuovo valore calorie
        assumedcalories=calories();
        caloriestoget = totcalories-assumedcalories;
        int Values[] = { assumedcalories , caloriestoget };
        setupPieChart(Values);
    }
}
