package com.apitechnosoft.ipad.utils;

import java.io.IOException;
import android.app.Dialog;
import android.content.Context;
import android.graphics.PixelFormat;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;

import com.apitechnosoft.ipad.R;

public class VideoPopup  extends Dialog implements SurfaceHolder.Callback
{
    MediaPlayer mediaPlayer;
    SurfaceView surfaceView;
    SurfaceHolder surfaceHolder;
    boolean pausing = false;;
    String videoPath = "";
    String videoName = "";

    public VideoPopup(Context context, final String name, final String path)
    {
        super(context);
        this.videoName = name;
        this.videoPath = path;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_custom_dialog);
        setTitle(this.videoName);

        //------ ------ ------
        Button buttonStartVideo = (Button)findViewById(R.id.start_button);
        Button buttonPauseVideo = (Button)findViewById(R.id.pause_button);
        Button buttonStopVideo = (Button)findViewById(R.id.end_button);

        getWindow().setFormat(PixelFormat.UNKNOWN);
        surfaceView = (SurfaceView)findViewById(R.id.videoElem);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        mediaPlayer = new MediaPlayer();

        //Handle the click-event on start button
        buttonStartVideo.setOnClickListener(new Button.OnClickListener(){


            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                pausing = false;

                if(mediaPlayer.isPlaying()){
                    mediaPlayer.reset();
                }

                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mediaPlayer.setDisplay(surfaceHolder);

                try {
                    mediaPlayer.setDataSource(videoPath);
                    mediaPlayer.prepare();
                } catch (IllegalArgumentException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IllegalStateException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                mediaPlayer.start();
            }});


        //Handle the click-event on pause button
        buttonPauseVideo.setOnClickListener(new Button.OnClickListener(){


            @Override
            public void onClick(View v) {

                if(pausing){
                    pausing = false;
                    mediaPlayer.start();
                } else{
                    pausing = true;
                    mediaPlayer.pause();
                }
            }});

        //Handle the click-event on stop button
        buttonStopVideo.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) { mediaPlayer.stop(); }});

    }


    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) { }


    @Override
    public void surfaceCreated(SurfaceHolder holder) { }


    @Override
    public void surfaceDestroyed(SurfaceHolder holder) { }


}

