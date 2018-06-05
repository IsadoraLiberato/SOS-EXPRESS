package sosexpress.com.br.sosexpres.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class Preferences {
    private Context contexto;
    private SharedPreferences preferences;
    public final String NOME_ARQUIVO = "sos.preferences";
    public final int MODE = 0;
    private SharedPreferences.Editor editor;
    private boolean showIntro;

    public static final String EMAIL_CLIENTE = "email_cliente";
    public static final String SENHA_CLIENTE = "senha_cliente";
    public static final String ID_CLIENTE = "id_cliente";
    private static final int PARAM = 0;

    public static final String EMAIL_OFICINA = "email_oficina";
    public static final String SENHA_OFICINA = "senha_oficina";
    public static final String ID_OFICINA = "id_oficina";

    public static final String SHOW_INTRO = "show_intro";

    public Preferences( Context contextoParametro){

        contexto = contextoParametro;
        preferences = contexto.getSharedPreferences(NOME_ARQUIVO, MODE );
        editor = preferences.edit();

    }

    public void salvarDados(String email_cliente, String senha_cliente ){

        editor.putString(EMAIL_CLIENTE, email_cliente);
        editor.putString(SENHA_CLIENTE, senha_cliente);
        editor.commit();

    }
    public void salvarDadosOficina(String email_oficina, String senha_oficina ){

        editor.putString(EMAIL_OFICINA, email_oficina);
        editor.putString(SENHA_OFICINA, senha_oficina);
        editor.commit();

    }

    public void salvaIdCliente( int id ){

        editor.putInt(ID_CLIENTE, id);
        editor.commit();

    }
    public void salvaIdOficina( int id ){

        editor.putInt(ID_OFICINA, id);
        editor.commit();

    }

    public void setShowIntro(boolean visualisouIntro){
        this.showIntro = visualisouIntro;
        editor.putBoolean(SHOW_INTRO, showIntro);
        editor.commit();
    }

    public boolean isShowIntro(){
        return preferences.getBoolean(SHOW_INTRO,showIntro);
    }


    public String getEmailCliente(){
        return preferences.getString(EMAIL_CLIENTE, null);
    }

    public String getSenhaCliente(){
        return preferences.getString(SENHA_CLIENTE, null);
    }

    public int getIdCliente(){
        return preferences.getInt(ID_CLIENTE, PARAM);
    }


    public String getEmailOficina() {
        return preferences.getString(EMAIL_OFICINA, null);
    }

    public String getSenhaOficina() {
        return preferences.getString(SENHA_OFICINA, null);
    }

    public  int getIdOficina() {
        return preferences.getInt(ID_OFICINA, PARAM);
    }
}
