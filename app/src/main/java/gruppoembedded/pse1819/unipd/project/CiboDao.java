package gruppoembedded.pse1819.unipd.project;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import static androidx.room.OnConflictStrategy.REPLACE;
@Dao
public interface CiboDao {
    @Insert(onConflict = REPLACE)
    void insertCibo(Cibo cibo);

    @Query("SELECT * FROM Cibo")
    List<Cibo> loadAllCibi();

    //in questo modo posso fare ricerche mirate, per id o testo
    @Query("SELECT * FROM Cibo WHERE id LIKE :search ")
    List<Cibo> findCiboWithName(String search);


    @Query("DELETE FROM Cibo")
    void deleteAll();

    @Delete
    void deleteCibo(Cibo n);
}
