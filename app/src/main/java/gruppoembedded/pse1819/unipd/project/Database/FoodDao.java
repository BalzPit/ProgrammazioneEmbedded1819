package gruppoembedded.pse1819.unipd.project.Database;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface FoodDao {
    @Insert(onConflict = REPLACE)
    void insertFood(Food cibo);

    @Query("SELECT * FROM Food")
    List<Food> loadAllFood();

    //in questo modo posso fare ricerche mirate, per nome
    @Query("SELECT * FROM Food WHERE nome LIKE :search ")
    Food findFoodWithName(String search);


    @Query("DELETE FROM Food")
    void deleteAll();

    @Delete
    void deleteFood(Food n);
}
