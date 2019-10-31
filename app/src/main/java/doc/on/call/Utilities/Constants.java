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
    public static final int READ_TIMEOUT = 5;
    public static final int WRITE_TIMEOUT = 5;
    public static final int CONNECTION_TIMEOUT = 10;
    public static int SPLASH_TIME_OUT = 3000;

    // Obscured Shared Preference
    public static final String PREF_NONCE = "SP_Nonce";
    public static final String PREF_TOKEN = "SP_Token";

    // Date Time
    public static final String DT_YEAR = "Year";
    public static final String DT_MONTH = "Month";
    public static final String DT_MONTH_YEAR = "Month Year";
    public static final String DT_DAY = "Day";
    public static final String DT_DAY_TIME = "Day Time";
    public static final String DT_TIME = "Time";

    // Regex
    public static final String EMAILREGEX = "^[a-zA-Z0-9.!#$%&’*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$";
}