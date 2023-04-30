package com.vitargo.unomanager;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableRow;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import java.util.*;

import static java.lang.String.*;

public class MainActivity extends AppCompatActivity implements AddPlayerDialog.AddPlayerDialogListener {
    private int counter = 0;
    Map<Integer, Integer> resultTable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        resultTable = new HashMap<>();
        setContentView(R.layout.activity_main);

        EditText scorePlayer1 = findViewById(R.id.score_1);
        EditText scorePlayer2 = findViewById(R.id.score_2);
        EditText scorePlayer3 = findViewById(R.id.score_3);
        EditText scorePlayer4 = findViewById(R.id.score_4);
        EditText scorePlayer5 = findViewById(R.id.score_5);
        EditText scorePlayer6 = findViewById(R.id.score_6);
        EditText scorePlayer7 = findViewById(R.id.score_7);
        EditText scorePlayer8 = findViewById(R.id.score_8);
        EditText scorePlayer9 = findViewById(R.id.score_9);
        EditText scorePlayer10 = findViewById(R.id.score_10);

        Button button = findViewById(R.id.button_add_player);
        Button clear = findViewById(R.id.button_clear);

        scorePlayer1.setOnKeyListener(getOnKeyListener(scorePlayer1, findViewById(R.id.result_1)));
        scorePlayer2.setOnKeyListener(getOnKeyListener(scorePlayer2, findViewById(R.id.result_2)));
        scorePlayer3.setOnKeyListener(getOnKeyListener(scorePlayer3, findViewById(R.id.result_3)));
        scorePlayer4.setOnKeyListener(getOnKeyListener(scorePlayer4, findViewById(R.id.result_4)));
        scorePlayer5.setOnKeyListener(getOnKeyListener(scorePlayer5, findViewById(R.id.result_5)));
        scorePlayer6.setOnKeyListener(getOnKeyListener(scorePlayer6, findViewById(R.id.result_6)));
        scorePlayer7.setOnKeyListener(getOnKeyListener(scorePlayer7, findViewById(R.id.result_7)));
        scorePlayer8.setOnKeyListener(getOnKeyListener(scorePlayer8, findViewById(R.id.result_8)));
        scorePlayer9.setOnKeyListener(getOnKeyListener(scorePlayer9, findViewById(R.id.result_9)));
        scorePlayer10.setOnKeyListener(getOnKeyListener(scorePlayer10, findViewById(R.id.result_10)));

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                addPlayer();
            }
        });

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clear();
            }
        });
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
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                keys = sortedMap.keySet().stream().mapToInt(Number::intValue).toArray();
            }
            for (Integer textView : keys) {
                TextView allResult = findViewById(textView);
                allResult.setTextColor(Color.BLACK);
            }

            if (resultTable.get(keys[keys.length - 1]) != resultTable.get(keys[0])) {
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Collections.sort(list, Comparator.comparing(o -> o.getValue()));
        }
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
                case 1:
                    showPlayerRow(findViewById(R.id.result_1), findViewById(R.id.row_1_1), findViewById(R.id.row_1_2),
                            findViewById(R.id.row_1_3), findViewById(R.id.player_1), name);
                    break;
                case 2:
                    showPlayerRow(findViewById(R.id.result_2), findViewById(R.id.row_2_1), findViewById(R.id.row_2_2),
                            findViewById(R.id.row_2_3), findViewById(R.id.player_2), name);
                    break;
                case 3:
                    showPlayerRow(findViewById(R.id.result_3), findViewById(R.id.row_3_1), findViewById(R.id.row_3_2),
                            findViewById(R.id.row_3_3), findViewById(R.id.player_3), name);
                    break;
                case 4:
                    showPlayerRow(findViewById(R.id.result_4), findViewById(R.id.row_4_1), findViewById(R.id.row_4_2),
                            findViewById(R.id.row_4_3), findViewById(R.id.player_4), name);
                    break;
                case 5:
                    showPlayerRow(findViewById(R.id.result_5), findViewById(R.id.row_5_1), findViewById(R.id.row_5_2),
                            findViewById(R.id.row_5_3), findViewById(R.id.player_5), name);
                    break;
                case 6:
                    showPlayerRow(findViewById(R.id.result_6), findViewById(R.id.row_6_1), findViewById(R.id.row_6_2),
                            findViewById(R.id.row_6_3), findViewById(R.id.player_6), name);
                    break;
                case 7:
                    showPlayerRow(findViewById(R.id.result_7), findViewById(R.id.row_7_1), findViewById(R.id.row_7_2),
                            findViewById(R.id.row_7_3), findViewById(R.id.player_7), name);
                    break;
                case 8:
                    showPlayerRow(findViewById(R.id.result_8), findViewById(R.id.row_8_1), findViewById(R.id.row_8_2),
                            findViewById(R.id.row_8_3), findViewById(R.id.player_8), name);
                    break;
                case 9:
                    showPlayerRow(findViewById(R.id.result_9), findViewById(R.id.row_9_1), findViewById(R.id.row_9_2),
                            findViewById(R.id.row_9_3), findViewById(R.id.player_9), name);
                    break;
                case 10:
                    showPlayerRow(findViewById(R.id.result_10), findViewById(R.id.row_10_1), findViewById(R.id.row_10_2),
                            findViewById(R.id.row_10_3), findViewById(R.id.player_10), name);
                    break;
            }
            dialog.getDialog().cancel();
        }
    }

    private void showPlayerRow(TextView resultPlayer, TableRow groupNameRow, TableRow playerRow, View line, TextView message, String name) {
        resultPlayer.setText("0");
        resultTable.put(resultPlayer.getId(), 0);
        groupNameRow.setVisibility(View.VISIBLE);
        playerRow.setVisibility(View.VISIBLE);
        line.setVisibility(View.VISIBLE);
        message.setText(name);
    }
}
