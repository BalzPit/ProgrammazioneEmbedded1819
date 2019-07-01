package gruppoembedded.pse1819.unipd.project.Database;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Food {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    public long id;

    @NonNull
    public String nome;
    @Override
    public String toString() {
        return String.format("%s", nome);
    }

    @NonNull
    public long KcalPerUnit;
    //l'unità di misurazione è 100gr
}
