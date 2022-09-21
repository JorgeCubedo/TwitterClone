package com.cubo.twitterclone;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TweetAdapter extends RecyclerView.Adapter<TweetAdapter.MyTweetHolder> {
    private Context context;
    private List<Tweet> tweetList;

    public TweetAdapter(Context context, List<Tweet> tweets) {
        this.context = context;
        this.tweetList = tweets;
    }

    @NonNull
    @Override
    public MyTweetHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_twitt, parent, false);
        return new MyTweetHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyTweetHolder holder, int position) {
        Tweet tweet = tweetList.get(position);
        holder.txtDisplayName.setText(tweet.getDisplayName());
        holder.txtUsername.setText("@" + tweet.getUsername());
        holder.txtTweet.setText(tweet.getTweet());
        holder.txtTimeP.setText(tweet.getPublishedTime());
    }

    @Override
    public int getItemCount() {
        return tweetList.size();
    }

    public static class MyTweetHolder extends RecyclerView.ViewHolder {
        public TextView txtDisplayName;
        public TextView txtUsername;
        public TextView txtTweet;
        public TextView txtTimeP;

        public MyTweetHolder(View v) {
            super(v);
            txtDisplayName = v.findViewById(R.id.text_display_name);
            txtUsername = v.findViewById(R.id.text_username);
            txtTweet = v.findViewById(R.id.text_twitt);
            txtTimeP = v.findViewById(R.id.text_time);
        }
    }

}
