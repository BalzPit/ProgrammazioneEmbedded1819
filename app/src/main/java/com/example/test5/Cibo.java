package com.example.test5;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Cibo")
public class Cibo {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    public long id;

    @NonNull
    String text;
    @Override
    public String toString() {
        return String.format("%s (id = %d)", text, id);
    }
}
