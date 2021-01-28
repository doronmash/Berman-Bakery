package com.benkatzav.berman;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.graphics.SurfaceTexture;
import android.os.Bundle;
import android.view.TextureView;
import android.widget.Button;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

Button signup,login;
    TextInputEditText email,password;
    TextureView animation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        animation = findViewById(R.id.animation);

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
    }
}