package com.serzhan.practice.contentprovider;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;

import com.serzhan.practice.contentprovider.content_provider.MyContentProvider;
import com.serzhan.practice.contentprovider.database.NoteDatabase;
import com.serzhan.practice.contentprovider.entity.Note;
import com.serzhan.practice.contentprovider.utils.ConvertUtils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NoteActivity extends AppCompatActivity {

    private static String EXTRA_NOTE = "extra_note";

    private EditText mEditText;
    private NoteDatabase db;
    private ExecutorService executorService;
    private Handler handler;
    private Note note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        mEditText = findViewById(R.id.activity_note_content_edit_text);
        executorService = Executors.newSingleThreadExecutor();
        db = NoteDatabase.getInstance(NoteActivity.this);
        handler = new Handler(Looper.getMainLooper());
        getNote();
    }

    private void getNote() {
        note = (Note) getIntent().getSerializableExtra(EXTRA_NOTE);
        mEditText.setText(note.getContent());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_note_apply, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.activity_note_menu_apply_item: {
                updateNote();
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateNote() {
        executorService.execute(() -> {
            note.setContent(mEditText.getText().toString());
            getContentResolver().update(
                    MyContentProvider.CONTENT_URI,
                    ConvertUtils.getNoteContentValues(note),
                     null,
                     null
            );
            finish();
        });
    }

    public static Intent newIntent(Context context, Note note) {
        Intent intent = new Intent(context, NoteActivity.class);
        intent.putExtra(EXTRA_NOTE, note);
        return intent;
    }
}
