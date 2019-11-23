package doc.on.call.RetroFit.Request;

import android.content.Context;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import okhttp3.OkHttpClient.Builder;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static doc.on.call.Utilities.Constants.CONNECTION_TIMEOUT;
import static doc.on.call.Utilities.Constants.READ_TIMEOUT;
import static doc.on.call.Utilities.Constants.WRITE_TIMEOUT;

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
            client = new Builder().addInterceptor(new TokenInterceptor(context))
                    .followRedirects(false)
                    .connectTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS)
                    .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                    .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
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