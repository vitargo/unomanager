package com.vitargo.unomanager;

import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.vitargo.db.Winner;
import com.vitargo.db.WinnerRateDBHelper;

import java.util.List;

public class Statistic extends AppCompatActivity {

    private WinnerRateDBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stat);
        dbHelper = new WinnerRateDBHelper(this);

        updateTable();

        Button buttonBack = findViewById(R.id.button_back);
        Button buttonClear = findViewById(R.id.button_clear_stat);

        buttonBack.setOnClickListener(view -> this.finish());
        buttonClear.setOnClickListener(view -> this.clearStat());
    }

    @Override
    protected void onDestroy() {
        dbHelper.close();
        super.onDestroy();
    }

    private void clearStat(){
        dbHelper.deleteAll();
        MainActivity.newRowId = 0;
        this.recreate();
    }

    private void updateTable(){
        List<Winner> winners = dbHelper.getAllWinners();
        TableLayout table = findViewById(R.id.winner_table);
        for (Winner winner : winners){
            TableRow row = new TableRow(this);
            TableRow.LayoutParams rowParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
            row.setLayoutParams(rowParams);
            TextView name = new TextView(this);
            name.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            name.setText(winner.getName());
            row.addView(name);
            TextView score = new TextView(this);
            score.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            score.setText(winner.getScore());
            row.addView(score);
            TextView date = new TextView(this);
            date.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            date.setText(winner.getDate());
            row.addView(date);
            table.addView(row);
        }
    }
}