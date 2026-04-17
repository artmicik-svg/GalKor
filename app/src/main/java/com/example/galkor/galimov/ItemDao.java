package com.example.galkor.galimov;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;

@Dao
public interface ItemDao {
    @Query("SELECT * FROM items ORDER BY createdAt DESC")
    List<Item> getAllItems();

    @Query("SELECT * FROM items WHERE isFavorite = 1 ORDER BY createdAt DESC")
    List<Item> getFavoriteItems();

    @Insert
    long insert(Item item);

    @Update
    void update(Item item);

    @Delete
    void delete(Item item);

    @Query("SELECT * FROM items WHERE id = :id")
    Item getItemById(long id);
}