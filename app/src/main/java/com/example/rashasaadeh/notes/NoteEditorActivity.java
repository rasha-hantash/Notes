package com.example.rashasaadeh.notes;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.util.HashSet;

public class NoteEditorActivity extends AppCompatActivity {

    int noteId; //variable is here so that noteId can be changed to add a new note

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_editor);

        EditText editText = (EditText) findViewById(R.id.editText);

        Intent intent = getIntent(); //uses the intent that was used to get this activity
         noteId = intent.getIntExtra("noteId", -1); //give it a default value of -1 which is impossible because list view starts at 0

        //checks for valid noteId
        if(noteId != -1)
        {
            editText.setText(MainActivity.notes.get(noteId));
        } else {
            //prepare note editor activity to add a new note
            MainActivity.notes.add(""); //creates an empty note
            noteId = MainActivity.notes.size() - 1; //a noteId to access the new note
            MainActivity.arrayAdapter.notifyDataSetChanged(); //update the ArrayAdapter to display in the listVIew

        }



        //used for while text is being changed, save content of a note
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //charSequence is the context of the text

                MainActivity.notes.set(noteId, String.valueOf(charSequence)); //update notes arraylist from main activity, noteId is the integer of the item of the int i, and we want to change it to charSequence
                MainActivity.arrayAdapter.notifyDataSetChanged();  //update the listVIew itself via the ArrayAdapter

                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.example.rashasaadeh.notes", Context.MODE_PRIVATE);

                HashSet<String> set = new HashSet<String>(MainActivity.notes);

                sharedPreferences.edit().putStringSet("notes",set).apply(); //sharedPreferences can work with sets


            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }
}
