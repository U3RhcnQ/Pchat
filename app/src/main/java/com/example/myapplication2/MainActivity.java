package com.example.myapplication2;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MyActivity";
    private int RoomID = 0;
    private int MessageID = 0;
    private final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef;
    private DatabaseReference lastMessageKey;
    private String lastKey ="";
    private String username = "";
    // Create a new message
    Map<String, Object> message = new HashMap<>();
    // Create a list of messages to bind to the RecyclerView
    private List<Message> messageList;
    private MessageAdapter messageAdapter;
    private RecyclerView recyclerView;

    public FirebaseDatabase getDatabase() {
        return database;
    }

    public int getRoomID() {
        return RoomID;
    }

    public void setRoomID(int ID){
        this.RoomID = ID;
    }

    private void sendMessage(String text) {

        // Write a message to the database
        message.put("user", username);
        message.put("content", text);
        message.put("timestamp", System.currentTimeMillis());

        // Push the new message to the database
        DatabaseReference newMessageRef = this.myRef.push(); // Generates a unique key
        // Retrieve the unique key

        // set the last key for all users
        this.lastKey = newMessageRef.getKey();
        newMessageRef.setValue(message);
        lastMessageKey.setValue(this.lastKey);
        addMessage(new Message(text, "You",true));

    }

    // Method to add a new message dynamically
    public void addMessage(Message message) {
        messageList.add(message);
        messageAdapter.notifyItemInserted(messageList.size() - 1);
        recyclerView.scrollToPosition(messageList.size() - 1);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            username = extras.getString("username");
            setRoomID(extras.getInt("roomID"));
            //The key argument here must match that used in the other activity
        }

        // Ensure we have correct data:
        myRef = this.database.getReference("Rooms/"+this.RoomID+"/");
        lastMessageKey = this.database.getReference("Rooms/"+this.RoomID+"/lastKey");

        // Find views by their IDs
        Button myButton = findViewById(R.id.buttonSend);
        Button backButton = findViewById(R.id.backButton);
        EditText myTextView = findViewById(R.id.editTextMessage);
        recyclerView = findViewById(R.id.messageRecyclerView);


        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // Initialize the message list
        messageList = new ArrayList<>();
        // Set up the adapter
        messageAdapter = new MessageAdapter(messageList);
        recyclerView.setAdapter(messageAdapter);

        // Add dummy data
        messageList.add(new Message("Hello!", "Bot",false));
        messageList.add(new Message("Welcome to the chat.", "Bot", false));

        // Log the size of the list
        Log.d("MessageList", "Size: " + messageList.size());
        for (Message message : messageList) {
            Log.d("MessageList", "Message: " + message);
        }


        // Read from the database
        lastMessageKey.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                if (value != null) {
                    DatabaseReference newMsg = getDatabase().getReference("Rooms/" + getRoomID() + "/" + value);
                    newMsg.get().addOnSuccessListener(dataSnapshot2 -> {

                        // This method is called when the task is successful
                        GenericTypeIndicator<HashMap<String, Object>> genericTypeIndicator = new GenericTypeIndicator<HashMap<String, Object>>() {};
                        HashMap<String, Object> content = dataSnapshot2.getValue(genericTypeIndicator); // Fetch content using GenericTypeIndicator

                        Log.d(TAG, "Value is: " + content + " Index: " + value + " Last sent Id: " + lastKey);

                        // Make sure we don't echo our own message

                        if (!lastKey.equals(value)) {
                            assert content != null;
                            if (!Objects.equals(content.get("user"), username)) {
                                addMessage(new Message(content.get("content").toString(), content.get("user").toString(), false));
                            }
                        }


                    }).addOnFailureListener(e -> {
                        // This method is called if there's an error in fetching the data
                        Log.e(TAG, "Failed to retrieve data", e);
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        // Set an OnClickListener on the Button
        myButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Change the text in the TextView
                String message = myTextView.getText().toString();
                if (!message.isEmpty()) {
                    sendMessage(message);
                }
                myTextView.setText("");
            }
        });

        // Set an OnClickListener on the Button
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}