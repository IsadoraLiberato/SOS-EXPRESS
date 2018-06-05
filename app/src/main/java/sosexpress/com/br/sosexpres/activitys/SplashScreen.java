package sosexpress.com.br.sosexpres.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import sosexpress.com.br.sosexpres.R;
import sosexpress.com.br.sosexpres.utils.Preferences;

public class SplashScreen extends AppCompatActivity {
    private Preferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        preferences = new Preferences(this);

        Toast.makeText(this, ""+preferences.isShowIntro(), Toast.LENGTH_SHORT).show();

        Handler handle = new Handler();
        handle.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(preferences.isShowIntro()){
                    telaAcaoUsuario();
                }else{
                    mostrarLogin();
                }

            }
        }, 2500);
    }

    private void mostrarLogin() {
        Intent intent = new Intent(SplashScreen.this,
                IntroScreen.class);
                startActivity(intent);
                finish();
    }

    private void telaAcaoUsuario(){
        Intent intent = new Intent(SplashScreen.this,
                ActivityAcaoUsuario.class);
                startActivity(intent);
                finish();
    }

}
