package com.flosebastian.app.biblepicker;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class BibleListPickerTest extends AppCompatActivity implements VersesListPicker.OnFragmentInteractionListener, DialogFragmentShowVerses.OnFragmentInteractionListener, DialogVerseListPicker.OnFragmentInteractionListener {

    private DialogFragmentShowVerses m_showVersesDialog     = null;
    private DialogVerseListPicker m_verseListPicker         = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bible_list_picker_test);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showVerseListPickerDialog();
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
            }
        });
    }

    @Override
    public void onSelectVersesButtonClicked(String versesTitle, String versesStr) {
        showVersesDialog(versesTitle, versesStr);

        return;
    }

    public void showVersesDialog(String title, String content){
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        // Create and show the dialog.
        m_showVersesDialog = DialogFragmentShowVerses.newInstance(title, content);
        m_showVersesDialog.show(ft, "dialog");
    }

    public void showVerseListPickerDialog(){
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        // Create and show the dialog.
        m_verseListPicker = DialogVerseListPicker.newInstance("", "");
        //m_verseListPicker.show(ft, "dialog");
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
