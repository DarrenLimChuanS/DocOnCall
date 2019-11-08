package doc.on.call.Utilities;

import java.text.SimpleDateFormat;
import java.util.Date;

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
    public static final String USERNAME_REGEX = "^(?i)([a-z0-9]+){8,100}$";
    public static final String FULLNAME_REGEX = "^[a-zA-Z -]{2,50}$";
    public static final String EMAIL_REGEX = "^(?!.{255})(?i)[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";
    public static final String PHONE_REGEX = "^(?!.{255})(?i)[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";
    public static final String ADDRESS_REGEX = "^(?!.{255})(?i)[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";
    public static final String PASSWORD_REGEX = "^(?!.{255})(?i)[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";
    public static final String OTP_REGEX = "^(?!.{255})(?i)[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";
    public static final String NRIC_REGEX = "^(?!.{255})(?i)[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";

}