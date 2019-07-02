package gruppoembedded.pse1819.unipd.project.Database;

import java.sql.Date;
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

    //trovo tutti i pasti di una giornata
    @Query("SELECT * FROM Meal WHERE year LIKE :year AND month LIKE:month AND day LIKE:day")
    List<Meal> findMealsOfDay(int year, int month, int day);

    @Query("SELECT * FROM Meal WHERE nome LIKE :search AND year LIKE :year AND month LIKE:month AND day LIKE:day")
    List<Meal> findMealWithName(String search, int year, int month, int day);

    @Query("DELETE FROM Meal")
    void deleteAll();
}
