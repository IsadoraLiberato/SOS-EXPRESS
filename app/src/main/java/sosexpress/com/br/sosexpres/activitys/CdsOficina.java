package sosexpress.com.br.sosexpres.activitys;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;
import com.google.firebase.iid.FirebaseInstanceId;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sosexpress.com.br.sosexpres.GeneratoRetrofit.RetrofitServiceGenerator;
import sosexpress.com.br.sosexpres.R;


import okhttp3.ResponseBody;
import sosexpress.com.br.sosexpres.RestClient.RetrofitService;
import sosexpress.com.br.sosexpres.entidades.Oficina;
import sosexpress.com.br.sosexpres.utils.BuscaCep;
import sosexpress.com.br.sosexpres.utils.CamposCdsOficina;
import sosexpress.com.br.sosexpres.utils.Constantes;


public class CdsOficina extends AppCompatActivity {

    private EditText edt_rua;
    private EditText edt_numero;
    private EditText edt_bairro;
    private EditText edt_cep;
    private EditText edt_nomeFantasia;
    private EditText edt_telefone;
    private EditText edt_cnpj;
    private EditText edt_email;
    private EditText edt_senha;
    private EditText edt_confirmS_ofic;
    private Button btn_bcr_cep;

    Oficina resOficina = new Oficina();
    Button cadOficina;
    ProgressDialog progress;

