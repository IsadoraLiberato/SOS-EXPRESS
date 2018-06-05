package sosexpress.com.br.sosexpres.activitys;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.github.florent37.viewtooltip.ViewTooltip;

import sosexpress.com.br.sosexpres.GeneratoRetrofit.RetrofitServiceGenerator;
import sosexpress.com.br.sosexpres.R;
import sosexpress.com.br.sosexpres.RestClient.RetrofitService;
import sosexpress.com.br.sosexpres.entidades.MensagemLogin;
import sosexpress.com.br.sosexpres.utils.Preferences;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginOficina extends AppCompatActivity {

    private EditText edt_email;
    private EditText edt_senha;
    private Button btn_acessarOficina;
    private int id_oficina;

    Preferences preferences;
    ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_oficina);

        preferences = new Preferences(LoginOficina.this);
        edt_email =  findViewById(R.id.edt_email_loginOficina);
        edt_senha =  findViewById(R.id.edt_senha_loginOficina);
        btn_acessarOficina =  findViewById(R.id.btn_acessarOficina);

        verificaSeEstaLogado();

    }

    private void verificaSeEstaLogado() {
        String email = preferences.getEmailOficina();
        String senha = preferences.getSenhaOficina();

        if(email != null && senha != null){

            telaOficina();
        }
    }

    private void telaOficina() {
        Intent intent = new Intent(LoginOficina.this, ActivityAreaOficina.class);
        intent.putExtra("id",preferences.getIdOficina());
        startActivity(intent);
        finish();
    }

    public void fazerLoginOficina(View view)
    {

        String email = edt_email.getText().toString();
        String senha = edt_senha.getText().toString();



        if(validaCampos(email,senha)){
            retornaOficina(email,senha,view);

            //Chamando o progress
            progress = new ProgressDialog(LoginOficina.this);
            progress.setTitle("Verificando dados..... ");
            progress.show();
        }


    }
    private boolean resultado;

    private void retornaOficina(final String email, final String senha, final View view) {

        RetrofitService service = RetrofitServiceGenerator.createService(RetrofitService.class);

        Call<MensagemLogin> call = service.getOficinaEmail(email);

        call.enqueue(new Callback<MensagemLogin>() {

            @Override
            public void onResponse(Call<MensagemLogin> call, Response<MensagemLogin> response) {

                if (response.isSuccessful()) {

                    MensagemLogin msgL = response.body();

                    //verifica aqui se o corpo da resposta não é nulo
                    if(msgL != null){
                        Log.i("MensagemRetorno ", msgL.getMsg());

                        if (msgL.getMsg().equals("true") && msgL.getEmail().equals(email) && msgL.getSenha().equals(senha)){
                            preferences.salvaIdOficina(msgL.getId());
                            preferences.salvarDadosOficina(email,senha);

                            id_oficina = preferences.getIdOficina();
                            progress.dismiss();
                            Toast.makeText(LoginOficina.this, "Usuario Autorizado", Toast.LENGTH_SHORT).show();

                            telaOficina();

                        }else{
                                AlertDialog.Builder alert = new AlertDialog.Builder(LoginOficina.this);
                                alert.setTitle("Erro");
                                alert.setMessage("Os dados digitados não correspondem a nenhuma oficina cadastrada");
                                alert.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                    }
                                });
                                alert.create()
                                        .show();
                                progress.dismiss();
                            }

                    } else {

                    Toast.makeText(getApplicationContext(), "Resposta falhou verifique sua conexão", Toast.LENGTH_SHORT).show();
                    // segura os erros de requisição
                    ResponseBody errorBody = response.errorBody();
                    //progress.dismiss();
                }

            }else{
                    Snackbar.make(view, "Resposta sem sucesso do servido!!!",Snackbar.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<MensagemLogin> call, Throwable t) {
                Log.e("AppCep", "Não foi possível recuperar a oficina", t);

            }
        });


    }
    private boolean validaCampos(String emailDig, String senhaDig) {
        boolean result = true;

        if ("".equals(emailDig) || emailDig == null) {
            ViewTooltip
                    .on(this, edt_email)
                    .autoHide(true, 3000)
                    .corner(30)
                    .position(ViewTooltip.Position.TOP)
                    .text("Campo de email obrigatório")
                    .color(Color.RED)
                    .show();
            result = false;

        }
        if (("".equals(emailDig) || emailDig == null) && ("".equals(senhaDig) || senhaDig == null)) {
            ViewTooltip
                    .on(this, edt_email)
                    .autoHide(true, 3000)
                    .corner(30)
                    .position(ViewTooltip.Position.TOP)
                    .text("Campo de email obrigatório")
                    .color(Color.RED)
                    .show();

            ViewTooltip
                    .on(this, edt_senha)
                    .autoHide(true, 3000)
                    .corner(30)
                    .position(ViewTooltip.Position.TOP)
                    .text("Campo de senha obrigatório")
                    .color(Color.RED)
                    .show();

            result = false;
        }
        if ("".equals(senhaDig) || senhaDig == null) {
            ViewTooltip
                    .on(this, edt_senha)
                    .autoHide(true, 3000)
                    .corner(30)
                    .position(ViewTooltip.Position.TOP)
                    .text("Campo de senha obrigatório")
                    .color(Color.RED)
                    .show();
            result = false;
        }


        return result;

    }



}
