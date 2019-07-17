package com.example.mymessagingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.LogOutCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.shashank.sony.fancytoastlib.FancyToast;

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
        setTitle("All Users");

        usersListView = findViewById(R.id.usersListView);
        allUsersNames = new ArrayList<>();
        userArrayAdapter = new ArrayAdapter(this,
                android.R.layout.simple_list_item_1, allUsersNames);
        getAllUsers();
        usersListView.setOnItemClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){

            case (R.id.logoutBtn):
                ParseUser.logOutInBackground(new LogOutCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e==null){
                            FancyToast.makeText(UsersScreen.this, "You are now logged out",
                                    Toast.LENGTH_SHORT, FancyToast.INFO, false).show();

                            Intent intent = new Intent(UsersScreen.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
                break;

        }

        return super.onOptionsItemSelected(item);
    }

    private void getAllUsers() {
        try {
            ParseUser.getQuery()
                    .whereNotEqualTo("username", ParseUser.getCurrentUser().getUsername())
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
        Intent intent = new Intent(this, ChatScreen.class);
        intent.putExtra("recipient", allUsersNames.get(position));
        startActivity(intent);
    }
}
