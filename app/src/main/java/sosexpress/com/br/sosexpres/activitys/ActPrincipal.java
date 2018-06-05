package sosexpress.com.br.sosexpres.activitys;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import sosexpress.com.br.sosexpres.R;
import sosexpress.com.br.sosexpres.fragments.MapaUsuario;
import sosexpress.com.br.sosexpres.utils.Preferences;


public class ActPrincipal extends AppCompatActivity {
    private final int MODE = 0;
    private final String NOME_ARQUIVO = "sos.preferences";

    private FragmentManager fragmentManager;

    AlertDialog alert;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_principal);
        getSupportActionBar().setTitle("MAPA DE OFICINAS");

        fragmentManager = getSupportFragmentManager();


        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.container, new MapaUsuario(), "MapaUsuario");
        transaction.commitAllowingStateLoss();

        dialog  = new Dialog(this);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.act_principal,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.sair:
                confirmSair();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void  confirmSair(){
        dialog.setContentView(R.layout.dialog_sair);
        TextView sair = dialog.findViewById(R.id.txtclose);
        sair.setText("X");
        Button btn_confirm = dialog.findViewById(R.id.btn_confirmar);

        sair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = getSharedPreferences(NOME_ARQUIVO,MODE).edit();
                editor.remove(Preferences.EMAIL_CLIENTE);
                editor.remove(Preferences.SENHA_CLIENTE);
                editor.remove(Preferences.ID_CLIENTE);
                editor.remove(Preferences.ID_OFICINA);
                editor.remove(Preferences.EMAIL_OFICINA);
                editor.remove(Preferences.SENHA_OFICINA);
                editor.commit();

                finish();
                Intent intent = new Intent(ActPrincipal.this, OpcaoLogin.class);
                startActivity(intent);
            }
        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

    }

}
