package com.example.snapchatclone;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import java.io.InputStream;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class ShowSnapActivity extends AppCompatActivity {

    TextView textView;
    ImageView imageView;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_snap);

        textView = findViewById(R.id.textView);
        imageView = findViewById(R.id.imageView);
        mAuth=FirebaseAuth.getInstance();

        textView.setText(getIntent().getStringExtra("message"));

        ImageDownloader task = new ImageDownloader();
        Bitmap myImage;
        try {
            myImage = task.execute(getIntent().getStringExtra("imageURL")).get();

            imageView.setImageBitmap(myImage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class ImageDownloader extends AsyncTask<String, Void, Bitmap> {


        @Override
        protected Bitmap doInBackground(String... urls) {
            try {

                URL url = new URL(urls[0]);
                HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
                connection.connect();
                InputStream in = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(in);
                return myBitmap;
            } catch (Exception e) {
                e.printStackTrace();
                return null;

            }
        }
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();

        FirebaseDatabase.getInstance().getReference().child("users").child(mAuth.getCurrentUser().getUid()).child("snaps").child(getIntent().getStringExtra("snapKey")).removeValue();

        FirebaseStorage.getInstance().getReference().child("images").child(getIntent().getStringExtra("imageName")).delete();
    }
}
