package com.example.desiregallery.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import com.example.desiregallery.helpers.ModelGenerator;
import com.example.desiregallery.models.Post;

import java.util.List;

public class PostListViewModel extends ViewModel {
    private MutableLiveData<List<Post>> posts;

    public LiveData<List<Post>> getPosts() {
        if (posts == null) {
            posts = new MutableLiveData<List<Post>>();
            loadPosts();
        }
        return posts;
    }

    private void loadPosts() {
        List<Post> data = ModelGenerator.Companion.getPosts();
        posts.setValue(data);
    }

}
