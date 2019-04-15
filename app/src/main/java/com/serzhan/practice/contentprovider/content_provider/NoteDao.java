package com.serzhan.practice.contentprovider.content_provider;

import android.database.Cursor;

import com.serzhan.practice.contentprovider.database.NoteDatabase;
import com.serzhan.practice.contentprovider.entity.Note;

import java.util.List;

public class NoteDao {

    private NoteDatabase database;

    public NoteDao(NoteDatabase database) {
        this.database = database;
    }

    public long addNote(Note note) {
       return database.getNoteDao().insert(note);
    }

    public int updateNote(Note note) {
        return database.getNoteDao().update(note);
    }

    public List<Note> getNotes() {
        return database.getNoteDao().getNotes();
    }

    public Cursor selectAll() { return database.getNoteDao().selectAll(); }

    public Note getNoteById(long id) {
        return database.getNoteDao().getNoteByID(id);
    }

    public Cursor getNote(long id) {
        return database.getNoteDao().getNote(id);
    }
}
