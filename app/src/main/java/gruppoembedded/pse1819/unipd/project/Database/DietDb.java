package gruppoembedded.pse1819.unipd.project.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Food.class, Meal.class, Day.class}, version = 1, exportSchema = false)
public abstract class DietDb extends RoomDatabase {
    private static DietDb INSTANCE;
    public abstract FoodDao noteModelFood();
    public abstract MealDao noteModelMeal();
    public abstract DayDao noteModelDay();

    // database persistente mantiene i dati dopo la disconnessione
    public static DietDb getDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), DietDb.class, "note_db")
                    .allowMainThreadQueries()
                    .build();
        }
        return INSTANCE;
    }
    public static void destroyInstance() {
        INSTANCE = null;
    }
}
