package br.com.tads.estarfiscal.retrofit;


import br.com.tads.estarfiscal.model.Estar;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by DevMaker on 29/07/2016.
 */
public interface RequestInterfaceUser {
    @POST("getters/getEstarFiltro.php")
    @FormUrlEncoded
    Call<ResponseBody> getEstar(@Query("filter") String filter, @Field("latitude") Double latitude, @Field("longitude") Double longitude);

    @GET("setters/updStatus.php")
    Call<ResponseBody> retirar(@Query("id") String id);

    @GET("setters/push.php")
    Call<ResponseBody> notificar(@Query("UserId") String id);

    @POST("getters/getEstarByPlaca.php")
    @FormUrlEncoded
    Call<ResponseBody> getPlaca(@Field("Placa") String placa);

    @GET("teste/server")
    Call<ResponseBody> testeServer();

    @POST("data/put/{userid}")
    @FormUrlEncoded
    Call<ResponseBody> putLote(@Path("userid") String userid, @Field("data") String data);

    @GET("produtos/get")
    Call<ResponseBody> getProdutos();
}
