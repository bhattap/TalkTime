package com.example.mymessagingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.ArrayList;
import java.util.List;

public class ChatScreen extends AppCompatActivity implements View.OnClickListener {

    private ImageView sendMessageBtn;
    private ListView chatListView;
    private EditText messageEditText;
    private String recipient, recipientName, sender;
    private ArrayAdapter chatArrayAdapter;
    private ArrayList<String> chatMessagesList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_screen);
        Intent receivedIntent = getIntent();
        recipient = receivedIntent.getStringExtra("recipient");
        recipientName = receivedIntent.getStringExtra("recipientName");
        sender = ParseUser.getCurrentUser().getUsername();
        //senderName = ParseUser.getCurrentUser().get("firstName")+ LAST NAME
        setTitle(recipientName);

        chatListView = findViewById(R.id.chatListView);
        messageEditText = findViewById(R.id.messageEditText);
        sendMessageBtn = findViewById(R.id.sendMessageBtn);
        sendMessageBtn.setOnClickListener(this);

        chatMessagesList = new ArrayList<>();
        chatArrayAdapter = new ArrayAdapter(this,
                android.R.layout.simple_list_item_1, chatMessagesList);
        chatListView.setAdapter(chatArrayAdapter);
        setUpChatHistory();
    }

    private void setUpChatHistory() {
        try {
            //get all the chat messages sent by sender and received by recipient
            //also get the ones sent by recipient and received by sender
            //order these two lists
            //display the messages on the listview
            ParseQuery<ParseObject> sentBySender =
                    ParseQuery.getQuery("Chat")
                        .whereEqualTo("sender", sender)
                        .whereEqualTo("recipient", recipient);
            ParseQuery<ParseObject> sentByRecipient =
                    ParseQuery.getQuery("Chat")
                            .whereEqualTo("sender", recipient)
                            .whereEqualTo("recipient", sender);
            //create an arraylist for the two queries
            ArrayList<ParseQuery<ParseObject>> allQueries = new ArrayList<>();
            allQueries.add(sentBySender);
            allQueries.add(sentByRecipient);

            //create a new query to order our arraylist
            ParseQuery<ParseObject> myQuery = ParseQuery.or(allQueries);
            myQuery.orderByAscending("createdAt");
            myQuery.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> objects, ParseException e) {
                    if (objects.size()>0 && e==null){
                        for (ParseObject message : objects){
                            chatMessagesList.add(
                                    message.get("sender")+ ": "+ message.get("message"));
                        }
                        chatArrayAdapter.notifyDataSetChanged();
                    }
                }
            });


        } catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case (R.id.sendMessageBtn):
                if (!messageEditText.getText().toString().isEmpty()) {
                    //add the new message to the server
                    ParseObject newChatObject = new ParseObject("Chat");
                    newChatObject.put("sender", sender);
                    newChatObject.put("recipient", recipient);
                    newChatObject.put("message", messageEditText.getText().toString());
                    newChatObject.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e==null){
                                //display on the screen
                                chatMessagesList.add(sender+": "+messageEditText.getText());
                                chatArrayAdapter.notifyDataSetChanged();

                                //clear out the edit text field
                                messageEditText.setText("");
                            } else {
                                FancyToast.makeText(ChatScreen.this, e.getMessage(),
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
