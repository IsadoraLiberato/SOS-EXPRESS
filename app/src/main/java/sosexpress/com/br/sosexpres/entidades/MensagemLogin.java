package sosexpress.com.br.sosexpres.entidades;

public class MensagemLogin {

    private String msg;
    private int id;
    private String email;
    private String senha;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) { this.msg = msg; }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}
