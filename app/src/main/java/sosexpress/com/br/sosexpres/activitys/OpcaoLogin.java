package sosexpress.com.br.sosexpres.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import sosexpress.com.br.sosexpres.R;

public class OpcaoLogin extends AppCompatActivity {

    private Button btn_usu_login;
    private Button btn_usu_ofic;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opcao_de_login);

        btn_usu_login = findViewById(R.id.btn_usu_login);
        btn_usu_ofic = findViewById(R.id.btn_usu_ofic);
    }

    public void telaLogOficina(View view){
        Intent intent = new Intent(OpcaoLogin.this, LoginOficina.class);
        startActivity(intent);
    }

    public void telaLogUsuario(View view){
        Intent intent = new Intent(OpcaoLogin.this, LoginCliente.class);
        startActivity(intent);
    }
}
