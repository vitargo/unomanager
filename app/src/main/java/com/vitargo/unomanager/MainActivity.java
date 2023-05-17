package com.vitargo.unomanager;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import java.util.*;

import static java.lang.String.valueOf;

public class MainActivity extends AppCompatActivity implements AddPlayerDialog.AddPlayerDialogListener {
    private int counter = 0;
    Map<Integer, Integer> resultTable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        resultTable = new HashMap<>();
        setContentView(R.layout.activity_main);

        TableLayout scoreTable = findViewById(R.id.score_table);
        for (int i = 0; i < scoreTable.getChildCount(); i++) {
            if(scoreTable.getChildAt(i) instanceof TableRow row){
                if (row.getChildCount() > 1) {
                    row.getChildAt(1).setOnKeyListener(getOnKeyListener((EditText) row.getChildAt(1), (TextView) row.getChildAt(2)));
                }
            }
        }

        Button button = findViewById(R.id.button_add_player);
        Button clear = findViewById(R.id.button_clear);
        Button rule = findViewById(R.id.button_rule);
        Button close = findViewById(R.id.button_close);

        button.setOnClickListener(view -> addPlayer());

        clear.setOnClickListener(view -> clear());

        rule.setOnClickListener(view -> showCatalog());

        close.setOnClickListener(view -> {
            this.finish();
            System.exit(0);
        });

        if (savedInstanceState != null) {
            int size = savedInstanceState.getInt("size");
            int all = scoreTable.getChildCount();
            int count = 0;
            for (int i = 0; i < (size * 3); i++) {
                if(scoreTable.getChildAt(i) instanceof TableRow row){
                    if (row.getChildCount() > 1) {
                        count++;
                        showPlayerRow((TextView) row.getChildAt(2), (TableRow) scoreTable.getChildAt(i - 1), row, scoreTable.getChildAt(i + 1), (TextView) row.getChildAt(0),
                                savedInstanceState.getString("player" + count), savedInstanceState.getString("result" + count));
                    }

                }
            }
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        TableLayout scoreTable = findViewById(R.id.score_table);
        checkRowVisibilityAndSaveScore(scoreTable, outState);
        super.onSaveInstanceState(outState);
    }

    private void checkRowVisibilityAndSaveScore(TableLayout layout, Bundle outState) {
        int size = layout.getChildCount();
        if (size > 0) {
            int count = 0;
            for (int i = 0; i < size; i++) {
                View child = layout.getChildAt(i);
                if (child instanceof TableRow row && ((TableRow) child).getChildCount() > 1) {
                    if (row.getVisibility() == View.VISIBLE) {
                        count++;
                        TextView player = (TextView) row.getChildAt(0);
                        TextView result = (TextView) row.getChildAt(2);
                        outState.putString("player" + count, player.getText().toString());
                        outState.putString("result" + count, result.getText().toString());
                    }
                }
            }
            outState.putInt("size", count);
        }

    }


