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

    //serve perché così posso sapere se la tabella dei cibi è già stata creata
    @NonNull
    int instanziato=0;
}
