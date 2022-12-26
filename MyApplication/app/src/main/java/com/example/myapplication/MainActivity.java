package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText loginEditText = findViewById(R.id.edit_text_login);
        EditText registerEditText = findViewById(R.id.edit_text_password);
        TextView errorText = findViewById(R.id.text_view_error_text);

        Button buttonNext = findViewById(R.id.button_next);

        SharedPreferences preferences = getSharedPreferences("LOGIN", MODE_PRIVATE);
        String oldLogin = preferences.getString("LOGIN", null);
        if (oldLogin != null) {
            toSecondActivity(oldLogin);
        }

        buttonNext.setOnClickListener(v -> {
            String loginText = loginEditText.getText().toString();
            String registerText = registerEditText.getText().toString();

            if (loginText.equals("Ivan") && registerText.equals("322")) {
                errorText.setVisibility(View.INVISIBLE);

                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("LOGIN", loginText);
                editor.apply();

                toSecondActivity(loginText);
            } else {
                errorText.setVisibility(View.VISIBLE);
            }
        });

        errorText.setOnClickListener(v -> {
            Intent startActivityIntent = new Intent(this, EmailActivity.class);
            startActivity(startActivityIntent);
        });
    }

    public void toSecondActivity(String login) {
        Intent startActivityIntent = new Intent(this, SecondActivity.class);
        startActivityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivityIntent.putExtra("LOGIN", login);
        startActivity(startActivityIntent);
    }
}