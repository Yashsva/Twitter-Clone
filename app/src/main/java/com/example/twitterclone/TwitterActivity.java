package com.example.twitterclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class TwitterActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ListView listView;
    private ArrayList<String> arrayList;
    private ArrayAdapter arrayAdapter;
    private TextView txtLoadingData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twitter);

        listView=findViewById(R.id.listView);
        arrayList=new ArrayList();
        arrayAdapter=new ArrayAdapter(this,android.R.layout.simple_list_item_checked,arrayList);
        listView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);

        txtLoadingData=findViewById(R.id.txtLoadingData);

        listView.setOnItemClickListener(this);

        try {


            ParseQuery<ParseUser> parseQuery = ParseUser.getQuery();

            parseQuery.whereNotEqualTo("username", ParseUser.getCurrentUser().getUsername());
            parseQuery.findInBackground(new FindCallback<ParseUser>() {
                @Override
                public void done(List<ParseUser> objects, ParseException e) {

                    if (e == null) {
                        if (objects.size() > 0) {

                            for (ParseUser user : objects) {
                                arrayList.add(user.getUsername());
                            }

                            listView.setAdapter(arrayAdapter);
                            txtLoadingData.animate().alpha(0).setDuration(2000);
                            listView.setVisibility(View.VISIBLE);

                            for(String twitterUser:arrayList)
                            {
                                if(ParseUser.getCurrentUser().getList("following")!=null)
                                {
                                    if (ParseUser.getCurrentUser().getList("following").contains(twitterUser))
                                    {
                                        listView.setItemChecked(arrayList.indexOf(twitterUser), true);
                                    }
                                }
                            }

                        }
                    } else {
                        Toast.makeText(TwitterActivity.this, "Error : " + e.getMessage(), Toast.LENGTH_SHORT).show();

                    }

                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId()==R.id.logoutUserItem)
        {
            ParseUser.getCurrentUser().logOut();
            Intent intent=new Intent(TwitterActivity.this,SignUp.class);
            startActivity(intent);
            finish();



        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        CheckedTextView checkedTextView= (CheckedTextView) view;

        if(checkedTextView.isChecked())
        {
            Toast.makeText(this,arrayList.get(position) +" is now followed !",Toast.LENGTH_SHORT).show();


            ParseUser.getCurrentUser().add("following",arrayList.get(position));
        }
        else
        {
            Toast.makeText(this,arrayList.get(position) +" is no more followed !",Toast.LENGTH_SHORT).show();

            ParseUser.getCurrentUser().getList("following").remove(arrayList.get(position));
            ParseUser.getCurrentUser().put("following",ParseUser.getCurrentUser().getList("following"));

        }

        ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e==null)
                {
                    Toast.makeText(TwitterActivity.this,"Saved",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(TwitterActivity.this,"Error : "+ e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
