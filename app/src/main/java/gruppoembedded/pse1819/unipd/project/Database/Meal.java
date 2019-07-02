package gruppoembedded.pse1819.unipd.project.Database;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

//questa entità è identificata da un'insieme di valori
@Entity(primaryKeys = {"nome", "day", "month","year"})
public class Meal {

    @NonNull
    public int day;

    @NonNull
    public int month;

    @NonNull
    public int year;

    @NonNull
    public String nome;

    @Override
    public String toString() {
        return String.format("%s %d/%d/%d", nome,year,month,day);
    }

    //questo sarà l'elenco dei cibi associati ad oggi, un json?
    public String cibiDiOggi;
}