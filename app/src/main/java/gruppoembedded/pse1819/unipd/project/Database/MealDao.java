package gruppoembedded.pse1819.unipd.project.Database;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import static androidx.room.OnConflictStrategy.REPLACE;
@Dao
public interface MealDao {
    @Insert(onConflict = REPLACE)
    void insertMeal(Meal pasto);

    @Query("SELECT * FROM Meal")
    List<Meal> loadAllMeals();

    //in questo modo posso fare ricerche mirate, per id o nome
    @Query("SELECT * FROM Meal WHERE id LIKE :search ")
    List<Meal> findMealWithId(long search);

    @Query("SELECT * FROM Meal WHERE nome LIKE :search ")
    List<Meal> findMealWithName(String search);

    @Query("DELETE FROM Meal")
    void deleteAll();
}
