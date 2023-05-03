package com.example.factsaboutnumbers.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "numbers_table", indices = {@Index(value = "fact", unique = true)})
public class Number {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @NonNull
    private int number;

    @NonNull
    private String fact;

    public Number(int number, String fact) {
        this.number = number;
        this.fact = fact;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getFact() {
        return fact;
    }

    public void setFact(String fact) {
        this.fact = fact;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @NonNull
    @Override
    public String toString() {
        return "Number: id = " + id + ", number = " + number + ", fact = " + fact;
    }
}