    CamposCdsOficina camposCdsOficina;
    private String cidade = "";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cds_oficina);

        Constantes.currentToken = FirebaseInstanceId.getInstance().getToken();
        cadOficina = findViewById(R.id.btn_cad_oficina);

        //Pegar as views
        edt_rua = findViewById(R.id.edt_rua_ofic);
        edt_numero = findViewById(R.id.edt_numero_ofic);
        edt_bairro = findViewById(R.id.edt_bairro_ofic);
        edt_cep = findViewById(R.id.edt_cep_ofic);
        edt_nomeFantasia = findViewById(R.id.edt_nomeF_ofic);
        edt_telefone = findViewById(R.id.edt_tel_ofic);
        edt_cnpj = findViewById(R.id.edt_cnpj_ofic);
        edt_email = findViewById(R.id.edt_email_ofic);
        edt_senha = findViewById(R.id.edt_senha_ofic);
        edt_confirmS_ofic = findViewById(R.id.edt_confirmS_ofic);
        btn_bcr_cep = findViewById(R.id.btn_bcr_cep);


        /*Setando mascaras*/
        //cep
        SimpleMaskFormatter simpleMaskCep = new SimpleMaskFormatter("NNNNN-NNN");
        MaskTextWatcher maskCpf = new MaskTextWatcher(edt_cep, simpleMaskCep);
        edt_cep.addTextChangedListener(maskCpf);

        //telefone
        SimpleMaskFormatter simpleMaskTelefone = new SimpleMaskFormatter("(NN)NNNNN-NNNN");
        MaskTextWatcher maskTelefone = new MaskTextWatcher(edt_telefone, simpleMaskTelefone);
        edt_telefone.addTextChangedListener(maskTelefone);
        //cnpj
        SimpleMaskFormatter simpleMaskCnpj = new SimpleMaskFormatter("NN.NNN.NNN/NNNN-NN");
        MaskTextWatcher maskCnpj = new MaskTextWatcher(edt_cnpj, simpleMaskCnpj);
        edt_cnpj.addTextChangedListener(maskCnpj);


        cadOficina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                camposCdsOficina = new CamposCdsOficina();
                camposCdsOficina.setEdt_rua(edt_rua);
                camposCdsOficina.setEdt_numero(edt_numero);
                camposCdsOficina.setEdt_bairro(edt_bairro);
                camposCdsOficina.setEdt_cep(edt_cep);
                camposCdsOficina.setEdt_nomeFantasia(edt_nomeFantasia);
                camposCdsOficina.setEdt_telefone(edt_telefone);
                camposCdsOficina.setEdt_cnpj(edt_cnpj);
                camposCdsOficina.setEdt_email(edt_email);
                camposCdsOficina.setEdt_senha(edt_senha);
                camposCdsOficina.setEdt_confirmS_ofic(edt_confirmS_ofic);


                Oficina ofic = new Oficina();
                ofic.setRua(camposCdsOficina.getEdt_rua().getText().toString());
                ofic.setNumero(camposCdsOficina.getEdt_numero().getText().toString());
                ofic.setBairro(camposCdsOficina.getEdt_bairro().getText().toString());
                ofic.setCep(camposCdsOficina.getEdt_cep().getText().toString());
                ofic.setNome(camposCdsOficina.getEdt_nomeFantasia().getText().toString());
                ofic.setTelefone(camposCdsOficina.getEdt_telefone().getText().toString());
                ofic.setCnpj(camposCdsOficina.getEdt_cnpj().getText().toString());
                ofic.setEmail(camposCdsOficina.getEdt_email().getText().toString());
                ofic.setToken(Constantes.currentToken);
                ofic.setSenha(camposCdsOficina.getEdt_senha().getText().toString());




                if(validaCampos(camposCdsOficina, view)){
                    //verifica se a cidade do usuário é Natal-RN
                    if(!cidade.equals("Natal")){
                        showAlert("Atenção", "Infelizmente esse app ainda não está disponível para sua cidade ("+cidade+")", false);

                    }
                    Toast.makeText(CdsOficina.this, "Campos todos preenchidos", Toast.LENGTH_SHORT).show();
                    //Chamada do metodo do retrofit
                    retrofitConverter(ofic);
                    //Chamando o progress
                    progress = new ProgressDialog(CdsOficina.this);
                    progress.setTitle("Salavando dados..... ");
                    progress.show();
                }
            }
        });

        btn_bcr_cep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DownloadCEPTask().execute(edt_cep.getText().toString());
            }
        });

    }

    public void retrofitConverter(Oficina oficina) {

        RetrofitService service = RetrofitServiceGenerator.createService(RetrofitService.class);
        int num = Integer.valueOf(oficina.getNumero());

        Call<Oficina> call = service.cadastarOficinas(oficina.getRua(), num, oficina.getBairro(), oficina.getCep(),
                oficina.getNome(), oficina.getTelefone(), oficina.getCnpj(), oficina.getEmail(),oficina.getToken(), oficina.getSenha());

        call.enqueue(new Callback<Oficina>() {
            @Override
            public void onResponse(Call<Oficina> call, Response<Oficina> response) {
                if (response.isSuccessful()) {

                    Oficina oficina = response.body();
                    // Log.i("AppOficina", "Response = "+response);
                    //Log.i("AppOficina", "Body = "+response.body());

                    String rua = oficina.getBairro();
                    Log.i("AppOficina", "Body = " + rua);
                    //verifica aqui se o corpo da resposta não é nulo
                    if (oficina != null) {

                        progress.dismiss();


                    } else {
                        progress.dismiss();
                        Toast.makeText(getApplicationContext(), "Resposta nula do servidor", Toast.LENGTH_SHORT).show();
                    }

                } else {

                    Toast.makeText(getApplicationContext(), "Resposta não foi sucesso", Toast.LENGTH_SHORT).show();
                    // segura os erros de requisição
                    ResponseBody errorBody = response.errorBody();
                    progress.dismiss();
                }

            }

            @Override
            public void onFailure(Call<Oficina> call, Throwable t) {
                Log.e("AppCep", "Não foi possível recuperar o Cep", t);
                progress.dismiss();
            }
        });

    }


    private boolean validaCampos(CamposCdsOficina camposCdsOficina, View view) {
        boolean resultado = true;

        String rua = camposCdsOficina.getEdt_rua().getText().toString();
        String numero = camposCdsOficina.getEdt_numero().getText().toString();
        String bairro = camposCdsOficina.getEdt_bairro().getText().toString();
        String cep = camposCdsOficina.getEdt_cep().getText().toString();
        String nome = camposCdsOficina.getEdt_nomeFantasia().getText().toString();
        String telefone = camposCdsOficina.getEdt_telefone().getText().toString();
        String cnpj = camposCdsOficina.getEdt_cnpj().getText().toString();
        String email = camposCdsOficina.getEdt_email().getText().toString();
        String senha = camposCdsOficina.getEdt_senha().getText().toString();
        String senhaConfirm = camposCdsOficina.getEdt_confirmS_ofic().getText().toString();

        if ("".equals(rua) || rua == null) {
            camposCdsOficina.getEdt_rua().setError("Preencha o campo rua");
            resultado = false;
        }

        if ("".equals(numero) || numero == null) {
            camposCdsOficina.getEdt_numero().setError("Preencha o campo numero");
            resultado = false;
        }

        if ("".equals(bairro) || bairro == null) {
            camposCdsOficina.getEdt_bairro().setError("Preencha o campo bairro");
            resultado = false;
        }
        if ("".equals(cep) || cep == null) {
            camposCdsOficina.getEdt_cep().setError("Preencha o campo cep");
            resultado = false;
        }
        if ("".equals(nome) || nome == null) {
            camposCdsOficina.getEdt_nomeFantasia().setError("Preencha o campo nome");
            resultado = false;
        }
        if ("".equals(telefone) || telefone == null) {
            camposCdsOficina.getEdt_telefone().setError("Preencha o campo telefone");
            resultado = false;
        }
        if ("".equals(cnpj) || cnpj == null) {
            camposCdsOficina.getEdt_cnpj().setError("Preencha o campo cnpj");
            resultado = false;
        }
        if ("".equals(email) || email == null) {
            camposCdsOficina.getEdt_email().setError("Preencha o campo email");
            resultado = false;
        }
        if ("".equals(senha) || senha == null) {
            camposCdsOficina.getEdt_senha().setError("Preencha o campo senha");
            resultado = false;
        }

        if ("".equals(senhaConfirm) || senhaConfirm == null) {
            camposCdsOficina.getEdt_confirmS_ofic().setError("Preencha o campo senhaConfirm");
            resultado = false;
        }

        if (!senha.equals(senhaConfirm)) {
            Snackbar.make(view, "As senhas digitadas não são iguais", Snackbar.LENGTH_LONG).show();
            resultado = false;
        }

        if (cep.length() < 9) {
            camposCdsOficina.getEdt_cep().setError("O campo cep deve conter 8 dígitos");
            resultado = false;
        }
        if (telefone.length() < 14) {
            camposCdsOficina.getEdt_telefone().setError("O campo telefone deve conter 11 dígitos");
            resultado = false;
        }
        if (cnpj.length() < 18) {
            camposCdsOficina.getEdt_cnpj().setError("O campo cnpj deve conter 14 dígitos");
            resultado = false;
        }

        return resultado;
    }

    private class DownloadCEPTask extends AsyncTask<String, Void, BuscaCep> {
        @Override
        protected BuscaCep doInBackground(String... ceps) {
            BuscaCep vCep = null;

            try {
                vCep = new BuscaCep(ceps[0]);
            } finally {
                return vCep;
            }
        }

        @Override
        protected void onPostExecute(BuscaCep result) {

            if (result != null) {
                cidade = result.getLocalidade();
                //verifica se a cidade do usuário é Natal-RN
                if(!cidade.equals("Natal")){
                    showAlert("Atenção", "Infelizmente esse app ainda não está disponível para sua cidade ("+cidade+")", false);

                }else{
                    //seta texto nos campos do cadastro
                    edt_rua.setText(result.getLogradouro());
                    edt_bairro.setText(result.getBairro());

                    Toast.makeText(CdsOficina.this, cidade, Toast.LENGTH_SHORT).show();
                }


            }
        }


    }

    private void showAlert(String title, String msg, boolean cancelable){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(msg);
        builder.setCancelable(cancelable);
        builder.setNeutralButton("Fechar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(CdsOficina.this, ActivityAcaoUsuario.class);
                startActivity(intent);
                finish();
            }
        });
        builder.create()
                .show();
    }
}

