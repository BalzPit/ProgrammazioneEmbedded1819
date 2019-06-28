package gruppoembedded.pse1819.unipd.project.Database;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

//il nuovo giorno dovrà essere creato all'apertura dell'app => accesso al calendario
@Entity
public class Day {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    public long id;

    @NonNull
    String numero;
    @NonNull
    String mese;
    @NonNull
    String anno;

    String colazione;
    String pranzo;
    String spuntino;
    String cena;
}
