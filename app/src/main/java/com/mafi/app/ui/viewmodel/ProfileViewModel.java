package com.mafi.app.ui.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.mafi.app.data.local.SharedPreferencesManager;
import com.mafi.app.data.model.Content;
import com.mafi.app.data.repository.ContentRepository;

import java.util.List;

public class ProfileViewModel extends AndroidViewModel {

    private ContentRepository contentRepository;
    private SharedPreferencesManager preferencesManager;
    private MutableLiveData<List<Content>> userContentList;
    private MutableLiveData<String> username;
    private MutableLiveData<String> email;
    private MutableLiveData<String> profilePicture;
    private MutableLiveData<String> errorMessage;

    public ProfileViewModel(@NonNull Application application) {
        super(application);
        contentRepository = new ContentRepository(application);
        preferencesManager = SharedPreferencesManager.getInstance(application);
        userContentList = new MutableLiveData<>();
        username = new MutableLiveData<>();
        email = new MutableLiveData<>();
        profilePicture = new MutableLiveData<>();
        errorMessage = new MutableLiveData<>();

        // Kullanıcı bilgilerini yükle
        loadUserInfo();
    }

    public LiveData<List<Content>> getUserContentList() {
        return userContentList;
    }

    public LiveData<String> getUsername() {
        return username;
    }

    public LiveData<String> getEmail() {
        return email;
    }

    public LiveData<String> getProfilePicture() {
        return profilePicture;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void loadUserInfo() {
        username.setValue(preferencesManager.getUsername());
        email.setValue(preferencesManager.getEmail());
        profilePicture.setValue(preferencesManager.getProfilePicture());
    }

    public void loadUserContents() {
        try {
            int userId = preferencesManager.getUserId();
            if (userId != -1) {
                List<Content> contents = contentRepository.getContentsByUserId(userId);
                userContentList.setValue(contents);
            } else {
                errorMessage.setValue("Kullanıcı girişi yapılmamış!");
            }
        } catch (Exception e) {
            errorMessage.setValue("Kullanıcı içerikleri yüklenirken hata oluştu: " + e.getMessage());
        }
    }

    public void logout() {
        preferencesManager.clearUserSession();
    }
}