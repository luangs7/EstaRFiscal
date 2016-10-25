package br.com.tads.estarfiscal.retrofit;

import android.content.Context;
import android.location.Location;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;


import br.com.tads.estarfiscal.model.Estar;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by DevMaker on 9/28/16.
 */
public class ApiManager {
    OkHttpClient okHttpClient;
    Retrofit retrofit;
    public static String  endpoint = "http://wsestarapp.esy.es/";
    Context context;

    public ApiManager(Context context){
        this.context = context;
        okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(5000, TimeUnit.MILLISECONDS)
                .readTimeout(3000, TimeUnit.MILLISECONDS)
                .writeTimeout(3000, TimeUnit.MILLISECONDS)
                .build();

        Gson gson = new GsonBuilder()
                .create();
//      SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
//      String endpoint =  sharedPreferences.getString("endpoint","");
        retrofit = new Retrofit.Builder()
                .baseUrl(endpoint)
                .client(okHttpClient)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }

    public ApiManager(Context context,Retrofit retrofit, OkHttpClient okHttpClient) {
        this.context = context;
        this.retrofit = retrofit;
        this.okHttpClient = okHttpClient;
    }

    public void getEstar(Callback<ResponseBody> callback,String filter, Location location){
        RequestInterfaceUser request = retrofit.create(RequestInterfaceUser.class);
        Call<ResponseBody> call = request.getEstar(filter,location.getLatitude(),location.getLongitude());
        call.enqueue(callback);
    }

    public void retirar(Callback<ResponseBody> callback,Estar estar){
        RequestInterfaceUser request = retrofit.create(RequestInterfaceUser.class);
        Call<ResponseBody> call = request.retirar(estar.getIdEstar());
        call.enqueue(callback);
    }

    public void notificar(Callback<ResponseBody> callback,Estar estar){
        RequestInterfaceUser request = retrofit.create(RequestInterfaceUser.class);
        Call<ResponseBody> call = request.notificar(estar.getUsuarioId());
        call.enqueue(callback);
    }

    public void getplaca(Callback<ResponseBody> callback,String placa){
        RequestInterfaceUser request = retrofit.create(RequestInterfaceUser.class);
        Call<ResponseBody> call = request.getPlaca(placa);
        call.enqueue(callback);
    }


    public void testeServer(Callback<ResponseBody> callback){
        RequestInterfaceUser request = retrofit.create(RequestInterfaceUser.class);
        Call<ResponseBody> call = request.testeServer();
        call.enqueue(callback);
    }

    public void putLote(Callback<ResponseBody> callback,String userId,String data){
        RequestInterfaceUser request = retrofit.create(RequestInterfaceUser.class);
        Call<ResponseBody> call = request.putLote(userId,data);
        call.enqueue(callback);
    }

    public void getProdutos(Callback<ResponseBody> callback){
        RequestInterfaceUser request = retrofit.create(RequestInterfaceUser.class);
        Call<ResponseBody> call = request.getProdutos();
        call.enqueue(callback);
    }
}
