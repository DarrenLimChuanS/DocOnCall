package doc.on.call.Utilities;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import doc.on.call.R;

import static doc.on.call.Utilities.Constants.ADDRESS_REGEX;
import static doc.on.call.Utilities.Constants.DT_DAY;
import static doc.on.call.Utilities.Constants.DT_DAY_MONTH;
import static doc.on.call.Utilities.Constants.DT_MONTH;
import static doc.on.call.Utilities.Constants.DT_DAY_TIME;
import static doc.on.call.Utilities.Constants.DT_MONTH_YEAR;
import static doc.on.call.Utilities.Constants.DT_TIME;
import static doc.on.call.Utilities.Constants.DT_YEAR;
import static doc.on.call.Utilities.Constants.EMAIL_REGEX;
import static doc.on.call.Utilities.Constants.FULLNAME_REGEX;
import static doc.on.call.Utilities.Constants.ISSUE_REGEX;
import static doc.on.call.Utilities.Constants.NRIC_REGEX;
import static doc.on.call.Utilities.Constants.OTP_REGEX;
import static doc.on.call.Utilities.Constants.PASSWORD_REGEX;
import static doc.on.call.Utilities.Constants.PHONE_REGEX;
import static doc.on.call.Utilities.Constants.TOKEN_REGEX;
import static doc.on.call.Utilities.Constants.USERNAME_REGEX;

public class Commons {
    private static final String TAG = "Commons";

    /**
     * Function to convert String Date Time to various different type
     * @param dateTime
     * @param returnType
     * @return
     */
    public static String convertDateTime(String dateTime, String returnType) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd kk:m:s");
        Date date = null;
        try {
            date = sdf.parse(dateTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String convertedDateTime = "";
        switch (returnType) {
            case DT_YEAR:
                // 2019
                SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
                convertedDateTime = yearFormat.format(date);
                break;
            case DT_MONTH_YEAR:
                // November 2019
                SimpleDateFormat monthYearFormat = new SimpleDateFormat("MMMM yyyy");
                convertedDateTime = monthYearFormat.format(date);
                break;
            case DT_MONTH:
                // November
                SimpleDateFormat monthFormat = new SimpleDateFormat("MMMM");
                convertedDateTime = monthFormat.format(date);
                break;
            case DT_DAY_MONTH:
                // 3 Nov
                SimpleDateFormat dayMonthFormat = new SimpleDateFormat("d MMM");
                convertedDateTime = dayMonthFormat.format(date);
                break;
            case DT_DAY:
                // Sunday
                SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE");
                convertedDateTime = dayFormat.format(date);
                break;
            case DT_DAY_TIME:
                // Sunday 3:19PM
                SimpleDateFormat dayTimeFormat = new SimpleDateFormat("EEEE\nh:mm a");
                convertedDateTime = dayTimeFormat.format(date);
                break;
            case DT_TIME:
                // 3:19PM
                SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
                convertedDateTime = timeFormat.format(date);
                break;
            default:
                convertedDateTime = dateTime;
                break;
        }
        return convertedDateTime;
    }

    /**
     * Function to display Toast message in the center of the screen
     * @param message
     * @param context
     */
    public static void showMessage(String message, Context context) {
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
        if( v != null) {
            v.setGravity(Gravity.CENTER);
        }
        toast.show();
    }

    /**
     * ================================= START OF VALIDATIONS =================================
     */
    public static boolean isEmailValid(String email) {
        return email.matches(EMAIL_REGEX) ? true : false;
    }

    public static boolean isUsernameValid(String username) {
        return username.matches(USERNAME_REGEX);
    }

    public static boolean isPasswordValid(String password) {
        return password.matches(PASSWORD_REGEX);
    }

    public static boolean isPasswordMatchValid(String firstPassword, String secondPassword) {
        return firstPassword.equals(secondPassword);
    }

    public static boolean isOtpValid(String otp) {
        return otp.matches(OTP_REGEX);
    }


    public static boolean isFullNameValid(String fullName) {
        return fullName.matches(FULLNAME_REGEX) ? true : false;
    }

    public static boolean isNRICValid(String nric) {
        return nric.matches(NRIC_REGEX) ? true : false;
    }

    public static boolean isAgeValid(String birthYear) {
        try {
            int intbirthYear = Integer.parseInt(birthYear);
            // Fetching current year
            int year = Calendar.getInstance().get(Calendar.YEAR);
            Log.d(TAG, Integer.toString(intbirthYear) + " VS " + Integer.toString(year));
            // User is below 125 years old
            if (year - intbirthYear <= 125){
                return true;
            } else {
                return false;
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean isPhoneValid(String phone) {
        if(phone.matches(PHONE_REGEX)) {
            try {
                Integer.parseInt(phone);
                return true;
            } catch (NumberFormatException e) {
                e.printStackTrace();
                return false;
            }
        } else {
            return false;
        }
    }

    public static boolean isAddressValid(String address) {
        return address.matches(ADDRESS_REGEX) ? true : false;
    }

    public static boolean isTokenValid(String token) {
        Log.d(TAG, token);
        Log.d(TAG, "=====================================");
        Log.d(TAG, TOKEN_REGEX);
        return token.matches(TOKEN_REGEX) ? true : false;
    }

    public static boolean isIssueValid(String issue) {
        return issue.matches(ISSUE_REGEX) ? true : false;
    }

    public static boolean isDateValid(String date) {
        if (!date.equals("Date is not selected")) {
            try {
                // Convert date to Java Date to check
                Date dateToTest = new SimpleDateFormat("yyyy-MM-dd").parse(date);
                Calendar dateToTestCal = Calendar.getInstance();
                dateToTestCal.setTime(dateToTest);
                if ((dateToTestCal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) || (dateToTestCal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)) {
                    return false;
                } else {
                    return true;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return false;
        } else {
            return false;
        }
    }

    public static boolean isTimeValid(String time) {
        if (!time.equals("Time is not selected")){
            try {
                // Convert time to Java Date to check
                Date timeToTest = new SimpleDateFormat("kk:m").parse(time);
                Calendar timeToTestCal = Calendar.getInstance();
                timeToTestCal.setTime(timeToTest);
                timeToTestCal.add(Calendar.DATE, 1);

                // Start time
                String startTimeString = "09:00";
                Date startTime = new SimpleDateFormat("kk:m").parse(startTimeString);
                Calendar startTimeCal = Calendar.getInstance();
                startTimeCal.setTime(startTime);
                startTimeCal.add(Calendar.DATE, 1);

                // End time
                String endTimeString = "16:00";
                Date endTime = new SimpleDateFormat("kk:m").parse(endTimeString);
                Calendar endTimeCal = Calendar.getInstance();
                endTimeCal.setTime(endTime);
                endTimeCal.add(Calendar.DATE, 1);

                // Check if time to check is between start and end time
                Date x = timeToTestCal.getTime();
                return (x.after(startTimeCal.getTime()) && x.before(endTimeCal.getTime()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return false;
        } else {
            return false;
        }
    }
    /**
     * ================================= END OF VALIDATIONS =================================
     */
}
