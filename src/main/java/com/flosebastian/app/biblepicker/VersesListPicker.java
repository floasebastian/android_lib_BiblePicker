package com.flosebastian.app.biblepicker;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link VersesListPicker.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link VersesListPicker#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VersesListPicker extends Fragment implements AdapterView.OnItemSelectedListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    Spinner m_spinnerBible      = null;
    Spinner m_spinnerBook       = null;
    Spinner m_spinnerChapter    = null;

    Button m_buttonShow         = null;
    Button m_buttonCopy         = null;
    Button m_buttonSelect       = null;
    Button m_buttonShared       = null;
    Button m_buttonPrev         = null;
    Button m_buttonNext         = null;


    Set<Integer> m_setSelected = new HashSet<Integer>();

    LinearLayout m_linearLayoutVerses = null;
    LinearLayout m_linearLayoutAction = null;

    TextView m_titleChapter = null;

    private BibleManager m_bibleManager;

    private OnFragmentInteractionListener mListener;

    public VersesListPicker() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment VersesListPicker.
     */
    // TODO: Rename and change types and number of parameters
    public static VersesListPicker newInstance(String param1, String param2) {
        VersesListPicker fragment = new VersesListPicker();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View thisFragmentView = inflater.inflate(R.layout.fragment_verses_list_picker, container, false);

        m_bibleManager = BibleManager.getInstance();
        m_bibleManager.init(this.getContext());

        this.initializeSpinners(thisFragmentView);
        this.initializeButtons(thisFragmentView);

        m_linearLayoutVerses = (LinearLayout) thisFragmentView.findViewById(R.id.linear_layout_verses_container);
        m_linearLayoutAction = (LinearLayout) thisFragmentView.findViewById(R.id.linear_layout_action_buttons_group);

        m_titleChapter = (TextView) thisFragmentView.findViewById(R.id.text_view_chapter);

        return thisFragmentView;
    }

    private void initializeSpinners(View theView){
        m_spinnerBible      = (Spinner)theView.findViewById(R.id.spinner_bible);
        m_spinnerBook       = (Spinner)theView.findViewById(R.id.spinner_book);
        m_spinnerChapter    = (Spinner)theView.findViewById(R.id.spinner_chapter);

        ArrayList<String> bibleList = (ArrayList<String>) m_bibleManager.getInstance().getBiblesList();

        if(bibleList != null){
            Utilities.fillSpinner(this, m_spinnerBible, bibleList);
            m_spinnerBible.setOnItemSelectedListener(this);
        }
    }

    private void initializeButtons(View theView){
        m_buttonShow        = theView.findViewById(R.id.button_show);
        m_buttonCopy        = theView.findViewById(R.id.button_copy);
        m_buttonSelect      = theView.findViewById(R.id.button_select);
        m_buttonPrev      = theView.findViewById(R.id.button_next);
        m_buttonNext      = theView.findViewById(R.id.button_prev);
        m_buttonShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), getSelectedVerses(),Toast.LENGTH_LONG).show();
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

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            //mListener.onFragmentInteraction(uri);
        }
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
        int bible    = (int)m_spinnerBook.getSelectedItemId();
        int book    = (int)m_spinnerBook.getSelectedItemId();
        int chapter = (int)m_spinnerChapter.getSelectedItemId();

        String strBible = m_spinnerBible.getSelectedItem().toString();
        String strBook = m_spinnerBook.getSelectedItem().toString();
        String strChapter = m_spinnerChapter.getSelectedItem().toString();

        m_titleChapter.setText(strBook + " " + strChapter);

        ArrayList<String> versesInChapter = (ArrayList<String>) m_bibleManager.getVersesInChapter(book, chapter);
        m_linearLayoutVerses.removeAllViews();

        if(versesInChapter !=null){
            for(int i = 0; i < versesInChapter.size(); i++){
                TextView tv = new TextView(this.getContext());
                tv.setText(versesInChapter.get(i));
                tv.setBackgroundColor(Color.rgb(255, 255, 255));
                tv.setOnLongClickListener(new View.OnLongClickListener() {

                    private boolean isSelected = false;

                    @Override
                    public boolean onLongClick(View v) {
                        TextView tv = (TextView) v;
                        int selectedIdx = ((LinearLayout)v.getParent()).indexOfChild(v);
                        String sel = "";
                        if(tv != null){
                            isSelected = !isSelected;
                            if(isSelected){
                                m_setSelected.add(selectedIdx);
                                sel = "Select: ";
                                tv.setBackgroundColor(Color.rgb(255, 255, 0));
                            }else {
                                m_setSelected.remove(selectedIdx);
                                sel = "Unselect: ";
                                tv.setBackgroundColor(Color.rgb(255, 255, 255));
                            }
                        }
                        sel += selectedIdx;
                        Toast.makeText(getContext(), sel, Toast.LENGTH_LONG).show();

                        if(m_setSelected.size() > 0){
                            m_linearLayoutAction.setVisibility(View.VISIBLE);
                        }else{
                            m_linearLayoutAction.setVisibility(View.GONE);
                        }
                        return false;
                    }
                });
                ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                tv.setLayoutParams(lp);
                m_linearLayoutVerses.addView(tv);
            }
        }
    }


    public String getSelectedVerses(){
        int bible    = (int)m_spinnerBook.getSelectedItemId();
        int book    = (int)m_spinnerBook.getSelectedItemId();
        int chapter = (int)m_spinnerChapter.getSelectedItemId();
        Integer[] indices =  m_setSelected.toArray(new Integer[m_setSelected.size()]);
        String result = m_bibleManager.getVerses(book, chapter, indices, true, Utilities.NumberFormat.SUPERSCRIPT, true, false);
        return result;
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
        void onSelectVersesButtonClicked(String versesStr);
    }
}
