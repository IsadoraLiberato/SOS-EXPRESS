package sosexpress.com.br.sosexpres.activitys;

import android.app.ProgressDialog;
import android.content.Intent;
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

import sosexpress.com.br.sosexpres.GeneratoRetrofit.RetrofitServiceGenerator;
import sosexpress.com.br.sosexpres.R;
import sosexpress.com.br.sosexpres.RestClient.RetrofitService;
import sosexpress.com.br.sosexpres.entidades.Usuario;
import sosexpress.com.br.sosexpres.utils.Constantes;
import sosexpress.com.br.sosexpres.utils.Preferences;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CdsUsuario extends AppCompatActivity {
    private EditText edt_nome_usu;
    private EditText edt_cpf_usu;
    private EditText edt_email_usu;
    private EditText edt_telefone_usu;
    private EditText edt_senha_usu;
    private EditText edt_senhaConfirm_usu;
    private Button btn_cad_usu;

    ProgressDialog progress;

    Preferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cds_usuario);

        Constantes.currentToken = FirebaseInstanceId.getInstance().getToken();

        edt_nome_usu = findViewById(R.id.edt_nome_usu);
        edt_cpf_usu = findViewById(R.id.edt_cpf_usu);
        edt_email_usu = findViewById(R.id.edt_email_usu);
        edt_telefone_usu = findViewById(R.id.edt_telefone_usu);
        edt_senha_usu = findViewById(R.id.edt_senha_usu);
        edt_senhaConfirm_usu = findViewById(R.id.edt_confirmS_usu);
        btn_cad_usu = findViewById(R.id.btn_cad_usu);

        /*Definir mascaras*/
        SimpleMaskFormatter simpleMaskCpf = new SimpleMaskFormatter("NNNNNNNNN-NN");
        MaskTextWatcher maskCpf = new MaskTextWatcher(edt_cpf_usu, simpleMaskCpf);
        edt_cpf_usu.addTextChangedListener( maskCpf );

        SimpleMaskFormatter simpleMaskTelefone = new SimpleMaskFormatter("(NN)NNNNN-NNNN");
        MaskTextWatcher maskTelefone = new MaskTextWatcher(edt_telefone_usu, simpleMaskTelefone);
        edt_telefone_usu.addTextChangedListener( maskTelefone );

    }

    public void cadastraUser(View view){
        String nome = edt_nome_usu.getText().toString();
        String cpf  = edt_cpf_usu.getText().toString();
        String email = edt_email_usu.getText().toString();
        String telefone = edt_telefone_usu.getText().toString();
        String senha = edt_senha_usu.getText().toString();
        String confS = edt_senhaConfirm_usu.getText().toString();

        Usuario user = new Usuario();
        user.setNome(nome);
        user.setCpf(cpf);
        user.setEmail(email);
        user.setToken(Constantes.currentToken);
        user.setTelefone(telefone);
        user.setSenha(senha);
        
        boolean validaCdsUsuario = validaCdsUsuario(view);

        if(validaCdsUsuario){
            cadastraCliente(user);

            //Chamando o progress
            progress = new ProgressDialog(CdsUsuario.this);
            progress.setTitle("Salavando dados..... ");
            progress.show();
        }

    }

    public void cadastraCliente(Usuario usuario) {

        RetrofitService service = RetrofitServiceGenerator.createService(RetrofitService.class);

        Call<Usuario> call = service.cadastrarCliente(usuario.getNome(),usuario.getEmail(), usuario.getTelefone(),usuario.getCpf(),usuario.getToken(),usuario.getSenha());

        call.enqueue(new Callback<Usuario>() {
            private Call<Usuario> call;
            private Response<Usuario> response;

            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                this.call = call;
                this.response = response;
                if (response.isSuccessful()) {

                    Usuario usuario = response.body();

                    Toast.makeText(CdsUsuario.this, "NomeCli: "+usuario.getNome(), Toast.LENGTH_SHORT).show();
                    //verifica aqui se o corpo da resposta não é nulo
                    if (usuario != null) {

                        preferences = new Preferences(CdsUsuario.this);
                        preferences.salvarDados(usuario.getEmail(),usuario.getSenha());
                        preferences.salvaIdCliente(usuario.getId());

                        Toast.makeText(CdsUsuario.this, "Email: "+preferences.getEmailCliente()+" Senha: "+preferences.getSenhaCliente(), Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(CdsUsuario.this, LoginCliente.class);
                        startActivity(intent);

                        //resOficina.setRua(oficina.getRua());

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
            public void onFailure(Call<Usuario> call, Throwable t) {
                Log.e("AppCep", "Não foi possível recuperar o Cep", t);
                progress.dismiss();
            }
        });

    }

    private boolean  validaCdsUsuario(View view ){
        boolean result = true;
        String nome = edt_nome_usu.getText().toString();
        String cpf = edt_cpf_usu.getText().toString();

        String email = edt_email_usu.getText().toString();
        String telefone= edt_telefone_usu.getText().toString();
        String senha = edt_senha_usu.getText().toString();
        String senhaConfirm = edt_senhaConfirm_usu.getText().toString();

        if("".equals(nome) || nome == null){
            edt_nome_usu.setError("Campo nome precisa ser preenchido");
             result = false;
        }

        if("".equals(cpf) || cpf == null){
            edt_cpf_usu.setError("Campo cpf precisa ser preenchido");
            result = false;
        }
        if("".equals(email) || email == null){
            edt_email_usu.setError("Campo email precisa ser preenchido");
            result = false;
        }

        if("".equals(telefone) || telefone == null){
            edt_telefone_usu.setError("Campo nome precisa ser preenchido");
            result = false;
        }

        if("".equals(senha) || senha == null){
            edt_senha_usu.setError("Campo senha é obrigatório");
            result = false;
        }

        if(!senha.equals(senhaConfirm)){
            //Toast.makeText(this, "Senhas não correspondem", Toast.LENGTH_SHORT).show();
            Snackbar.make(view, "Senhas digitadas não são iguais",Snackbar.LENGTH_LONG).show();
            result = false;
        }

        return result;

    }

}
