package com.example.myapplication2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main2);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button gotochatButton = findViewById(R.id.gotochatButton);
        EditText usernameInput = findViewById(R.id.usernameInput);
        EditText roomInput = findViewById(R.id.roomInput);

        // Set an OnClickListener on the Button
        gotochatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Set Default value
                int roomID = -1;

                String username = usernameInput.getText().toString();
                roomID = Integer.parseInt(roomInput.getText().toString());

                if (!username.isEmpty() && roomID > 0) {
                    Intent switchActivityIntent = new Intent(MainActivity2.this, MainActivity.class);
                    switchActivityIntent.putExtra("username", username);
                    switchActivityIntent.putExtra("roomID", roomID);
                    startActivity(switchActivityIntent);
                }
            }
        });


    }
}