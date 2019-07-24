package com.example.mymessagingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.github.library.bubbleview.BubbleLinearLayout;
import com.github.library.bubbleview.BubbleTextView;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.ArrayList;
import java.util.List;

public class BubbleChatScreen extends AppCompatActivity implements View.OnClickListener {

    private List<ChatModel> listChat;
    private ListView listView;
    private String sender, recipient, recipientName;
    private CustomAdapter customAdapter;
    private EditText messageContent;
    private ImageView sendImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bubble_chat_screen);
        Intent receivedIntent = getIntent();
        recipient = receivedIntent.getStringExtra("recipient");
        recipientName = receivedIntent.getStringExtra("recipientName");
        sender = ParseUser.getCurrentUser().getUsername();
        setTitle(recipientName);
        listChat = new ArrayList<>();
        listView = findViewById(R.id.bubbleLayout);
        messageContent = findViewById(R.id.messageContent);
        sendImageView = findViewById(R.id.sendImageView);
        sendImageView.setOnClickListener(this);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case (R.id.sendImageView):
                if (!messageContent.getText().toString().isEmpty()) {
                    //add the new message to the server
                    ParseObject newChatObject = new ParseObject("Chat");
                    newChatObject.put("sender", sender);
                    newChatObject.put("recipient", recipient);
                    newChatObject.put("message", messageContent.getText().toString());
                    newChatObject.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e==null){
                                //display on the screen
                                listChat.add(new ChatModel(messageContent.getText().toString(), false));
                                customAdapter.notifyDataSetChanged();

                                //clear out the edit text field
                                messageContent.setText("");
                            } else {
                                FancyToast.makeText(BubbleChatScreen.this, e.getMessage(),
                                        Toast.LENGTH_LONG, FancyToast.ERROR,
                                        false).show();
                            }
                        }
                    });
                }
                break;
        }
    }
}
