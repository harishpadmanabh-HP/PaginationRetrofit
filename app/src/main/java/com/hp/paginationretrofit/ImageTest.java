package com.hp.paginationretrofit;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.squareup.picasso.Picasso;

public class ImageTest extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_test);
        ImageView imageView=findViewById(R.id.imageView);
        String url="http://image.tmdb.org/t/p/w780/4W0FnjSGn4x0mKZlBRx8OjFxQUM.jpg";
      // Picasso.get().load("https://i.ibb.co/wsM1wr9/0956089226fe3fad0a9da14dcf82ae43.jpg").into(imageView);
        Glide.with(this).load(url).into(imageView);



    }
}
