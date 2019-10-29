package doc.on.call.Room;

import android.arch.persistence.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import doc.on.call.Model.Appointment;

/**
 * GSON String to List of Appointment Converter
 */
public class AppointmentTypeConverters {

    @TypeConverter
    public static List<Appointment> stringToAppointmentList(String json) {
        Gson gson = new Gson();
        Type type = new TypeToken<List<Appointment>>() {}.getType();
        List<Appointment> appointmentList = gson.fromJson(json, type);
        return appointmentList;
    }

    @TypeConverter
    public static String appointmentListToString(List<Appointment> list) {
        Gson gson = new Gson();
        Type type = new TypeToken<List<Appointment>>() {}.getType();
        String json = gson.toJson(list, type);
        return json;
    }
}
