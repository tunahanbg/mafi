package com.mafi.app.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mafi.app.R;
import com.mafi.app.data.model.Content;
import com.mafi.app.ui.adapter.ContentAdapter;
import com.mafi.app.ui.viewmodel.HomeViewModel;

import java.util.ArrayList;

public class HomeFragment extends Fragment implements ContentAdapter.OnItemClickListener {

    private HomeViewModel viewModel;
    private RecyclerView recyclerViewContent;
    private ContentAdapter contentAdapter;
    private ProgressBar progressBar;
    private FloatingActionButton fabAddContent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // View elemanlarını bağla
        recyclerViewContent = view.findViewById(R.id.recycler_view_content);
        progressBar = view.findViewById(R.id.progress_bar);
        fabAddContent = view.findViewById(R.id.fab_add_content);

        // RecyclerView'ı ayarla
        recyclerViewContent.setLayoutManager(new LinearLayoutManager(getContext()));
        contentAdapter = new ContentAdapter(getContext(), new ArrayList<>());
        contentAdapter.setOnItemClickListener(this);
        recyclerViewContent.setAdapter(contentAdapter);

        // ViewModel'i bağla
        viewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        // Gözlemcileri ayarla
        viewModel.getContentList().observe(getViewLifecycleOwner(), contents -> {
            contentAdapter.updateContentList(contents);
        });

        viewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        });

        viewModel.getErrorMessage().observe(getViewLifecycleOwner(), errorMessage -> {
            if (errorMessage != null && !errorMessage.isEmpty()) {
                Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
            }
        });

        // İçerik ekleme butonu işlemi
        fabAddContent.setOnClickListener(v -> {
            // ContentManagerFragment'a geçiş
            if (getActivity() != null) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new ContentManagerFragment())
                        .addToBackStack(null)
                        .commit();
            }
        });

        // İçerikleri yükle
        viewModel.loadAllContents();
    }

    @Override
    public void onItemClick(Content content) {
        // İçerik detayını dialog olarak göster
        // Örnek:
        Toast.makeText(getContext(), "İçerik: " + content.getTitle(), Toast.LENGTH_SHORT).show();

        // Gerçek uygulamada burada bir dialog gösterebiliriz
        // DialogFragment contentDetailDialog = ContentDetailDialogFragment.newInstance(content.getId());
        // contentDetailDialog.show(getChildFragmentManager(), "content_detail");
    }

    @Override
    public void onItemLongClick(Content content, View view) {
        // Uzun tıklama ile içerik için işlemler (silme, düzenleme vb.)
        // Örnek:
        Toast.makeText(getContext(), "İçerik için işlemler: " + content.getTitle(), Toast.LENGTH_SHORT).show();
    }
}