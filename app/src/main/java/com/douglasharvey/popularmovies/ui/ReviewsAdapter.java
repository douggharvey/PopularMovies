package com.douglasharvey.popularmovies.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.douglasharvey.popularmovies.R;
import com.douglasharvey.popularmovies.data.Review;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ViewHolder> {

    private List<Review> reviewList = new ArrayList<>();
    @SuppressWarnings("unused")
    private static final String LOG_TAG = ReviewsAdapter.class.getSimpleName();
    private static final int DEFAULT_MAX_LINES = 5;
    private static final int MAXIMUM_LINES = 200;

    void setReviewsData(List<Review> list) {
        if (list != null) {
            reviewList = list;
            notifyDataSetChanged();
        }
    }

    ReviewsAdapter(@NonNull Context context) {
        @SuppressWarnings({"UnnecessaryLocalVariable", "unused"}) Context context1 = context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_reviews, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.review = reviewList.get(position);
        holder.tvReviewAuthor.setText(holder.review.getAuthor());
        holder.tvReviewContent.setText(holder.review.getContent());

        holder.tvReviewContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // expand & collapse on individual items
                if (holder.tvReviewContent.getMaxLines()==DEFAULT_MAX_LINES) {
                    holder.tvReviewContent.setMaxLines(MAXIMUM_LINES);
                }
                else {holder.tvReviewContent.setMaxLines(DEFAULT_MAX_LINES);
                }
                notifyDataSetChanged(); // tried notifyItemChanged(position) but only worked on the second click??
            }
        });

    }


    @Override
    public int getItemCount() {
        return reviewList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        Review review;

        @BindView(R.id.tv_review_content)
        TextView tvReviewContent;
        @BindView(R.id.tv_review_author)
        TextView tvReviewAuthor;

        ViewHolder(View view) {
            super(view);
            tvReviewAuthor = view.findViewById(R.id.tv_review_author);
            tvReviewContent = view.findViewById(R.id.tv_review_content);
            tvReviewContent.setMaxLines(DEFAULT_MAX_LINES);
        }
    }


}
