package com.mafi.app.ui.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.mafi.app.data.model.Content;
import com.mafi.app.data.repository.ContentRepository;

public class TextEditorViewModel extends AndroidViewModel {
    private static final String TAG = "TextEditorViewModel";

    private ContentRepository contentRepository;
    private MutableLiveData<Content> selectedContent;
    private MutableLiveData<String> editedText;
    private MutableLiveData<Boolean> saveSuccess;
    private MutableLiveData<String> errorMessage;

    public TextEditorViewModel(@NonNull Application application) {
        super(application);
        contentRepository = new ContentRepository(application);
        selectedContent = new MutableLiveData<>();
        editedText = new MutableLiveData<>("");
        saveSuccess = new MutableLiveData<>(false);
        errorMessage = new MutableLiveData<>();
    }

    public LiveData<Content> getSelectedContent() {
        return selectedContent;
    }

    public LiveData<String> getEditedText() {
        return editedText;
    }

    public LiveData<Boolean> getSaveSuccess() {
        return saveSuccess;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void loadContent(int contentId) {
        try {
            Log.d(TAG, "İçerik yükleniyor. ID: " + contentId);

            Content content = contentRepository.getContentById(contentId);
            if (content != null) {
                selectedContent.setValue(content);

                // Eğer içeriğin halihazırda bir açıklaması varsa, editöre koy
                if (content.getDescription() != null && !content.getDescription().isEmpty()) {
                    editedText.setValue(content.getDescription());
                }

                Log.d(TAG, "İçerik başarıyla yüklendi: " + content.getTitle());
            } else {
                Log.e(TAG, "İçerik bulunamadı. ID: " + contentId);
                errorMessage.setValue("İçerik bulunamadı.");
            }
        } catch (Exception e) {
            Log.e(TAG, "İçerik yükleme hatası: " + e.getMessage(), e);
            errorMessage.setValue("İçerik yüklenirken hata oluştu: " + e.getMessage());
        }
    }

    public void setEditedText(String text) {
        editedText.setValue(text);
    }

    public void saveContent() {
        try {
            Content content = selectedContent.getValue();
            String text = editedText.getValue();

            if (content == null) {
                Log.e(TAG, "İçerik null, kaydedilemez");
                errorMessage.setValue("İçerik bulunamadı");
                return;
            }

            if (text == null || text.isEmpty()) {
                Log.e(TAG, "Metin boş, kaydedilemez");
                errorMessage.setValue("Lütfen bir metin girin");
                return;
            }

            Log.d(TAG, "İçerik kaydediliyor. ID: " + content.getId());

            // İçerik açıklamasını güncelle
            content.setDescription(text);

            // Veritabanını güncelle
            int result = contentRepository.updateContent(content);

            if (result > 0) {
                Log.d(TAG, "İçerik başarıyla kaydedildi");
                saveSuccess.setValue(true);
            } else {
                Log.e(TAG, "İçerik kaydedilemedi");
                errorMessage.setValue("İçerik kaydedilemedi");
            }
        } catch (Exception e) {
            Log.e(TAG, "İçerik kaydetme hatası: " + e.getMessage(), e);
            errorMessage.setValue("İçerik kaydedilirken hata oluştu: " + e.getMessage());
        }
    }

    // Özet işlevi - Basit bir örnek
    public void summarizeText() {
        String currentText = editedText.getValue();
        if (currentText == null || currentText.isEmpty()) {
            errorMessage.setValue("Özetlenecek metin yok");
            return;
        }

        // Basit bir özet algoritması - ilk 2 cümleyi al
        String[] sentences = currentText.split("(?<=[.!?])\\s+");
        if (sentences.length <= 2) {
            // Metin zaten çok kısa
            errorMessage.setValue("Metin zaten kısa, özetlenemez");
            return;
        }

        StringBuilder summary = new StringBuilder();
        for (int i = 0; i < Math.min(2, sentences.length); i++) {
            summary.append(sentences[i]).append(" ");
        }

        // Özeti editöre koy
        editedText.setValue(summary.toString().trim());
    }

    // Genişletme işlevi - Basit bir örnek
    public void expandText() {
        String currentText = editedText.getValue();
        if (currentText == null || currentText.isEmpty()) {
            errorMessage.setValue("Genişletilecek metin yok");
            return;
        }

        // Basit bir genişletme - her cümlenin sonuna detay ekle
        String[] sentences = currentText.split("(?<=[.!?])\\s+");
        StringBuilder expanded = new StringBuilder();

        for (String sentence : sentences) {
            expanded.append(sentence).append(" Bu konuda daha fazla detay verilebilir. ");
        }

        // Genişletilmiş metni editöre koy
        editedText.setValue(expanded.toString().trim());
    }

    public void resetSaveSuccess() {
        saveSuccess.setValue(false);
    }
}