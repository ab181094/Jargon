package com.csecu.amrit.jargon.list;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.csecu.amrit.jargon.R;
import com.csecu.amrit.jargon.model.ModelWord;

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
        holder.tvWord.setText(modelWord.getWord().substring(0, 1).toUpperCase() +
                modelWord.getWord().substring(1));
        holder.tvMeaning.setText(String.valueOf(modelWord.getMeaning().substring(0, 1).
                toUpperCase() + modelWord.getMeaning().substring(1)));
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
}