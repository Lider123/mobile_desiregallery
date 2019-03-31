package com.example.desiregallery.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;
import com.example.desiregallery.MainApplication;
import com.example.desiregallery.models.Post;
import com.example.desiregallery.network.DGNetwork;
import com.example.desiregallery.network.IDGApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.List;

public class PostListViewModel extends ViewModel {
    private static final String TAG = PostListViewModel.class.getSimpleName();

    private MutableLiveData<List<Post>> posts;

    public LiveData<List<Post>> getPosts() {
        if (posts == null) {
            posts = new MutableLiveData<>();
            loadPosts();
        }
        return posts;
    }

    private void loadPosts() {
        IDGApi api = DGNetwork.getService();
        api.getPosts().enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    posts.setValue(response.body());
                    Log.i(TAG, "Posts have been loaded");
                }
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                Log.e(TAG, "Unable to load posts");
                t.printStackTrace();
            }
        });
    }

}
