package com.example.rashasaadeh.notes;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashSet;

import static android.R.id.input;


public class MainActivity extends AppCompatActivity {

    static ArrayList<String> notes = new ArrayList<>(); //need an arraylist to link to our list view, and need to access it from all parts of the app, make static to access from NoteEditorActivity class
    static  ArrayAdapter arrayAdapter;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //this method creates the menu on main activity

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_note_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
       super.onOptionsItemSelected(item);

        if(item.getItemId() == R.id.add_note);
        {
            Intent intent = new Intent(getApplicationContext(), NoteEditorActivity.class); //if we clicked on add_note, then we want to jump to NoteEditorActivity

            startActivity(intent); //not provide a noteId
            return true; //return true if appropriate item is selected
        }


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listView = (ListView) findViewById(R.id.listVIew);

        //when we displayed content if the listView we need to check to see if there is a set, if there is then display it
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.example.rashasaadeh.notes", Context.MODE_PRIVATE);

        HashSet<String> set = (HashSet<String>) sharedPreferences.getStringSet("notes", null);

        if(set == null)
        {
            notes.add("Example note");
        } else {
            //create a new ArrayList based on the set
            notes = new ArrayList<>(set);
        }


        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, notes);

        listView.setAdapter(arrayAdapter);

        //when item on list is clicked
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent(getApplicationContext(), NoteEditorActivity.class); //allow you to jump to the Note Editor acitivty
                intent.putExtra("noteId", i); //lets the new activity know which item was tapped on listView
                startActivity(intent);
            }
        });

        //delete note after long click
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                final int itemToDelete = i;

                new AlertDialog.Builder(MainActivity.this) //confirm that user wants to delete their note, change getApplicationContext to MainActivity.this so that MainActivity is changed
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Are you sure")
                        .setMessage("Do you want to delte this note?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                               //user tapped "Yes"
                                notes.remove(itemToDelete); //removes the note that we want to delete in the ArrayList, change i -> itemToDelete to prevent a crash from assuming it is onClick i parameter
                                arrayAdapter.notifyDataSetChanged();

                                //set is updated when note is deleted
                                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.example.rashasaadeh.notes", Context.MODE_PRIVATE);

                                HashSet<String> set = new HashSet<String>(MainActivity.notes);

                                sharedPreferences.edit().putStringSet("notes",set).apply(); //sharedPreferences can work with sets
                            }
                        })
                        .setNegativeButton("No", null) //do nothing if user taps no
                        .show();

                return true; //change return false to return true so that long click is not assumed that we want to do short click as well

            }

        });
    }
}
