package gruppoembedded.pse1819.unipd.project.Database;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Cibo")
public class Cibo {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    public long id;

    @NonNull
    public String text;
    @Override
    public String toString() {
        return String.format("%s (id = %d)", text, id);
    }
}
