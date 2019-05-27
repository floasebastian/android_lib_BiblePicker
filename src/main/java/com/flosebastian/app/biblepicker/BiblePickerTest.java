package com.flosebastian.app.biblepicker;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

public class BiblePickerTest extends AppCompatActivity implements VersesPicker.OnFragmentInteractionListener {

    final static float STEP = 200;
    private float m_fontSize = 13.0f;
    private float m_ratio = 1.0f;
    private int m_baseDist;
    private float m_baseRatio;
    private TextView m_textViewVerses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bible_picker_test);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView textViewTitle = (TextView) this.findViewById(R.id.text_view_title);
        if(textViewTitle != null){
            textViewTitle.setText("Bible Picker Test");
        }
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        m_textViewVerses = (TextView) findViewById(R.id.text_view_result);
        m_textViewVerses.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getPointerCount() == 2) {
                    int action = event.getAction();
                    int pureaction = action & MotionEvent.ACTION_MASK;
                    if (pureaction == MotionEvent.ACTION_POINTER_DOWN) {
                        m_baseDist = getDistance(event);
                        m_baseRatio = m_ratio;
                    } else {
                        float delta = (getDistance(event) - m_baseDist) / STEP;
                        float multi = (float) Math.pow(2, delta);
                        m_ratio = Math.min(1024.0f, Math.max(0.1f, m_baseRatio * multi));
                        m_textViewVerses.setTextSize(m_ratio + 13);
                    }
                }
                return true;
            }
        });
    }

    @Override
    public void onVerseSelected(String verseStr) {
        m_textViewVerses.setText(verseStr);
    }

    int getDistance(MotionEvent event) {
        int dx = (int) (event.getX(0) - event.getX(1));
        int dy = (int) (event.getY(0) - event.getY(1));
        return (int) (Math.sqrt(dx * dx + dy * dy));
    }
}
