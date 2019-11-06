package doc.on.call.RetroFit.Request;

import android.content.Context;
import doc.on.call.Utilities.Constants;
import doc.on.call.Utilities.ObscuredSharedPreference;
import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.Interceptor.Chain;
import okhttp3.Request;
import okhttp3.Request.Builder;
import okhttp3.Response;

public class TokenInterceptor implements Interceptor {
    private Context context;
    private ObscuredSharedPreference mSharedPreference;

    public TokenInterceptor(Context context) {
        this.context = context;
        this.mSharedPreference = new ObscuredSharedPreference(context);
    }

    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Builder newBuilder = request.newBuilder();
        newBuilder.addHeader("Content-Type", Constants.API_CONTENT_TYPE);
        // Authorization is needed, check for token
        if (request.header(Constants.NO_AUTH_HEADER_KEY) == null) {
            newBuilder.addHeader("Authorization", "Bearer " + mSharedPreference.readJWTToken());
        }
        return chain.proceed(newBuilder.build());
    }
}