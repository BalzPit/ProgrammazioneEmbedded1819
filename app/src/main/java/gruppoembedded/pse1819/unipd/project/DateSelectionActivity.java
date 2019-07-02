package gruppoembedded.pse1819.unipd.project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import gruppoembedded.pse1819.unipd.project.Database.Day;
import gruppoembedded.pse1819.unipd.project.Database.DbSupport;
import gruppoembedded.pse1819.unipd.project.Database.Food;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class DateSelectionActivity extends AppCompatActivity {

    private static final String TAG="DateSelection";
    public DbSupport support= new DbSupport(this);

    //used to save the chosen date and pass it to MainActivity
    private DateParcelable dateParcelable;
    private Date selectedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_selection);

        //imposta l'ActionBar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //qui decido il titolo della tabella
        TextView tv= findViewById(R.id.dates);
        tv.setText(R.string.dateslist);

        createList();
    }

    //questo metodo inserisce ogni elemento salvato di Day nella lista
    // , in modo da poter essere selezionato
    private void createList() {
        // prendo le date salvate
        final List<Day> date=support.getDatabaseManager().noteModelDay().loadAllDays();

        // definisco un ArrayList di stringhe
        final ArrayList<String> listp = new ArrayList<String>();
        for (int i = 0; i < date.size(); ++i) {
            listp.add(date.get(i).toString());
        }

        // recupero la lista dal layout
        final ListView mylist = (ListView) findViewById(R.id.datesListView);

        // creo e istruisco l'adattatore
        final ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listp);

        // inietto i dati
        mylist.setAdapter(adapter);

        //gestisco il tocco sulle righe
        mylist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "onItemClick: cliccato"+"  "+parent.getItemAtPosition(position));

                //ricreo l'oggetto Date da passare a MainActivity, è nella stessa posizione "id"
                // sia nella List<Date> "date" che nella lista dei giorni "mylist"
                int year= date.get((int)id).anno;
                int month= date.get((int)id).mese;
                int day= date.get((int)id).giorno;
                selectedDate = new Date(year,month,day);

                //gestione del passaggiodella data a MainActivity activities
                //pass the date so the system can decide which meal to add the selected food to
                dateParcelable= new DateParcelable(selectedDate);

                Intent ritornaData=new Intent(DateSelectionActivity.this, MainActivity.class);
                ritornaData.putExtra("date",dateParcelable );
                setResult(Activity.RESULT_OK, ritornaData);
                finish();
            }
        });
    }
}
