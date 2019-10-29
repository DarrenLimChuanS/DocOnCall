package doc.on.call.Room;

import android.arch.persistence.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

/**
 * GSON String to List of String Converter
 */
public class StringTypeConverters {

    @TypeConverter
    public static List<String> stringToStringList(String json) {
        Gson gson = new Gson();
        Type type = new TypeToken<List<String>>() {}.getType();
        List<String> stringList = gson.fromJson(json, type);
        return stringList;
    }

    @TypeConverter
    public static String stringListToString(List<String> list) {
        Gson gson = new Gson();
        Type type = new TypeToken<List<String>>() {}.getType();
        String json = gson.toJson(list, type);
        return json;
    }
}
