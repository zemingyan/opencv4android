package com.experiment.first;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import org.opencv.videoio.VideoCapture;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    public static Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = getApplicationContext();

        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File file = MainActivity.context.getFilesDir();
                Log.d("外部存储路径"," ===== " + file.getPath());


                Drawable drawable = context.getDrawable(R.drawable.logo);
                Uri mUri = Uri.parse("android.resource://" + getPackageName() + "/"+ R.raw.ball_tracking_example);
                Log.d("uri的真实路径是", "======= "+ mUri.getPath());
                MediaPlayer mediaPlayer = new MediaPlayer();
                try {
                    mediaPlayer.setDataSource(getApplicationContext(), mUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                VideoCapture videoCapture = new VideoCapture("/storage/DCIM/ball_tracking_example.mp4");
            }
        });


    }
}