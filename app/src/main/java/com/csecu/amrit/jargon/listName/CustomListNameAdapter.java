package com.csecu.amrit.jargon.listName;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.csecu.amrit.jargon.R;

import java.util.ArrayList;

/**
 * Created by Amrit on 15-09-2017.
 */

public class CustomListNameAdapter extends RecyclerView.Adapter<CustomListNameAdapter.MyViewHolder> {

    private ArrayList<String> stringList;

    /**
     * View holder class
     */
    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView tvName;

        public MyViewHolder(View view) {
            super(view);
            tvName = (TextView) view.findViewById(R.id.tvViewListName);
        }
    }

    public CustomListNameAdapter(ArrayList<String> stringList) {
        this.stringList = stringList;
    }

    @Override
    public void onBindViewHolder(CustomListNameAdapter.MyViewHolder holder, int position) {
        System.out.println("Bind [" + holder + "] - Pos [" + position + "]");
        String string = stringList.get(position);
        holder.tvName.setText(string);
    }

    @Override
    public int getItemCount() {
        Log.d("RV", "Item size [" + stringList.size() + "]");
        return stringList.size();
    }

    @Override
    public CustomListNameAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row, parent, false);
        return new CustomListNameAdapter.MyViewHolder(v);
    }

    public String getItem(int position) {
        return stringList.get(position);
    }
}