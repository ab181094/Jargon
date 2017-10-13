package com.csecu.amrit.jargon.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Amrit on 11-09-2017.
 */

public class ModelWord implements Parcelable{
    String word;
    String meaning;
    String type;
    String list;
    String id;

    public ModelWord() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getMeaning() {
        return meaning;
    }

    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getList() {
        return list;
    }

    public void setList(String list) {
        this.list = list;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(word);
        parcel.writeString(meaning);
        parcel.writeString(type);
        parcel.writeString(list);
    }

    public ModelWord(Parcel parcel) {
        this.id = parcel.readString();
        this.word = parcel.readString();
        this.meaning = parcel.readString();
        this.type = parcel.readString();
        this.list = parcel.readString();
    }

    public static final Creator<ModelWord> CREATOR = new Creator<ModelWord>() {
        @Override
        public ModelWord createFromParcel(Parcel parcel) {
            return new ModelWord(parcel);
        }

        @Override
        public ModelWord[] newArray(int i) {
            return new ModelWord[i];
        }
    };
}
