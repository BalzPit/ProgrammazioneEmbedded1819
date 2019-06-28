package gruppoembedded.pse1819.unipd.project;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity /*implements View.OnClickListener*/{

    public String title = "Situazione Attuale";

    protected int totcalories = 2700;
    protected int assumedcalories = 1500;
    protected int caloriestoget = totcalories-assumedcalories;
    int Values[] = { assumedcalories , caloriestoget };
    String Labels[]= {"ASSUNTE", "RESTANTI"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle(title);
        setupPieChart(); // chiamo il metodo che poi creerò

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
    }

    private void setupPieChart() {
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
        chart.setCenterText("TOTALE CALORIE" + " " + totcaloriesstring); //testo che compare all'internod el buco del grafico
        chart.setCenterTextColor(-16777216); //il valore -16777216 corrisponde al colore nero
        chart.setCenterTextSize(16f); //dimensione del testo all'internod el buco
        //creo un'animazione all'apertura del grafico
        chart.animateY(1500);
        chart.invalidate();
    }
}
