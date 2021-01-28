package com.benkatzav.berman;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.graphics.SurfaceTexture;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.Surface;
import android.view.TextureView;
import android.widget.Button;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

Button signup,login;
    TextInputEditText email,password;
    TextureView animation;
    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        animation = findViewById(R.id.animation);
        animation.setSurfaceTextureListener(surfaceTextureListener);

        callFragment(new FragLogin());
    }

    public void callFragment(GeneralFragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, (Fragment) fragment, fragment.getClass().getSimpleName())
                .commit();
    }

    @Override
    public void onBackPressed() {
        GeneralFragment previousFragment = GeneralFragment.getCurrentFragment().getPreviousFragment();

        if (GeneralFragment.getCurrentFragment() instanceof FragMenu) {
            return;
        }

        if (previousFragment != null) {
            GeneralFragment.setCurrentFragment(previousFragment);
            callFragment(previousFragment);
        } else {
            moveTaskToBack(true);
        }
    }

    TextureView.SurfaceTextureListener surfaceTextureListener = new TextureView.SurfaceTextureListener() {
        @Override
        public void onSurfaceTextureAvailable(@NonNull SurfaceTexture surface, int width, int height) {
            Surface s = new Surface(surface);

            try {
                mediaPlayer = new MediaPlayer();
                mediaPlayer.setDataSource(getApplicationContext(),  Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.bread));
                mediaPlayer.setSurface(s);
                mediaPlayer.prepareAsync();
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        mediaPlayer.start();
                    }
                });
                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        mediaPlayer.start();
                    }
                });
            } catch (IllegalArgumentException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (SecurityException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IllegalStateException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        @Override
        public void onSurfaceTextureSizeChanged(@NonNull SurfaceTexture surface, int width, int height) {

        }

        @Override
        public boolean onSurfaceTextureDestroyed(@NonNull SurfaceTexture surface) {
            return false;
        }

        @Override
        public void onSurfaceTextureUpdated(@NonNull SurfaceTexture surface) {

        }
    };
}