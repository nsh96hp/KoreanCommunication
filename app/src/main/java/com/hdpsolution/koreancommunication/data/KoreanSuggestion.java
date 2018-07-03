package com.hdpsolution.koreancommunication.data;

import android.os.Parcel;

import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;

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

public class KoreanSuggestion implements SearchSuggestion {

    private String mTextName;
    private String mTextPinyin;
    private String mTextKr;
    private boolean mIsHistory = false;

    public KoreanSuggestion(int language, Korean kSuggestion) {
        switch (language) {
            case 1:
                this.mTextName = kSuggestion.getTextEN().toLowerCase();
                break;
            case 2:
                this.mTextName = kSuggestion.getTextCH().toLowerCase();
                break;
            case 3:
                this.mTextName = kSuggestion.getTextVN().toLowerCase();
                break;
        }
        this.mTextPinyin = kSuggestion.getPinyin().toLowerCase();
        this.mTextKr = kSuggestion.getTextKR().toLowerCase();

    }

    public KoreanSuggestion(Parcel source) {
        this.mTextName = source.readString();
        this.mIsHistory = source.readInt() != 0;
    }

    public void setIsHistory(boolean isHistory) {
        this.mIsHistory = isHistory;
    }

    public boolean getIsHistory() {
        return this.mIsHistory;
    }

    @Override
    public String getBody() {
        return mTextName;
    }

    public static final Creator<KoreanSuggestion> CREATOR = new Creator<KoreanSuggestion>() {
        @Override
        public KoreanSuggestion createFromParcel(Parcel in) {
            return new KoreanSuggestion(in);
        }

        @Override
        public KoreanSuggestion[] newArray(int size) {
            return new KoreanSuggestion[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mTextName);
        dest.writeInt(mIsHistory ? 1 : 0);
    }
}