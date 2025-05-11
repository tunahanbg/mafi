package com.mafi.app.ui.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.mafi.app.data.model.Content;
import com.mafi.app.data.repository.ContentRepository;

import java.util.List;

public class HomeViewModel extends AndroidViewModel {

    private ContentRepository contentRepository;
    private MutableLiveData<List<Content>> contentList;
    private MutableLiveData<Boolean> isLoading;
    private MutableLiveData<String> errorMessage;

    public HomeViewModel(@NonNull Application application) {
        super(application);
        contentRepository = new ContentRepository(application);
        contentList = new MutableLiveData<>();
        isLoading = new MutableLiveData<>(false);
        errorMessage = new MutableLiveData<>();
    }

    public LiveData<List<Content>> getContentList() {
        return contentList;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void loadAllContents() {
        isLoading.setValue(true);

        try {
            // Arka plan thread'inde çalıştırılmalı (gerçek uygulamada AsyncTask veya Executor kullanılır)
            List<Content> contents = contentRepository.getAllContents();
            contentList.setValue(contents);
            isLoading.setValue(false);
        } catch (Exception e) {
            isLoading.setValue(false);
            errorMessage.setValue("İçerikler yüklenirken hata oluştu: " + e.getMessage());
        }
    }

    public void loadContentsByType(int contentTypeId) {
        isLoading.setValue(true);

        try {
            List<Content> contents = contentRepository.getContentsByType(contentTypeId);
            contentList.setValue(contents);
            isLoading.setValue(false);
        } catch (Exception e) {
            isLoading.setValue(false);
            errorMessage.setValue("İçerikler filtrelenirken hata oluştu: " + e.getMessage());
        }
    }

    public void refreshContent() {
        loadAllContents();
    }
}