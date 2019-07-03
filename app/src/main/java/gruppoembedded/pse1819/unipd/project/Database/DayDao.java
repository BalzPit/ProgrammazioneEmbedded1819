package gruppoembedded.pse1819.unipd.project.Database;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface DayDao {
    @Insert(onConflict = REPLACE)
    void insertDay(Day giorno);

    @Query("SELECT * FROM Day")
    List<Day> loadAllDays();

    //in questo modo posso fare ricerche mirate, per id o testo
    @Query("SELECT * FROM Day WHERE anno LIKE :anno AND mese LIKE:mese AND giorno LIKE:giorno ")
    List<Day> findDayWithName(int anno, int mese, int giorno);

    //niente delete per i giorni
}
