package sosexpress.com.br.sosexpres.activitys;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.github.florent37.viewtooltip.ViewTooltip;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sosexpress.com.br.sosexpres.GeneratoRetrofit.RetrofitServiceGenerator;
import sosexpress.com.br.sosexpres.R;
import sosexpress.com.br.sosexpres.RestClient.RetrofitService;
import sosexpress.com.br.sosexpres.entidades.MensagemLogin;
import sosexpress.com.br.sosexpres.utils.Preferences;

public class LoginCliente extends AppCompatActivity {

    private EditText edt_email;
    private EditText edt_senha;
    private Button btn_acessar;
    private Button btn_cadastro;

    private Preferences preferences;
    ProgressDialog progress;


    private int id_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_cliente);

        preferences = new Preferences(LoginCliente.this);
        edt_email =  findViewById(R.id.edt_email_login);
        edt_senha =  findViewById(R.id.edt_senha_login);
        btn_acessar =  findViewById(R.id.btn_acessar);

        verificaSeEstaLogado();

    }

    public void fazerLogin(View view)
    {

        String email = edt_email.getText().toString();
        String senha = edt_senha.getText().toString();

        boolean validaCliente = validaCampos(email, senha);

        if(validaCliente){
            //Chamando o progress
            progress = new ProgressDialog(LoginCliente.this);
            progress.setTitle("Verificando dados..... ");
            progress.show();

            retornaCliente(email,senha);
        }

    }


    private void telamapa (){
        Intent intent = new Intent(LoginCliente.this, ActPrincipal.class);
        intent.putExtra("id",preferences.getIdCliente());
        startActivity(intent);
        finish();
     }

    public void verificaSeEstaLogado(){

        String email = preferences.getEmailCliente();
        String senha = preferences.getSenhaCliente();

        if(email != null && senha != null){

            telamapa();
        }
    }
    private boolean resultado;

    public boolean retornaCliente(final String email, final String senha){

        RetrofitService service = RetrofitServiceGenerator.createService(RetrofitService.class);

        Call<MensagemLogin> call = service.getCliente(email);

        call.enqueue(new Callback<MensagemLogin>() {

            @Override
            public void onResponse(Call<MensagemLogin> call, Response<MensagemLogin> response) {
                if (response.isSuccessful()) {

                    MensagemLogin msgL = response.body();

                    //verifica aqui se o corpo da resposta não é nulo
                            Log.i("MensagemRetorno ", msgL.getMsg());
                        if (msgL.getMsg().equals("true") && msgL.getEmail().equals(email) && msgL.getSenha().equals(senha)){
                            preferences.salvaIdCliente(msgL.getId());
                            preferences.salvarDados(email,senha);
                            id_user = preferences.getIdCliente();
                            progress.dismiss();

                            Toast.makeText(LoginCliente.this, "Dados conferem", Toast.LENGTH_SHORT).show();
                            telamapa();
                            
                            }else{
                            AlertDialog.Builder alert = new AlertDialog.Builder(LoginCliente.this);
                            alert.setTitle("Erro");
                            alert.setMessage("Os dados digitados não correspondem a nenhum cliente cadastrado");
                            alert.setNeutralButton("OK", null);
                            alert.create()
                                    .show();
                            progress.dismiss();

                        }

                }else {
                    Toast.makeText(LoginCliente.this, "Não obteve resposta do servidor", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<MensagemLogin> call, Throwable t) {
                Log.e("AppCep", "Não foi possível recuperar o Cep", t);

            }
        });

        return resultado;

    }

    private boolean validaCampos(String emailDig, String senhaDig){
        boolean result = true;

        if("".equals(emailDig) || emailDig == null){
            ViewTooltip
                    .on(this, edt_email)
                    .autoHide(true, 3000)
                    .corner(30)
                    .position(ViewTooltip.Position.TOP)
                    .text("Campo de email obrigtório")
                    .color(Color.RED)
                    .show();
            result = false;
        }
        if(("".equals(emailDig) || emailDig == null) && ("".equals(senhaDig) || senhaDig == null)){
            ViewTooltip
                    .on(this, edt_email)
                    .autoHide(true, 3000)
                    .corner(30)
                    .position(ViewTooltip.Position.TOP)
                    .text("Campo de email obrigtório")
                    .color(Color.RED)
                    .show();

            ViewTooltip
                    .on(this, edt_senha)
                    .autoHide(true, 3000)
                    .corner(30)
                    .position(ViewTooltip.Position.TOP)
                    .text("Campo de senha obrigtório")
                    .color(Color.RED)
                    .show();
            result = false;
        }
        if ("".equals(senhaDig) || senhaDig == null){
            ViewTooltip
                    .on(this, edt_senha)
                    .autoHide(true, 3000)
                    .corner(30)
                    .position(ViewTooltip.Position.TOP)
                    .text("Campo de senha obrigtório")
                    .color(Color.RED)
                    .show();
            result = false;
        }

        return  result;

    }


}
