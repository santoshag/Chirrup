package com.codepath.apps.chirrup.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.chirrup.R;
import com.codepath.apps.chirrup.decorators.LinkifiedTextView;
import com.codepath.apps.chirrup.models.Tweet;

import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * Created by santoshag on 8/5/16.
 */
public class TweetsAdapter extends RecyclerView.Adapter<TweetsAdapter.ViewHolder>{

    // Store a member variable for the contacts
    private List<Tweet> mTweets;
    // Store the context for easy access
    private Context mContext;

    // Pass in the contact array into the constructor
    public TweetsAdapter(Context context, List<Tweet> contacts) {
        mTweets = contacts;
        mContext = context;
    }

    // Easy access to the context object in the recyclerview
    private Context getContext() {
        return mContext;
    }

    @Override
    public TweetsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();

        //inflate the custom view
        LayoutInflater inflater = LayoutInflater.from(context);
        View tweetView = inflater.inflate(R.layout.item_tweet, parent, false);

        //return the viewholder instance
        ViewHolder viewHolder = new ViewHolder(tweetView);


        return viewHolder;

    }

    //bind data to the viewholder
    @Override
    public void onBindViewHolder(TweetsAdapter.ViewHolder viewHolder, int position) {
        Tweet tweet = mTweets.get(position);

        TextView tvUserName = viewHolder.tvUserName;
        TextView tvScreenName = viewHolder.tvScreenName;
        LinkifiedTextView tvBody = viewHolder.tvBody;
        TextView tvRelativeTime = viewHolder.tvRelativeTime;
        ImageView ivProfileImg = viewHolder.ivProfileImg;
        ImageView ivPhoto = viewHolder.ivPhoto;
        ImageView ivRetweet = viewHolder.ivRetweet;
        ImageView ivLike = viewHolder.ivLike;


        TextView tvRetweetCount = viewHolder.tvRetweetCount;
        TextView tvLikeCount = viewHolder.tvLikeCount;

        tvUserName.setText(tweet.getUser().getName());
        tvScreenName.setText(tweet.getUser().getScreenName());
        tvBody.setText(tweet.getBody());
        tvRetweetCount.setText("");
        tvLikeCount.setText("");

        if(tweet.getRetweetCount() > 0) {
            ivRetweet.setImageResource(0);
            tvRetweetCount.setText(String.valueOf(tweet.getRetweetCount()));
            ivRetweet.setImageDrawable(getContext().getResources().getDrawable(R.drawable.retweet_on));
        }
        if(tweet.getFavouritesCount() > 0) {
            ivLike.setImageResource(0);
            tvLikeCount.setText(String.valueOf(tweet.getFavouritesCount()));
            ivLike.setImageDrawable(getContext().getResources().getDrawable(R.drawable.like_on));

        }
//        TextViewUtils.stripUnderlines(tvBody);
        tvRelativeTime.setText(tweet.getRelativeDate());
        ivProfileImg.setImageResource(android.R.color.transparent);
        ivPhoto.setImageResource(android.R.color.transparent);

        Glide.with(getContext()).load(tweet.getUser().getProfileImageUrl()).bitmapTransform(new RoundedCornersTransformation(mContext, 15, 0)).into(ivProfileImg);
        Glide.with(getContext()).load(tweet.getEntity().getMediaUrl()).bitmapTransform(new RoundedCornersTransformation(mContext, 15, 0)).into(ivPhoto);
    }

    @Override
    public int getItemCount() {
        return mTweets.size();
    }

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public TextView tvUserName, tvRelativeTime, tvScreenName, tvLikeCount, tvRetweetCount;
        public LinkifiedTextView tvBody;
        public ImageView ivProfileImg, ivPhoto, ivLike, ivRetweet;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            tvUserName = (TextView) itemView.findViewById(R.id.tvUserName);
            tvScreenName = (TextView) itemView.findViewById(R.id.tvScreenName);
            tvLikeCount = (TextView) itemView.findViewById(R.id.tvLikeCount);
            tvRetweetCount = (TextView) itemView.findViewById(R.id.tvRetweetCount);

            tvBody = (LinkifiedTextView) itemView.findViewById(R.id.tvBody);

            tvRelativeTime = (TextView) itemView.findViewById(R.id.tvRelativeTime);
            ivProfileImg = (ImageView) itemView.findViewById(R.id.ivProfileImg);
            ivPhoto = (ImageView) itemView.findViewById(R.id.ivPhoto);
            ivRetweet = (ImageView) itemView.findViewById(R.id.ivRetweet);

            ivLike = (ImageView) itemView.findViewById(R.id.ivLike);

        }
    }


}

