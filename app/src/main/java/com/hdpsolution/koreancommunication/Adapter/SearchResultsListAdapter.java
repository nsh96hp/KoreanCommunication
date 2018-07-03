package com.hdpsolution.koreancommunication.Adapter;

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

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.graphics.Color;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.arlib.floatingsearchview.util.Util;

import com.hdpsolution.koreancommunication.Activity.MainActivity;
import com.hdpsolution.koreancommunication.R;
import com.hdpsolution.koreancommunication.data.DataFav;
import com.hdpsolution.koreancommunication.data.DatabaseHelper;
import com.hdpsolution.koreancommunication.data.Korean;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class SearchResultsListAdapter extends RecyclerView.Adapter<SearchResultsListAdapter.ViewHolder> {

    private List<Korean> mDataSet = new ArrayList<>();
    private List<Korean> mFav = new ArrayList<>();

    private int mLastAnimatedItemPosition = -1;
    private int flagTouch = -1;
    private int language;
    private DatabaseHelper db;
    private DataFav dbf;
    private Context mContext;
    private MediaPlayer m;
    private int mPlay = 0;
    private int mPlaySound = 1;


    public SearchResultsListAdapter(int languagex, Context mContext, ArrayList<Korean> fav) {
        this.language = languagex;
        this.mContext = mContext;
        this.mFav = fav;

    }

    public void changeLanguage(int newLanguage) {
        language = newLanguage;
        notifyDataSetChanged();
    }

    public void changeSound() {
        mPlaySound = mPlaySound * -1;
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onClick(Korean korean, int position);
    }

    private OnItemClickListener mItemsOnClickListener;

    private OnSpeakerClickListener mOnSpeakerClickListener;

    public interface OnSpeakerClickListener {
        void onClick(Korean korean, int pos);
    }

    public void setItemsOnClickSpeakerListener(OnSpeakerClickListener onClickListener) {
        this.mOnSpeakerClickListener = onClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView mTxtName;
        public final TextView mTxt_Kr;
        public final TextView mTxt_Pinyin;
        public final View mTextContainer;
        public LinearLayout ll_search;
        public ImageButton fav_item_s, unfav_item_s, sound_item_search, sound_item_speaker;


        public ViewHolder(View view) {
            super(view);
            m = new MediaPlayer();

            mTxtName = view.findViewById(R.id.txt_name);
            mTxt_Kr = view.findViewById(R.id.txt_kr);
            mTxt_Pinyin = view.findViewById(R.id.txt_pinyin);

            mTextContainer = view.findViewById(R.id.text_container);
            ll_search = view.findViewById(R.id.ll_search);

            fav_item_s = view.findViewById(R.id.fav_item_s);
            unfav_item_s = view.findViewById(R.id.unfav_item_s);
            sound_item_search = view.findViewById(R.id.sound_item_search);
            sound_item_speaker = view.findViewById(R.id.sound_item_speaker);

            dbf = new DataFav(mContext);
            fav_item_s.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int _id = mDataSet.get(getAdapterPosition()).get_id();
                    dbf.Handle_Fav(_id);
                    Korean k = mDataSet.get(getAdapterPosition());
                    k.setFavorite(1);
                    mFav.set(k.get_id() - 1, k);
                    Log.e("Fav", _id + "");
                    notifyDataSetChanged();
                }
            });
            unfav_item_s.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int _id = mDataSet.get(getAdapterPosition()).get_id();
                    dbf.Handle_UnFav(_id);
                    Korean k = mDataSet.get(getAdapterPosition());
                    k.setFavorite(0);
                    mFav.set(k.get_id() - 1, k);
                    Log.e("UnFav", _id + "");
                    notifyDataSetChanged();
                }
            });

            sound_item_search.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mPlaySound == 1) {
                        playSound(mDataSet.get(getAdapterPosition()).getVoice());
                    }
                }
            });


        }
    }


    public void Touch(int pos) {
        if (flagTouch == pos) {
            flagTouch = -1;
        } else {
            flagTouch = pos;
        }

        notifyDataSetChanged();
    }

    public void swapData(List<Korean> mNewDataSet) {
        mDataSet = mNewDataSet;
        flagTouch = -1;
        notifyDataSetChanged();
    }

    public void setItemsOnClickListener(OnItemClickListener onClickListener) {
        this.mItemsOnClickListener = onClickListener;
    }

    @Override
    public SearchResultsListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.search_results_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SearchResultsListAdapter.ViewHolder holder, final int position) {

        Korean Kr = mDataSet.get(position);
        Korean KrF = mFav.get(Kr.get_id() - 1);
        switch (language) {
            case 0:
                holder.mTxtName.setText(Kr.getTextVN());
                break;
            case 1:
                holder.mTxtName.setText(Kr.getTextCH());
                break;
            case 2:
                holder.mTxtName.setText(Kr.getTextEN());
                break;
        }
        holder.mTxt_Kr.setText(Kr.getTextKR());
        holder.mTxt_Pinyin.setText(Kr.getPinyin());


        if (mLastAnimatedItemPosition < position) {
            animateItem(holder.itemView);
            mLastAnimatedItemPosition = position;
        }

        holder.ll_search.setVisibility(View.GONE);
        if (flagTouch == position) {
            holder.ll_search.setVisibility(View.VISIBLE);
        }
        if (KrF.getFavorite() == 1) {
            holder.fav_item_s.setVisibility(View.GONE);
            holder.unfav_item_s.setVisibility(View.VISIBLE);
        } else {
            holder.unfav_item_s.setVisibility(View.GONE);
            holder.fav_item_s.setVisibility(View.VISIBLE);
        }

        if (mItemsOnClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("Clicked Item in view", position + "");
                    mItemsOnClickListener.onClick(mDataSet.get(position), position);
                }
            });
        }

        if (mOnSpeakerClickListener != null) {
            holder.sound_item_speaker.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Korean Kr = mDataSet.get(position);
                    mOnSpeakerClickListener.onClick(Kr, position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    private void animateItem(View view) {
        view.setTranslationY(Util.getScreenHeight((Activity) view.getContext()));
        view.animate()
                .translationY(0)
                .setInterpolator(new DecelerateInterpolator(3.f))
                .setDuration(700)
                .start();
    }

    public void playSound(String fileName) {
        AssetFileDescriptor descriptor = null;
        if (m.isPlaying()) {
            m.pause();
            m.stop();
            m.release();
            m = new MediaPlayer();
        }

        try {
            m.release();
            m = new MediaPlayer();
            descriptor = mContext.getAssets().openFd(fileName + "_f.ogg");
            m.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
            descriptor.close();
            m.prepare();
            m.setVolume(1f, 1f);
            m.start();
            mPlay = 1;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
