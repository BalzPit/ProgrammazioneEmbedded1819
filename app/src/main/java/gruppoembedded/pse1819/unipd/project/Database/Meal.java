package gruppoembedded.pse1819.unipd.project.Database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Meal {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    public long id;

    @NonNull
    public String nome;

    @Override
    public String toString() {
        return String.format("%s (id = %d)", nome, id);
    }

    //questo sarà l'elenco dei cibi associati ad oggi, un json?
    public String cibiDiOggi;
}