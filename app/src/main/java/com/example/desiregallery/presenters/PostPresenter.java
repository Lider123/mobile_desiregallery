package com.example.desiregallery.presenters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import com.example.desiregallery.Utils;
import com.example.desiregallery.models.Post;
import com.example.desiregallery.ui.activities.CommentsActivity;
import com.example.desiregallery.ui.activities.FullScreenImageActivity;
import com.example.desiregallery.ui.views.PostView;

public class PostPresenter {
    private PostView view;
    private Post post;

    public PostPresenter(PostView view) {
        this.view = view;
        post = new Post();
    }

    public PostPresenter(PostView view, Post post) {
        this.view = view;
        this.post = post;
    }

    public void setRating() {
        view.updateRating(post.getRating());
    }

    public void updateRating(float rate) {
        post.updateRating(rate);
        view.updateRating(post.getRating());
    }

    public void setImageView() {
        view.updateImage(post.getImageUrl().toString());
    }

    public void goToCommentActivity(Context context) {
        Intent intent = new Intent(context, CommentsActivity.class);
        intent.putExtra("post", post);
        context.startActivity(intent);
    }

    public void goToImageFullScreen(Context context, Bitmap bmpImage) {
        Intent intent = new Intent(context, FullScreenImageActivity.class);
        intent.putExtra("bytesImage", Utils.Companion.bitmapToBytes(bmpImage));
        context.startActivity(intent);
    }
}
