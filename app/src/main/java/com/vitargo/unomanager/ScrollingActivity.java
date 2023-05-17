package com.vitargo.unomanager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.vitargo.unomanager.databinding.ActivityScrollingBinding;
import com.vitargo.unomanager.databinding.ContentScrollingBinding;

public class ScrollingActivity extends AppCompatActivity {

    private ActivityScrollingBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        int text = intent.getIntExtra("game", 0);
        int title = intent.getIntExtra("title", 0);

        this.setTitle(title);

        binding = ActivityScrollingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ContentScrollingBinding d = binding.ruleText;
        TextView textView = d.getRoot().findViewById(R.id.game_rule);
        textView.setText(text);

        Toolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);
        CollapsingToolbarLayout toolBarLayout = binding.toolbarLayout;
        toolBarLayout.setTitle(getTitle());

//        FloatingActionButton fabInfo = binding.fabInfo;
//        fabInfo.setOnClickListener(view -> Snackbar.make(view, "Rules source: https://desktopgames.com.ua/ua/uno.html?tab=rules", Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show());

        FloatingActionButton fabRevert = binding.fabRevert;
        fabRevert.setOnClickListener(view -> this.finish());
    }


}