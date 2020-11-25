package com.example.terrasolecabaas.request;

import com.example.terrasolecabaas.modelo.Estadia;
import com.example.terrasolecabaas.modelo.Inquilino;
import com.example.terrasolecabaas.modelo.Pedido;
import com.example.terrasolecabaas.modelo.PedidoLinea;
import com.example.terrasolecabaas.modelo.Producto_Servicio;
import com.example.terrasolecabaas.modelo.Usuario;
import com.example.terrasolecabaas.ui.login.LoginActivity;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public class ApiClient {

    private static final String PATH = LoginActivity.RUTA + "api/";
    private static MyApiInterface myApiInterface;

    public static MyApiInterface getMyApiClient() {
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                .setLenient()
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(PATH)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        myApiInterface = retrofit.create(MyApiInterface.class);

        return myApiInterface;
    }

    public interface MyApiInterface {
        @FormUrlEncoded
        @POST("Usuarios/login")
        Call<String> Login(@Field("email") String email, @Field("contraseña") String contraseña);

        @GET("Usuarios/")
        Call<Usuario> getUsuario(@Header("Authorization") String token);

        @PUT("Usuarios/")
        Call<Usuario> putUsuario(@Header("Authorization") String token, @Body Usuario usuario);

        @PUT("Inquilinos/")
        Call<Inquilino> putInquilino(@Header("Authorization") String token, @Body Inquilino inquilino);

        @GET("Estadias/")
        Call<Estadia> getEstadia(@Header("Authorization") String token);

        @GET("Productos_Servicios/Productos")
        Call<ArrayList<Producto_Servicio>> getProductos(@Header("Authorization") String token);

        @GET("Productos_Servicios/Servicios")
        Call<ArrayList<Producto_Servicio>> getServicios(@Header("Authorization") String token);

        @POST("Pedidos/")
        Call<Pedido> postPedido(@Header("Authorization") String token, @Body Pedido pedido);

        @PUT("Pedidos/")
        Call<Pedido> putPedido(@Header("Authorization") String token, @Body Pedido pedido);

        @GET("Pedidos/Inquilino/")
        Call<ArrayList<Pedido>> getPedidosPorInquilino(@Header("Authorization") String token);

        @DELETE("Pedidos/{id}")
        Call<ResponseBody> deletePedido(@Header("Authorization") String token, @Path("id") int id);

        @GET("Pedidos/PedidosPendientes/")
        Call<ArrayList<Pedido>> getPedidosPendientes(@Header("Authorization") String token);
    }



}