    private View.OnKeyListener getOnKeyListener(EditText scorePlayer, TextView resultPlayer) {
        return new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (scorePlayer.isFocused()) {
                    if (i == KeyEvent.KEYCODE_ENTER) {
                        String enteredValue = scorePlayer.getText().toString();
                        if (!enteredValue.isEmpty()) {
                            int current = Integer.parseInt(enteredValue);
                            int total = Integer.parseInt(resultPlayer.getText().toString());
                            count(current, total, scorePlayer, resultPlayer);
                        }
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                }
                return false;
            }
        };
    }

    private void count(int current, int total, EditText score, TextView result) {
        if (counter == 1) {
            String message = "Мінімальна кількість учасників - не менше 2-х гравців!";
            AlertDialog alertDialog = getAlertDialog(message);
            alertDialog.show();
            score.clearFocus();
            score.getText().clear();
        } else {
            int green = Color.rgb(107, 142, 35);
            int red = Color.rgb(178, 34, 34);
            int sum = current + total;
            score.getText().clear();
            score.clearFocus();
            if (sum >= 200) {
                String message = "Учасник набрав максимальну кількість балів!";
                String title = "Гра завершена!";
                AlertDialog alertDialog = getAlertDialog(message, title);
                alertDialog.show();
            }
            result.setText(valueOf(sum));

            resultTable.put(result.getId(), sum);

            HashMap<Integer, Integer> sortedMap = customSort();
            int[] keys = new int[0];
            keys = sortedMap.keySet().stream().mapToInt(Number::intValue).toArray();
            for (Integer textView : keys) {
                TextView allResult = findViewById(textView);
                allResult.setTextColor(Color.BLACK);
            }

            if (!Objects.equals(resultTable.get(keys[keys.length - 1]), resultTable.get(keys[0]))) {
                TextView playerResult = findViewById(keys[0]);
                playerResult.setTextColor(green);
                for (int i = 1; i < keys.length; i++) {
                    if (resultTable.get(keys[i]) == resultTable.get(keys[0])) {
                        TextView maxResult = findViewById(keys[i]);
                        maxResult.setTextColor(green);
                    }
                }
                TextView minResult = findViewById(keys[keys.length - 1]);
                minResult.setTextColor(red);
                for (int i = keys.length - 2; i < 0; i++) {
                    TextView maxResult = findViewById(keys[i]);
                    maxResult.setTextColor(red);
                }
            } else {
                for (Integer textView : keys) {
                    TextView allResult = findViewById(textView);
                    allResult.setTextColor(red);
                }
            }
        }
    }

    private HashMap<Integer, Integer> customSort() {
        List<Map.Entry<Integer, Integer>> list = new LinkedList<>(resultTable.entrySet());
        list.sort(Comparator.comparing(Map.Entry::getValue));
        HashMap<Integer, Integer> sortedMap = new LinkedHashMap<>();
        for (Map.Entry<Integer, Integer> map : list) {
            sortedMap.put(map.getKey(), map.getValue());
        }
        return sortedMap;
    }

    private void clear() {
        finish();
        startActivity(getIntent());
    }

    private void showCatalog() {
        Intent intent = new Intent(this, Games.class);
        startActivity(intent);
    }

    private void addPlayer() {
        if (counter > 9) {
            String message = "Перевищенно максимальну кількість учасників!";
            AlertDialog alertDialog = getAlertDialog(message);
            alertDialog.show();
        } else {
            AddPlayerDialog d = new AddPlayerDialog();
            d.show(getSupportFragmentManager(), "AddPlayerDialog");
        }
    }

    private AlertDialog getAlertDialog(String message, String title) {
        AlertDialog dialog = getAlertDialog(message);
        dialog.setTitle(title);
        return dialog;
    }

    private AlertDialog getAlertDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage(message);
        builder.setTitle("Помилка");
        builder.setCancelable(false);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        return builder.create();
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog, String name) {
        if (name == null || name.isEmpty()) {
            String message = "Введіть ім'я гравця!";
            AlertDialog alertDialog = getAlertDialog(message);
            alertDialog.show();
        } else {
            counter++;

            switch (counter) {
                case 1 ->
                        showPlayerRow(findViewById(R.id.result_1), findViewById(R.id.row_1_1), findViewById(R.id.row_1_2),
                                findViewById(R.id.row_1_3), findViewById(R.id.player_1), name, "0");
                case 2 ->
                        showPlayerRow(findViewById(R.id.result_2), findViewById(R.id.row_2_1), findViewById(R.id.row_2_2),
                                findViewById(R.id.row_2_3), findViewById(R.id.player_2), name, "0");
                case 3 ->
                        showPlayerRow(findViewById(R.id.result_3), findViewById(R.id.row_3_1), findViewById(R.id.row_3_2),
                                findViewById(R.id.row_3_3), findViewById(R.id.player_3), name, "0");
                case 4 ->
                        showPlayerRow(findViewById(R.id.result_4), findViewById(R.id.row_4_1), findViewById(R.id.row_4_2),
                                findViewById(R.id.row_4_3), findViewById(R.id.player_4), name, "0");
                case 5 ->
                        showPlayerRow(findViewById(R.id.result_5), findViewById(R.id.row_5_1), findViewById(R.id.row_5_2),
                                findViewById(R.id.row_5_3), findViewById(R.id.player_5), name, "0");
                case 6 ->
                        showPlayerRow(findViewById(R.id.result_6), findViewById(R.id.row_6_1), findViewById(R.id.row_6_2),
                                findViewById(R.id.row_6_3), findViewById(R.id.player_6), name, "0");
                case 7 ->
                        showPlayerRow(findViewById(R.id.result_7), findViewById(R.id.row_7_1), findViewById(R.id.row_7_2),
                                findViewById(R.id.row_7_3), findViewById(R.id.player_7), name, "0");
                case 8 ->
                        showPlayerRow(findViewById(R.id.result_8), findViewById(R.id.row_8_1), findViewById(R.id.row_8_2),
                                findViewById(R.id.row_8_3), findViewById(R.id.player_8), name, "0");
                case 9 ->
                        showPlayerRow(findViewById(R.id.result_9), findViewById(R.id.row_9_1), findViewById(R.id.row_9_2),
                                findViewById(R.id.row_9_3), findViewById(R.id.player_9), name, "0");
                case 10 ->
                        showPlayerRow(findViewById(R.id.result_10), findViewById(R.id.row_10_1), findViewById(R.id.row_10_2),
                                findViewById(R.id.row_10_3), findViewById(R.id.player_10), name, "0");
            }
            Objects.requireNonNull(dialog.getDialog()).cancel();
        }
    }

    private void showPlayerRow(TextView resultPlayer, TableRow groupNameRow, TableRow playerRow, View line, TextView message, String name, String result) {
        resultPlayer.setText(result);
        resultTable.put(resultPlayer.getId(), Integer.valueOf(result));
        groupNameRow.setVisibility(View.VISIBLE);
        playerRow.setVisibility(View.VISIBLE);
        line.setVisibility(View.VISIBLE);
        message.setText(name);
    }
}
