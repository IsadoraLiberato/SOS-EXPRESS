package sosexpress.com.br.sosexpres.RestClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import sosexpress.com.br.sosexpres.entidades.MensagemLogin;
import sosexpress.com.br.sosexpres.entidades.Oficina;
import sosexpress.com.br.sosexpres.entidades.Registro;
import sosexpress.com.br.sosexpres.entidades.RetornoIdOficina;
import sosexpress.com.br.sosexpres.entidades.Usuario;

/**
 * Created by handerson on 23/04/18.
 */

public interface RetrofitService {
    //headers é caso de api autenticada
    //@Headers("X-Mashape-Key: AuuyclCPjcmshv2iOPq190OpzLrMp1FJWwejsnJrdfwOUr4h44")

    @FormUrlEncoded
    @POST("oficinas")
    Call<Oficina> cadastarOficinas(@Field("rua") String rua,
                                   @Field("numero") int numero,
                                   @Field("bairro") String bairro,
                                   @Field("cep") String cep,
                                   @Field("nome") String nome,
                                   @Field("cnpj") String cnpj,
                                   @Field("telefone") String telefone,
                                   @Field("email") String email,
                                   @Field("token") String token,
                                   @Field("senha") String senha);
    @GET("oficina")
    Call<List<Oficina>> getOficinas();

    @GET("oficinas/{email}")
    Call<MensagemLogin> getOficinaEmail(@Path("email") String email);

    @FormUrlEncoded
    @POST("clientes")
    Call<Usuario> cadastrarCliente(@Field("nome") String nome,
                                   @Field("email") String email,
                                   @Field("telefone") String telefone,
                                   @Field("cpf") String cpf,
                                   @Field("token") String token,
                                   @Field("senha") String senha);

    @GET("oficinaid/{nome}")
    Call<RetornoIdOficina> getOficina(@Path("nome") String nome);

    @GET("clientes/{email}")
    Call<MensagemLogin> getCliente(@Path("email") String email);

    @FormUrlEncoded
    @POST("registros")
    Call<Registro> cadastrarRegistro(@Field("placa") String placa,
                                     @Field("modelo") String modelo,
                                     @Field("descProblema") String descProblema,
                                     @Field("fk_id_cliente") int fk_id_cliente,
                                     @Field("fk_id_oficina") int fk_id_oficina,
                                     @Field("latUser") String latUser,
                                     @Field("logUser") String logUser);

    @GET("registros/{id}")
    Call<List<Registro>> getListaRegistro(@Path("id") int id);

}
