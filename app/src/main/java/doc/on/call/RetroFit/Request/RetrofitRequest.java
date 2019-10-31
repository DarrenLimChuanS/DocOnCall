package doc.on.call.RetroFit.Request;

import android.content.Context;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import okhttp3.OkHttpClient.Builder;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitRequest {
    // Fetch from NDK
    static {
        System.loadLibrary("native-lib");
    }
    public static native String getAPIBaseURL();

    // Variables
    private static Retrofit retrofit;
    public static OkHttpClient client;
    public static Gson gson = new GsonBuilder().serializeNulls().create();

    public static Retrofit getRetrofitInstance(Context context) {
        if (retrofit == null) {
            client = new Builder().addNetworkInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                    .addInterceptor(new TokenInterceptor(context))
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(2, TimeUnit.SECONDS)
                    .writeTimeout(2, TimeUnit.SECONDS)
                    .retryOnConnectionFailure(true)
                    .build();
            retrofit = new Retrofit.Builder().baseUrl(getAPIBaseURL())
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit;
    }
}