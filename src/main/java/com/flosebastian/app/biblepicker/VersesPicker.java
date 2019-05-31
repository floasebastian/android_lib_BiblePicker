package com.flosebastian.app.biblepicker;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link VersesPicker.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link VersesPicker#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VersesPicker extends Fragment implements AdapterView.OnItemSelectedListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    BibleManager m_bibleManager;

    Spinner m_spinnerBible          = null;
    Spinner m_spinnerBook           = null;
    Spinner m_spinnerChapter        = null;
    Spinner m_spinnerVerseStart     = null;
    Spinner m_spinnerVerseEnd       = null;

    Button m_buttonShow             = null;
    Button m_buttonCopy             = null;
    Button m_buttonSelect           = null;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public VersesPicker() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * param param1 Parameter 1.
     * param param2 Parameter 2.
     * @return A new instance of fragment VersesPicker.
     */
    // TODO: Rename and change types and number of parameters
    public static VersesPicker newInstance(/*String param1, String param2*/) {
        VersesPicker fragment = new VersesPicker();
        Bundle args = new Bundle();
        /*args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);*/
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            /*mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);*/
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View thisFragmentView = inflater.inflate(R.layout.fragment_verses_picker, container, false);
        m_bibleManager = BibleManager.getInstance();
        m_bibleManager.init(this.getActivity());
        this.initializeSpinners(thisFragmentView);
        this.initializeButtons(thisFragmentView);

        return thisFragmentView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            /*mListener.onFragmentInteraction(uri);*/
        }
    }

    private void initializeButtons(View theView){
        m_buttonShow        = (Button)theView.findViewById(R.id.button_show);
        m_buttonCopy        = (Button)theView.findViewById(R.id.button_copy);
        m_buttonSelect      = (Button)theView.findViewById(R.id.button_select);

        m_buttonShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strVerses = getSelectedVerses();
                Toast.makeText(getActivity(), strVerses, Toast.LENGTH_LONG).show();
            }
        });

        m_buttonCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strVerses = getSelectedVerses();
                ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Verse", strVerses);
                clipboard.setPrimaryClip(clip);
                Toast.makeText(getActivity(), "Verses copied to the clipboard!", Toast.LENGTH_LONG).show();
            }
        });

        m_buttonSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strVerses = getSelectedVerses();
                mListener.onSelectVersesButtonClicked(strVerses);
            }
        });

    }

    private String getSelectedVerses(){
        int bible    = (int)m_spinnerBook.getSelectedItemId();
        int book    = (int)m_spinnerBook.getSelectedItemId();
        int chapter = (int)m_spinnerChapter.getSelectedItemId();
        int start   = (int)m_spinnerVerseStart.getSelectedItemId();
        int end     = (int)Integer.parseInt(m_spinnerVerseEnd.getSelectedItem().toString()) - 1;
        String strBible = m_spinnerBible.getSelectedItem().toString();
        String strBook = m_spinnerBook.getSelectedItem().toString();
        String strVersesNum = "" + ((start == end) ? start + 1 : (start + 1) + "-" + (end + 1) );
        String strVerses = strBook  + " " + (chapter+1) + ":" + strVersesNum + ", " + strBible + "\n";
        strVerses += m_bibleManager.getVerses(book, chapter, start, end, true, Utilities.NumberFormat.SUPERSCRIPT, true, true);
        return strVerses;
    }

    private void initializeSpinners(View theView){
        m_spinnerBible      = (Spinner)theView.findViewById(R.id.spinner_bible);
        m_spinnerBook       = (Spinner)theView.findViewById(R.id.spinner_book);
        m_spinnerChapter    = (Spinner)theView.findViewById(R.id.spinner_chapter);
        m_spinnerVerseStart = (Spinner)theView.findViewById(R.id.spinner_verse_start);
        m_spinnerVerseEnd   = (Spinner)theView.findViewById(R.id.spinner_verse_end);

        ArrayList<String> bibleList = (ArrayList<String>) m_bibleManager.getInstance().getBiblesList();

        if(bibleList != null){
            Utilities.fillSpinner(this, m_spinnerBible, bibleList);
            m_spinnerBible.setOnItemSelectedListener(this);
        }


    }

    private void onBibleSelected(int bibleIdx){
        m_bibleManager.loadBible(bibleIdx);
        ArrayList<String> booksList = (ArrayList<String>) m_bibleManager.getInstance().getBooksList();
        if(booksList != null){
            Utilities.fillSpinner(this, m_spinnerBook, booksList);
            m_spinnerBook.setOnItemSelectedListener(this);
        }
    }

    private void onBookSelected(int bookIdx){
        int chaptersCount = this.m_bibleManager.getChaptersCount(bookIdx);
        ArrayList<String> chapters = Utilities.generateNumbersStringArrayList(1, chaptersCount);
        Utilities.fillSpinner(this, m_spinnerChapter, chapters);
        m_spinnerChapter.setOnItemSelectedListener(this);
    }

    private void onChapterSelected(int chapterIdx){
        int bookIdx = (int)m_spinnerBook.getSelectedItemId();
        int versesCount = this.m_bibleManager.getVersesCount(bookIdx, chapterIdx);
        ArrayList<String> versesS = Utilities.generateNumbersStringArrayList(1, versesCount);
        ArrayList<String> versesE = Utilities.generateNumbersStringArrayList(1, versesCount);
        Utilities.fillSpinner(this, m_spinnerVerseStart, versesS);
        Utilities.fillSpinner(this, m_spinnerVerseEnd, versesE);
        m_spinnerVerseStart.setOnItemSelectedListener(this);
        m_spinnerVerseEnd.setOnItemSelectedListener(this);
    }

    private void onVerseStartSelected(int verseIdx){
        int start = verseIdx + 1;
        int end = m_spinnerVerseStart.getAdapter().getCount();
        ArrayList<String> verses = Utilities.generateNumbersStringArrayList(start, end);
        Utilities.fillSpinner(this, m_spinnerVerseEnd, verses);
    }

    private void onVerseEndSelected(int verseIdx){

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(parent.getId() == R.id.spinner_bible){
            this.onBibleSelected((int)id);
        }
        else if(parent.getId() == R.id.spinner_book){
            this.onBookSelected((int)id);
        }
        else if(parent.getId() == R.id.spinner_chapter){
            this.onChapterSelected((int)id);
        }
        else if(parent.getId() == R.id.spinner_verse_start){
            this.onVerseStartSelected((int)id);
        }
        else if(parent.getId() == R.id.spinner_verse_end){
            this.onVerseEndSelected((int)id);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        /*void onFragmentInteraction(Uri uri);*/
        void onSelectVersesButtonClicked(String versesStr);
    }


}
