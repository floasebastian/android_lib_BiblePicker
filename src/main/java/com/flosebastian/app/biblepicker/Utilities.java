package com.flosebastian.app.biblepicker;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;

public final class Utilities {
    public static void fillSpinner(Fragment f, Spinner spinner, ArrayList<String> arrayList){
        if(f.getActivity() != null) {
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(f.getActivity(), android.R.layout.simple_spinner_item, arrayList);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
        }
    }

    public static ArrayList<String> generateNumbersStringArrayList(int start, int end){
        ArrayList<String> result = new ArrayList<String>();
        for(int i = start; i <= end; i++){
            result.add(String.valueOf(i));
        }
        return result;
    }

    public static String superscriptNum(String str) {
        str = str.replaceAll("0", "⁰");
        str = str.replaceAll("1", "¹");
        str = str.replaceAll("2", "²");
        str = str.replaceAll("3", "³");
        str = str.replaceAll("4", "⁴");
        str = str.replaceAll("5", "⁵");
        str = str.replaceAll("6", "⁶");
        str = str.replaceAll("7", "⁷");
        str = str.replaceAll("8", "⁸");
        str = str.replaceAll("9", "⁹");
        return str;
    }

    public static String subscriptNum(String str) {
        str = str.replaceAll("0", "₀");
        str = str.replaceAll("1", "₁");
        str = str.replaceAll("2", "₂");
        str = str.replaceAll("3", "₃");
        str = str.replaceAll("4", "₄");
        str = str.replaceAll("5", "₅");
        str = str.replaceAll("6", "₆");
        str = str.replaceAll("7", "₇");
        str = str.replaceAll("8", "₈");
        str = str.replaceAll("9", "₉");
        return str;
    }
}
