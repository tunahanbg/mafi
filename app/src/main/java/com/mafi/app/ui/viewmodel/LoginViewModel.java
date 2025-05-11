package com.mafi.app.ui.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.mafi.app.data.local.SharedPreferencesManager;

public class LoginViewModel extends AndroidViewModel {

    private SharedPreferencesManager preferencesManager;
    private MutableLiveData<Boolean> loginSuccess;
    private MutableLiveData<String> errorMessage;

    public LoginViewModel(@NonNull Application application) {
        super(application);
        preferencesManager = SharedPreferencesManager.getInstance(application);
        loginSuccess = new MutableLiveData<>();
        errorMessage = new MutableLiveData<>();
    }

    public LiveData<Boolean> getLoginSuccess() {
        return loginSuccess;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void login(String email, String password) {
        // Gerçek uygulamada burada API isteği veya veritabanı sorgusu yapılır
        // Örnek olarak basit bir doğrulama yapıyoruz
        if (email == null || email.isEmpty()) {
            errorMessage.setValue("E-posta adresi gerekli");
            return;
        }

        if (password == null || password.isEmpty()) {
            errorMessage.setValue("Şifre gerekli");
            return;
        }

        // Test için basit bir kontrol
        if (email.equals("test@example.com") && password.equals("password")) {
            // Başarılı giriş - örnek kullanıcı verilerini kaydet
            preferencesManager.saveUserSession(
                    1,
                    "Test Kullanıcı",
                    email,
                    "https://example.com/profile.jpg"
            );
            loginSuccess.setValue(true);
        } else {
            errorMessage.setValue("Geçersiz e-posta veya şifre");
        }
    }

    public boolean isLoggedIn() {
        return preferencesManager.isLoggedIn();
    }
}