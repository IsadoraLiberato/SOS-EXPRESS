package sosexpress.com.br.sosexpres.activitys;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.iid.FirebaseInstanceId;

import sosexpress.com.br.sosexpres.R;
import sosexpress.com.br.sosexpres.utils.Constantes;


public class ActivityAcaoUsuario extends AppCompatActivity {

    private Button btn_opcao_cadastro;
    private Button btn_opcao_login;

    private AlertDialog mNoGpsDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acticity_tela_acao);


        getSupportActionBar().hide();

        if(!verificaGps()){
            createNoGpsDialog();
        }

        btn_opcao_cadastro = findViewById(R.id.btn_opcao_cadastro);
        btn_opcao_login = findViewById(R.id.btn_opcao_login);


    }

    public void opcaoLogin(View view){
        Intent i = new Intent(ActivityAcaoUsuario.this, OpcaoLogin.class);
        startActivity(i);

    }

    public void opcaoCadastro(View view){
        Intent i = new Intent(ActivityAcaoUsuario.this, OpcaoCadastro.class);
        startActivity(i);

    }

    //solicita que usuário ative o GPS ao carregar a tela de ação
    private void createNoGpsDialog(){
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        Intent callGPSSettingIntent = new Intent(
                                android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(callGPSSettingIntent);
                        break;
                }
            }
        };

        DialogInterface.OnClickListener opcaoCancelar = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        mNoGpsDialog = builder.setMessage("Esse App necessita que o GPS esteja ativo\n" +
                "Deseja ativá-lo?")
                .setPositiveButton("Ativar", dialogClickListener)
                .setNegativeButton("Cancelar", opcaoCancelar)
                .setCancelable(false)
                .create();

        mNoGpsDialog.show();

    }

    //verifica se GPS já está ativo
    private boolean verificaGps(){
        LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );
        boolean isOn = manager.isProviderEnabled( LocationManager.GPS_PROVIDER);

        if(isOn){
            return isOn;
        }
        return false;
    }


}
