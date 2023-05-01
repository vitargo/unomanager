package com.vitargo.unomanager;

import android.os.Bundle;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import com.vitargo.unomanager.databinding.ActivityScrollingBinding;

public class ScrollingActivity extends AppCompatActivity {

    private ActivityScrollingBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityScrollingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);
        CollapsingToolbarLayout toolBarLayout = binding.toolbarLayout;
        toolBarLayout.setTitle(getTitle());

        FloatingActionButton fabInfo = binding.fabInfo;
        fabInfo.setOnClickListener(view -> Snackbar.make(view, "Rules source: https://desktopgames.com.ua/ua/uno.html?tab=rules", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show());

        FloatingActionButton fabRevert = binding.fabRevert;
        fabRevert.setOnClickListener(view -> this.finish());
    }
}