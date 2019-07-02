package gruppoembedded.pse1819.unipd.project.Database;


import java.sql.Date;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

//il nuovo giorno dovrà essere creato all'apertura dell'app => accesso al calendario
@Entity(primaryKeys = {"giorno","mese","anno"})
public class Day {

    @NonNull
    public int giorno;

    @NonNull
    public int mese;

    @NonNull
    public int anno;

    @Override
    public String toString() {
        Date date= new Date(anno,mese,giorno);
        return date.toString();
    }
}