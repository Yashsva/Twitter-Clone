package com.example.twitterclone;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PostTweet extends AppCompatActivity implements View.OnClickListener {

    private EditText edtTweet;
    private Button btnPostTweet,btnOthersTweet;
    private ListView listView;
    private SimpleAdapter simpleAdapter;
    private ArrayList<HashMap<String,String>> tweetList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_tweet);

        setTitle("Tweets");

        edtTweet=findViewById(R.id.edtTweet);
        btnPostTweet=findViewById(R.id.btnPostTweet);

        btnOthersTweet=findViewById(R.id.btnOthersTweets);
        listView=findViewById(R.id.listTweets);



        listView.setVisibility(View.GONE);

        btnPostTweet.setOnClickListener(this);

        btnOthersTweet.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.btnPostTweet:
                final String tweet=edtTweet.getText().toString();
                if(tweet.equals(""))
                {
                    Toast.makeText(this,"Please fill empty fields",Toast.LENGTH_SHORT).show();
                }
                else
                {

                    ParseObject parseObject=new ParseObject("tweet");
                    parseObject.put("postText",tweet);
                    parseObject.put("username",ParseUser.getCurrentUser().getUsername());
                    parseObject.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if(e==null)
                            {
                                Toast.makeText(PostTweet.this,"Posted : "+tweet,Toast.LENGTH_SHORT).show();
                                finish();

                            }else
                            {
                                Toast.makeText(PostTweet.this,"Error : "+ e.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                break;


            case R.id.btnOthersTweets:

                tweetList=new ArrayList();
                simpleAdapter=new SimpleAdapter(this,tweetList,android.R.layout.simple_list_item_2,new String[]{"tweetUserName","tweetText"},new int[]{android.R.id.text1,android.R.id.text2});



                ParseQuery<ParseObject> queryTweets=new ParseQuery<ParseObject>("tweet");
                queryTweets.whereContainedIn("username",ParseUser.getCurrentUser().getList("following"));
                queryTweets.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> objects, ParseException e) {
                        if(e==null)
                        {
                            if(objects.size()>0)
                            {
                                for(ParseObject objTweets:objects)
                                {
                                    HashMap<String,String> userTweet=new HashMap<>();
                                    userTweet.put("tweetUserName",objTweets.getString("username"));
                                    userTweet.put("tweetText",objTweets.getString("postText"));
                                    tweetList.add(userTweet);
                                }

                                listView.setAdapter(simpleAdapter);
                                listView.setVisibility(View.VISIBLE);

                            }
                            else
                            {
                                Toast.makeText(PostTweet.this,"No Posts Available",Toast.LENGTH_SHORT).show();

                            }
                        }
                        else
                        {
                            Toast.makeText(PostTweet.this,"Error : "+ e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                break;
        }
    }
}
