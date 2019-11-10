package doc.on.call.Utilities;

public class Constants {
    // HTTP Responses
    public static final int HTTP_OK = 200;
    public static final int HTTP_UNAUTHORIZED = 401;

    // OkHttp
    public static final String NO_AUTH_HEADER_KEY = "No-Authentication";
    public static final String API_CONTENT_TYPE = "application/json";
    public static final int READ_TIMEOUT = 5;
    public static final int WRITE_TIMEOUT = 5;
    public static final int CONNECTION_TIMEOUT = 10;
    public static final int SPLASH_TIME_OUT = 3000;

    // Obscured Shared Preference
    public static final String PREF_NONCE = "SP_Nonce";
    public static final String PREF_TOKEN = "SP_Token";
    public static final String PREF_RESEND = "SP_Resend";
    public static final String PREF_EMAIL = "SP_Email";

    // Date Time
    public static final String DT_YEAR = "Year";
    public static final String DT_MONTH = "Month";
    public static final String DT_MONTH_YEAR = "Month Year";
    public static final String DT_DAY_MONTH = "Day Month";
    public static final String DT_DAY = "Day";
    public static final String DT_DAY_TIME = "Day Time";
    public static final String DT_TIME = "Time";

    // Regex
    public static final String USERNAME_REGEX = "^[a-z0-9]{8,100}$";
    public static final String FULLNAME_REGEX = "^[a-zA-Z -]{2,50}$";
    public static final String EMAIL_REGEX = "^(?!.{255})(?i)[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";
    public static final String PHONE_REGEX = "^[6|8|9]\\d{7}$";
    public static final String ADDRESS_REGEX = "^[^\\s]+[a-zA-Z0-9\\s#,-]{3,100}$";
    public static final String PASSWORD_REGEX = "^[A-Za-z0-9!@#$%^&*()]{8,100}$";
    public static final String OTP_REGEX = "^[0-9]{6}$";
    public static final String NRIC_REGEX = "^(?i)[STFG][6-9,0][0-9]{6}[A-Z]$";
    public static final String TOKEN_REGEX = "^(?:[a-zA-Z0-9+\\/]{4})*(?:|(?:[a-zA-Z0-9+\\/]{3}=)|(?:[a-zA-Z0-9+\\/]{2}==)|(?:[a-zA-Z0-9+\\/]{1}===))$";
    public static final String ISSUE_REGEX = "^[A-Za-z]{1,255}$";
}