package com.example.musicplayer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.MusicViewHolder> {

    private List<ItemsModel> items;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onPlayClick(int position);
    }

    public MusicAdapter(List<ItemsModel> items, OnItemClickListener listener) {
        this.items = items;
        this.listener = listener;
    }

    public void updateList(List<ItemsModel> newList) {
        items = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MusicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_music, parent, false);
        return new MusicViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MusicViewHolder holder, int position) {
        ItemsModel item = items.get(position);
        holder.tvSurahNameEnglish.setText(item.getSurahNameEnglish());
        holder.tvSurahNameArabic.setText(item.getSurahNameArabic());
        holder.tvSurahNumber.setText("Surah No: " + item.getSurahNumber());
        holder.tvNumberOfVerses.setText("Verses: " + item.getNumberOfVerses());
        holder.tvPlaceOfRevelation.setText(item.getPlaceOfRevelation());

        holder.btnPlay.setOnClickListener(v -> {
            if (listener != null) {
                listener.onPlayClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class MusicViewHolder extends RecyclerView.ViewHolder {
        TextView tvSurahNameEnglish, tvSurahNameArabic, tvSurahNumber, tvNumberOfVerses, tvPlaceOfRevelation;
        ImageButton btnPlay;

        public MusicViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSurahNameEnglish = itemView.findViewById(R.id.tvSurahNameEnglish);
            tvSurahNameArabic = itemView.findViewById(R.id.tvSurahNameArabic);
            tvSurahNumber = itemView.findViewById(R.id.tvSurahNumber);
            tvNumberOfVerses = itemView.findViewById(R.id.tvNumberOfVerses);
            tvPlaceOfRevelation = itemView.findViewById(R.id.tvPlaceOfRevelation);
            btnPlay = itemView.findViewById(R.id.btnPlay);
        }
    }
}
