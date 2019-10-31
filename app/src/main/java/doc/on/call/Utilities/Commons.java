package doc.on.call.Utilities;

import android.content.Context;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static doc.on.call.Utilities.Constants.DT_DAY;
import static doc.on.call.Utilities.Constants.DT_MONTH;
import static doc.on.call.Utilities.Constants.DT_DAY_TIME;
import static doc.on.call.Utilities.Constants.DT_MONTH_YEAR;
import static doc.on.call.Utilities.Constants.DT_TIME;
import static doc.on.call.Utilities.Constants.DT_YEAR;

public class Commons {
    // Function to convert Date Time
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
                SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
                convertedDateTime = yearFormat.format(date);
                break;
            case DT_MONTH_YEAR:
                SimpleDateFormat monthYearFormat = new SimpleDateFormat("MMMM yyyy");
                convertedDateTime = monthYearFormat.format(date);
                break;
            case DT_MONTH:
                SimpleDateFormat monthFormat = new SimpleDateFormat("MMMM");
                convertedDateTime = monthFormat.format(date);
                break;
            case DT_DAY:
                SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE");
                convertedDateTime = dayFormat.format(date);
                break;
            case DT_DAY_TIME:
                SimpleDateFormat monthDayFormat = new SimpleDateFormat("d EEEE h:mm a");
                convertedDateTime = monthDayFormat.format(date);
                break;
            case DT_TIME:
                SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
                convertedDateTime = timeFormat.format(date);
                break;
            default:
                convertedDateTime = dateTime;
                break;
        }
        return convertedDateTime;
    }

    public static void showMessage(String message, Context context) {
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
        if( v != null) {
            v.setGravity(Gravity.CENTER);
        }
        toast.show();
    }
}
