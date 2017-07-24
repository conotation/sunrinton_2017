package cf.connota.sunrinton.Util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Conota on 2017-07-25.
 */

public class NeyoAPI {

    public static final String SERVER_URL = "http://sunrinton.connotation.cf";
    public static EstickerServer server;

    public NeyoAPI() {
        getServer();
    }

    public static void getServer(){
        Gson gson = new GsonBuilder().create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SERVER_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        server = retrofit.create(EstickerServer.class);
    }

    public interface EstickerServer {
        @GET("/auth/facebook/token")
        Call<ResponseBody> userinit(@Query("access_token") String access_token);



    }
}
