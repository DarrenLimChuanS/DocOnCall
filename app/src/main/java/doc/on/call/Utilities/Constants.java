package doc.on.call.Utilities;

public class Constants {
    // HTTP Responses
    public static final int HTTP_BAD = 400;
    public static final int HTTP_OK = 200;
    public static final int HTTP_SERVER_ERROR = 500;
    public static final int HTTP_UNAUTHORIZED = 401;

    // OkHttp
    public static final String NO_AUTH_HEADER_KEY = "No-Authentication";
    public static final String API_CONTENT_TYPE = "application/json";
    public static final int READ_TIMEOUT = 2;
    public static final int WRITE_TIMEOUT = 2;
    public static final int CONNECTION_TIMEOUT = 10;

    // Obscured Shared Preference
    public static final String PREF_NONCE = "SP_Nonce";
    public static final String PREF_TOKEN = "SP_Token";

    // Regex
    public static final String EMAILREGEX = "^[a-zA-Z0-9.!#$%&’*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$";
}