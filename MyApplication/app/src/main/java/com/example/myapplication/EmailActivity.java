package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.regex.Pattern;

public class EmailActivity extends AppCompatActivity {

    final String EMAIL_REGUX = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    Pattern emailPattern = Pattern.compile(EMAIL_REGUX, Pattern.CASE_INSENSITIVE);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email);

        EditText emailEditText = findViewById(R.id.edit_text_email);
        Button okButton = findViewById(R.id.button_ok);
        TextView emailErrorTextView = findViewById(R.id.text_view_error_email);

        okButton.setOnClickListener(v -> {
            String emailText = emailEditText.getText().toString();

            if (emailPattern.matcher(emailText).find()) {
                Intent startActivityIntent = new Intent(this, SecondActivity.class);
                startActivity(startActivityIntent);
            } else {

                emailErrorTextView.setVisibility(View.VISIBLE);
            }

        });
    }


}