package sosexpress.com.br.sosexpres.entidades;

public class Registro {

    private int id;
    private String placa;
    private String modelo;
    private String descProblema;
    private int fk_id_cliente;
    private int fk_id_oficina;
    private String latUser;
    private String logUser;
    private String created_at;
    private String updated_at;
    private String nome;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getDescProblema() {
        return descProblema;
    }

    public void setDescProblema(String descProblema) {
        this.descProblema = descProblema;
    }

    public int getFk_id_oficina() {
        return fk_id_oficina;
    }

    public void setFk_id_oficina(int fk_id_oficina) {
        this.fk_id_oficina = fk_id_oficina;
    }

    public int getFk_id_cliente() {
        return fk_id_cliente;
    }

    public void setFk_id_cliente(int fk_id_cliente) {
        this.fk_id_cliente = fk_id_cliente;
    }

    public String getLatUser() {
        return latUser;
    }

    public void setLatUser(String latUser) {
        this.latUser = latUser;
    }

    public String getLogUser() {
        return logUser;
    }

    public void setLongUser(String longUser) {
        this.logUser = longUser;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public void setLogUser(String logUser) {
        this.logUser = logUser;
    }


    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
