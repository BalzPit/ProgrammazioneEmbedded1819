package com.example.test5;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Cibo.class}, version = 1)
public abstract class CiboDb extends RoomDatabase {
    private static CiboDb INSTANCE;
    public abstract CiboDao noteModel();
    // database in memory è temporaneo, viene distrutto alla chiusura dell'applicazione
    public static CiboDb getInMemoryDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.inMemoryDatabaseBuilder(context.getApplicationContext(), CiboDb.class)
                    .allowMainThreadQueries()
                    .build();
        }
        return INSTANCE;
    }
    // database persistente mantiene i dati dopo la disconnessione
    public static CiboDb getDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), CiboDb.class, "note_db")
                    .allowMainThreadQueries()
                    .build();
        }
        return INSTANCE;
    }
    public static void destroyInstance() {
        INSTANCE = null;
    }
}
