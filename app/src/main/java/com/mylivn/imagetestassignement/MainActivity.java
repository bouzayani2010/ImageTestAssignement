package com.mylivn.imagetestassignement;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.bel.lampa.AvatarView;
import com.bel.lampa.loader.Lamp;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Button btn_submit;
    private int index;
    private AvatarView avatarView;
    private Lamp lamp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_submit = findViewById(R.id.submit);

        // avatarView to show
        // Custom Avatar View composed with two  view
        // Image View  rounded
        // And
        // Progress  View
        avatarView = (AvatarView) findViewById(R.id.avatar);

        // Resize the AvatarView , the diamter is on dp implicitly
        avatarView.setDiameter(this, 300);
        // change Color of Circle
        avatarView.setColor(this, getResources().getColor(R.color.green));
        // change Color of Circle Progress
        avatarView.setColorProgress(this, getResources().getColor(R.color.red));

        //   list of urls  should be intialized
        final List<String> urls = new ArrayList<String>();

        urls.add("https://media.licdn.com/dms/image/C560BAQGWXtTo3ZN90g/company-logo_200_200/0?" +
                "e=2159024400&v=beta&t=1Ht2TvYx6T4iRQYEm4wCmQBQsiKAwg1Mret7bSuyMko");
        urls.add("https://images.pexels.com/photos/1157255/pexels-photo-1157255.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=650&w=940");
        urls.add("https://images.pexels.com/photos/1759702/pexels-photo-1759702.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500");
        urls.add("https://startup-jobs-muenchen.de/wp-content/uploads/2016/07/mylivn_logo.png");
        urls.add("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRZ8DBE32pgfYh4IMiUxQ3RiN_zwUVHVocYxZO397lVEXsXjPy1lg");
        urls.add("https://images.pexels.com/photos/681467/pexels-photo-681467.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=5000");
        urls.add("https://image.flaticon.com/icons/png/128/25/25643.png");

        //   Load Image  url in imageview Using Lamp  class
        lamp = Lamp.with(MainActivity.this);
        // urls  image to download
        // we can provide a url string , not a list of string
        lamp.load(urls);


        //lamp.load(urls.get(0);

        //  max Number of object to cash  in memory
        lamp.maxCountCash(2);


        //  max cash disk size, unit implicitly is MB
        lamp.maxDiskCash(20);

        // placeholder when image is dowloaded
        int placeholder = R.drawable.images;
        lamp.placeholder(placeholder);
        // eeror image if  there are no result or some thinfg error
        lamp.error(placeholder);
        // the image view where the photo should be displayed
        lamp.into(avatarView);


        // first Load
        index = 0;
        lamp.rub(index);

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    index++;
                    index = index % lamp.getUrls().size();
                    lamp.rub(index);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }
}
