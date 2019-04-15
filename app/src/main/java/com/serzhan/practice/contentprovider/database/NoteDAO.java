package com.serzhan.practice.contentprovider.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import android.database.Cursor;

import com.serzhan.practice.contentprovider.entity.Note;

import java.util.List;

@Dao
public interface NoteDAO {

    @Query("SELECT * FROM note WHERE id = :id")
    Note getNoteByID(long id);

    @Query("SELECT * FROM note WHERE id = :id")
    Cursor getNote(long id);

    @Query("SELECT * FROM note")
    Cursor selectAll();

    @Query("SELECT * FROM note")
    List<Note> getNotes();

    @Update(onConflict = OnConflictStrategy.REPLACE)
    int update(Note note);

    @Insert(onConflict = OnConflictStrategy.FAIL)
    long insert(Note note);

    @Delete
    int delete(Note note);

    @Query("DELETE FROM note WHERE id = :id")
    int deleteById(long id);
}
