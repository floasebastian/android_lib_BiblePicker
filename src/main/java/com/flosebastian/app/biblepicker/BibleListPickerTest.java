package com.flosebastian.app.biblepicker;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class BibleListPickerTest extends AppCompatActivity implements VersesListPicker.OnFragmentInteractionListener, DialogFragmentShowVerses.OnFragmentInteractionListener {

    private DialogFragmentShowVerses m_showVersesDialog = null;

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
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public void onSelectVersesButtonClicked(String verseStr) {
        showVersesDialog(verseStr);

        return;
    }

    public void showVersesDialog(String content){
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        // Create and show the dialog.
        m_showVersesDialog = DialogFragmentShowVerses.newInstance(content, null);
        m_showVersesDialog.show(ft, "dialog");
    }
}
