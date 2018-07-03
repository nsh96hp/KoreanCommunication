package com.hdpsolution.koreancommunication.data;

/**
 * Copyright (C) 2015 Ari C.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.os.Parcel;
import android.os.Parcelable;

import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;

public class Korean implements Parcelable,SearchSuggestion {

    private int _id;
    private int category_id;

    private String textVN;
    private String textEN;
    private String textCH;

    private String textKR;
    private String pinyin;
    private String voice;
    private int favorite;
    private int status;


    private Korean(Parcel in) {
        _id = in.readInt();
        textVN = in.readString();
        textEN = in.readString();
        textCH = in.readString();
        textKR = in.readString();
        pinyin = in.readString();
        voice = in.readString();
        category_id = in.readInt();
        favorite = in.readInt();
        status = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(_id);
        dest.writeString(textCH);
        dest.writeString(textVN);
        dest.writeString(textEN);
        dest.writeString(textKR);
        dest.writeString(pinyin);
        dest.writeString(voice);
        dest.writeInt(category_id);
        dest.writeInt(favorite);
        dest.writeInt(status);

    }



    @Override
    public int describeContents() {
        return 0;
    }

    public Korean(int _id, int category_id, String textVN, String textEN, String textCH, String textKR, String pinyin, String voice, int favorite, int status) {
        this._id = _id;
        this.category_id = category_id;
        this.textVN = textVN;
        this.textEN = textEN;
        this.textCH = textCH;
        this.textKR = textKR;
        this.pinyin = pinyin;
        this.voice = voice;
        this.favorite = favorite;
        this.status = status;
    }

    public Korean(int _id, int favorite) {
        this._id = _id;
        this.favorite = favorite;
    }

    public Korean() {
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public String getTextVN() {
        return textVN;
    }

    public void setTextVN(String textVN) {
        this.textVN = textVN;
    }

    public String getTextEN() {
        return textEN;
    }

    public void setTextEN(String textEN) {
        this.textEN = textEN;
    }

    public String getTextCH() {
        return textCH;
    }

    public void setTextCH(String textCH) {
        this.textCH = textCH;
    }

    public String getTextKR() {
        return textKR;
    }

    public void setTextKR(String textKR) {
        this.textKR = textKR;
    }

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    public String getVoice() {
        return voice;
    }

    public void setVoice(String voice) {
        this.voice = voice;
    }

    public int getFavorite() {
        return favorite;
    }

    public void setFavorite(int favorite) {
        this.favorite = favorite;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public static final Creator<Korean> CREATOR = new Creator<Korean>() {
        @Override
        public Korean createFromParcel(Parcel in) {
            return new Korean(in);
        }

        @Override
        public Korean[] newArray(int size) {
            return new Korean[size];
        }
    };

    @Override
    public String toString() {
        return "Korean{" +
                "_id=" + _id +
                ", category_id=" + category_id +
                ", textVN='" + textVN + '\'' +
                ", textEN='" + textEN + '\'' +
                ", textCH='" + textCH + '\'' +
                ", textKR='" + textKR + '\'' +
                ", pinyin='" + pinyin + '\'' +
                ", voice='" + voice + '\'' +
                ", favorite=" + favorite +
                ", status=" + status +
                '}';
    }

    @Override
    public String getBody() {
        return textEN;
    }
}