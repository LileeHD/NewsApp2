package com.example.android.newsapp2;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class ArticleAdapter extends ArrayAdapter<Article> {
    private static final String LOCATION_SEPARATOR = "T";

    public ArticleAdapter(@NonNull Context context, int resource, List<Article> articles) {
        super(context, 0, articles);
    }

    @TargetApi(Build.VERSION_CODES.O)
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }

        Article currentArticle = getItem(position);

        TextView sectionView = (TextView) convertView.findViewById(R.id.section_view);
        if (currentArticle != null) {
            sectionView.setText(currentArticle.getSection());
        }

        TextView titleView = (TextView) convertView.findViewById(R.id.title_view);
        if (currentArticle != null) {
            titleView.setText(currentArticle.getTitle());
        }

        TextView authorView = convertView.findViewById(R.id.author_view);
        if (currentArticle != null) {
            authorView.setText(currentArticle.getAuthor());
        } else {
            authorView.setText(R.string.unknown);
        }

//        Date
        TextView dateView = (TextView) convertView.findViewById(R.id.date_view);
        if (currentArticle != null) {
            dateView.setText(currentArticle.getDate());
        } else {
            authorView.setText(R.string.unknown);
        }

        return convertView;
    }
}
