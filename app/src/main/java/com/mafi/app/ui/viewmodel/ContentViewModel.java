package com.mafi.app.ui.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.mafi.app.data.local.SharedPreferencesManager;
import com.mafi.app.data.model.Content;
import com.mafi.app.data.repository.ContentRepository;

public class ContentViewModel extends AndroidViewModel {

    private ContentRepository contentRepository;
    private SharedPreferencesManager preferencesManager;
    private MutableLiveData<Content> selectedContent;
    private MutableLiveData<Boolean> operationSuccess;
    private MutableLiveData<String> errorMessage;

    public ContentViewModel(@NonNull Application application) {
        super(application);
        contentRepository = new ContentRepository(application);
        preferencesManager = SharedPreferencesManager.getInstance(application);
        selectedContent = new MutableLiveData<>();
        operationSuccess = new MutableLiveData<>();
        errorMessage = new MutableLiveData<>();
    }

    public LiveData<Content> getSelectedContent() {
        return selectedContent;
    }

    public LiveData<Boolean> getOperationSuccess() {
        return operationSuccess;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void loadContent(int contentId) {
        try {
            Content content = contentRepository.getContentById(contentId);
            selectedContent.setValue(content);
        } catch (Exception e) {
            errorMessage.setValue("İçerik yüklenirken hata oluştu: " + e.getMessage());
        }
    }

    public void createContent(String title, String description, int contentTypeId, String contentUrl) {
        try {
            int userId = preferencesManager.getUserId();
            if (userId == -1) {
                errorMessage.setValue("Kullanıcı girişi yapılmamış!");
                return;
            }

            Content newContent = new Content();
            newContent.setTitle(title);
            newContent.setDescription(description);
            newContent.setContentTypeId(contentTypeId);
            newContent.setContentUrl(contentUrl);
            newContent.setUserId(userId);
            newContent.setCreatedAt(System.currentTimeMillis());

            long contentId = contentRepository.insertContent(newContent);

            if (contentId > 0) {
                operationSuccess.setValue(true);
            } else {
                errorMessage.setValue("İçerik oluşturulamadı.");
            }
        } catch (Exception e) {
            errorMessage.setValue("İçerik oluşturulurken hata oluştu: " + e.getMessage());
        }
    }

    public void updateContent(Content content) {
        try {
            int result = contentRepository.updateContent(content);
            operationSuccess.setValue(result > 0);

            if (result <= 0) {
                errorMessage.setValue("İçerik güncellenemedi.");
            }
        } catch (Exception e) {
            errorMessage.setValue("İçerik güncellenirken hata oluştu: " + e.getMessage());
        }
    }

    public void deleteContent(int contentId) {
        try {
            int result = contentRepository.deleteContent(contentId);
            operationSuccess.setValue(result > 0);

            if (result <= 0) {
                errorMessage.setValue("İçerik silinemedi.");
            }
        } catch (Exception e) {
            errorMessage.setValue("İçerik silinirken hata oluştu: " + e.getMessage());
        }
    }
}