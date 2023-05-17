package com.vitargo.unomanager;

import android.content.Intent;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class Games extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_games);

        Button buttonUno = findViewById(R.id.button_uno);
        Button buttonBridge = findViewById(R.id.button_bridge);
        Button buttonBack = findViewById(R.id.button_back);

        buttonUno.setOnClickListener(view -> showRule(R.string.uno_rule, R.string.title_uno));
        buttonBridge.setOnClickListener(view -> showRule(R.string.bridge_rule, R.string.title_bridge));
        buttonBack.setOnClickListener(view -> this.finish());
    }

    private void showRule(int game, int title) {
        Intent intent = new Intent(getApplicationContext(), ScrollingActivity.class);
        intent.putExtra("game", game);
        intent.putExtra("title", title);
        startActivity(intent);
    }
}