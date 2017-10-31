package com.csecu.amrit.jargon.copy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.csecu.amrit.jargon.R;
import com.csecu.amrit.jargon.model.ModelWord;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by Amrit on 14-09-2017.
 */

public class CustomCopyAdapter extends ArrayAdapter<ModelWord> {

    public class ViewHolder {
        TextView tvWord;
        TextView tvMeaning;
    }

    public CustomCopyAdapter(Context context, ArrayList<ModelWord> objects) {
        super(context, R.layout.copy_list, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row;
        ViewHolder viewHolder;

        if (convertView == null) {
            row = LayoutInflater.from(getContext()).inflate(R.layout.copy_list, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.tvWord = (TextView) row.findViewById(R.id.tvCopyWord);
            viewHolder.tvMeaning = (TextView) row.findViewById(R.id.tvCopyMeaning);

            row.setTag(viewHolder);
        } else {
            row = convertView;
            viewHolder = (ViewHolder) row.getTag();
        }

        ModelWord modelWord = getItem(position);

        String word = modelWord.getWord();
        String meaning = modelWord.getMeaning();

        word = decodeString(word);
        meaning = decodeString(meaning);

        viewHolder.tvWord.setText(word.substring(0, 1).toUpperCase() + word.substring(1));
        viewHolder.tvMeaning.setText(String.valueOf(meaning.substring(0, 1).toUpperCase()
                + meaning.substring(1)));

        return row;
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
