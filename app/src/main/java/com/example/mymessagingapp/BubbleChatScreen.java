package com.example.mymessagingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import com.github.library.bubbleview.BubbleLinearLayout;
import com.github.library.bubbleview.BubbleTextView;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class BubbleChatScreen extends AppCompatActivity {

    private List<ChatModel> listChat;
    private ListView listView;
    private String sender, recipient;
    private CustomAdapter customAdapter;
    //private

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bubble_chat_screen);
        Intent receivedIntent = getIntent();
        recipient = receivedIntent.getStringExtra("recipient");
        sender = ParseUser.getCurrentUser().getUsername();
        listChat = new ArrayList<>();

        listView = findViewById(R.id.bubbleLayout);
        customAdapter = new CustomAdapter(listChat, this);
        listView.setAdapter(customAdapter);
        setUpBubbleChat();
    }

    private void setUpBubbleChat() {
        ParseQuery<ParseObject> sentBySender = ParseQuery.getQuery("Chat").whereEqualTo("sender", sender).whereEqualTo("recipient", recipient);
        ParseQuery<ParseObject> sentByRecipient = ParseQuery.getQuery("Chat").whereEqualTo("sender", recipient).whereEqualTo("recipient", sender);
        ArrayList<ParseQuery<ParseObject>> allQuery = new ArrayList<>();
        allQuery.add(sentBySender);
        allQuery.add(sentByRecipient);
        ParseQuery<ParseObject> parseMessages = ParseQuery.or(allQuery);
        parseMessages.orderByAscending("createdAt");
        parseMessages.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (objects.size()>0 && e==null){
                    for (ParseObject obj:objects){
                        if (obj.get("sender").toString().equals(sender)) {
                            listChat.add(new ChatModel(obj.get("message").toString(), false));
                        } else {
                            listChat.add(new ChatModel(obj.get("message").toString(), true));
                        }
                    }
                    customAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void sendMessage() {

    }
}
