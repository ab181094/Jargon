package com.csecu.amrit.jargon.list;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.csecu.amrit.jargon.R;
import com.csecu.amrit.jargon.model.ModelWord;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by Amrit on 12-09-2017.
 */

public class CustomListAdapter extends RecyclerView.Adapter<CustomListAdapter.MyViewHolder> {

    private ArrayList<ModelWord> wordList;

    /**
     * View holder class
     */
    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView tvWord;
        public TextView tvMeaning;

        public MyViewHolder(View view) {
            super(view);
            tvWord = (TextView) view.findViewById(R.id.tvListWord);
            tvMeaning = (TextView) view.findViewById(R.id.tvListMeaning);
        }
    }

    public CustomListAdapter(ArrayList<ModelWord> wordList) {
        this.wordList = wordList;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        System.out.println("Bind [" + holder + "] - Pos [" + position + "]");
        ModelWord modelWord = wordList.get(position);

        String word = modelWord.getWord();
        String meaning = modelWord.getMeaning();

        word = decodeString(word);
        meaning = decodeString(meaning);

        holder.tvWord.setText(word.substring(0, 1).toUpperCase() + word.substring(1));
        holder.tvMeaning.setText(String.valueOf(meaning.substring(0, 1).toUpperCase()
                + meaning.substring(1)));
    }

    @Override
    public int getItemCount() {
        Log.d("RV", "Item size [" + wordList.size() + "]");
        return wordList.size();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.word_list, parent, false);
        return new MyViewHolder(v);
    }

    public ModelWord getItem(int position) {
        return wordList.get(position);
    }

    private String encodeString(String word) {
        try {
            return URLEncoder.encode(word, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return word;
        }
    }

    private String decodeString(String realAnswer) {
        try {
            realAnswer = URLDecoder.decode(realAnswer, "UTF-8");
            return realAnswer;
        } catch (UnsupportedEncodingException e) {
            return realAnswer;
        }
    }
}