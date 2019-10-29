package doc.on.call.Room;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import doc.on.call.Model.Patient;

/**
 * Room database for Patient Model
 */
@Database(entities = {Patient.class}, version = 1)
@TypeConverters({AppointmentTypeConverters.class, StringTypeConverters.class})
public abstract class PatientDatabase extends RoomDatabase {

    public static final String DATABASE_NAME = "patient_db";

    private static PatientDatabase instance;

    public static PatientDatabase getInstance(final Context context){
        if(instance == null){
            instance = Room.databaseBuilder(
                    context.getApplicationContext(),
                    PatientDatabase.class,
                    DATABASE_NAME
            ).build();
        }
        return instance;
    }

    public abstract PatientDao getPatientDao();

}