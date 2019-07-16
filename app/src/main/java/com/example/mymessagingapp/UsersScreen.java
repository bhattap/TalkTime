package com.example.mymessagingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class UsersScreen extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ListView usersListView;
    private ArrayList<String> allUsersNames;
    private ArrayAdapter userArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_screen);

        usersListView = findViewById(R.id.usersListView);
        allUsersNames = new ArrayList<>();
        userArrayAdapter = new ArrayAdapter(this,
                android.R.layout.simple_list_item_1, allUsersNames);
        getAllUsers();
        //usersListView.setOnItemClickListener(this);
    }

    private void getAllUsers() {
        try {
            ParseUser.getQuery()
                    .whereNotEqualTo("username", ParseUser.getCurrentUser())
                    .findInBackground(new FindCallback<ParseUser>() {
                @Override
                public void done(List<ParseUser> parseUsersList, ParseException e) {
                    if (parseUsersList.size()>0 && e==null) {
                        for (ParseUser parseUser : parseUsersList) {
                            allUsersNames.add(parseUser.getUsername());
                        }
                        usersListView.setAdapter(userArrayAdapter);
                    }
                }
            });
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
