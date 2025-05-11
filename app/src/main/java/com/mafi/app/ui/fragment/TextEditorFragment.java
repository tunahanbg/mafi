package com.mafi.app.ui.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.mafi.app.R;
import com.mafi.app.ui.viewmodel.TextEditorViewModel;

public class TextEditorFragment extends Fragment {
    private static final String TAG = "TextEditorFragment";
    private static final String ARG_CONTENT_ID = "content_id";

    private TextEditorViewModel viewModel;
    private TextView textViewTitle;
    private EditText editTextContent;
    private Button buttonSummarize;
    private Button buttonExpand;
    private Button buttonPlaceholder1;
    private Button buttonPlaceholder2;

    private int contentId;

    public static TextEditorFragment newInstance(int contentId) {
        TextEditorFragment fragment = new TextEditorFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_CONTENT_ID, contentId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            contentId = getArguments().getInt(ARG_CONTENT_ID, -1);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_text_editor, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "onViewCreated başladı. İçerik ID: " + contentId);

        // View elemanlarını bağla
        textViewTitle = view.findViewById(R.id.text_view_title);
        editTextContent = view.findViewById(R.id.edit_text_content);
        buttonSummarize = view.findViewById(R.id.button_summarize);
        buttonExpand = view.findViewById(R.id.button_expand);
        buttonPlaceholder1 = view.findViewById(R.id.button_placeholder1);
        buttonPlaceholder2 = view.findViewById(R.id.button_placeholder2);

        // ViewModel'i bağla
        viewModel = new ViewModelProvider(this).get(TextEditorViewModel.class);

        // Gözlemcileri ayarla
        viewModel.getSelectedContent().observe(getViewLifecycleOwner(), content -> {
            if (content != null) {
                textViewTitle.setText(content.getTitle());
            }
        });

        viewModel.getEditedText().observe(getViewLifecycleOwner(), text -> {
            if (text != null && !text.equals(editTextContent.getText().toString())) {
                editTextContent.setText(text);
                // İmleci en sona getir
                editTextContent.setSelection(editTextContent.getText().length());
            }
        });

        viewModel.getSaveSuccess().observe(getViewLifecycleOwner(), success -> {
            if (success != null && success) {
                Toast.makeText(getContext(), "İçerik başarıyla kaydedildi", Toast.LENGTH_SHORT).show();
                viewModel.resetSaveSuccess();
            }
        });

        viewModel.getErrorMessage().observe(getViewLifecycleOwner(), errorMessage -> {
            if (errorMessage != null && !errorMessage.isEmpty()) {
                Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
            }
        });

        // İçeriği yükle
        if (contentId > 0) {
            viewModel.loadContent(contentId);
        } else {
            Toast.makeText(getContext(), "Geçersiz içerik ID", Toast.LENGTH_SHORT).show();
            requireActivity().getSupportFragmentManager().popBackStack();
        }

        // Buton işlevlerini ayarla
        buttonSummarize.setOnClickListener(v -> {
            viewModel.setEditedText(editTextContent.getText().toString()); // Önce editördeki metni güncelle
            viewModel.summarizeText();
        });

        buttonExpand.setOnClickListener(v -> {
            viewModel.setEditedText(editTextContent.getText().toString()); // Önce editördeki metni güncelle
            viewModel.expandText();
        });

        buttonPlaceholder1.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Buton 3 - İşlev eklenmedi", Toast.LENGTH_SHORT).show();
        });

        buttonPlaceholder2.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Buton 4 - İşlev eklenmedi", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        // Fragment kapatılırken içeriği kaydet
        saveContent();
    }

    private void saveContent() {
        if (editTextContent.getText() != null) {
            viewModel.setEditedText(editTextContent.getText().toString());
            viewModel.saveContent();
        }
    }
}