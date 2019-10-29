package doc.on.call.Model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.time.LocalDateTime;
import java.util.List;

import doc.on.call.Room.AppointmentTypeConverters;

/**
 * Patient Model
 */
@Entity(tableName = "patient")
@TypeConverters(AppointmentTypeConverters.class)
public class Patient implements Parcelable {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    private String id;

    @ColumnInfo(name = "datetimeCreated")
    private LocalDateTime dateTimeCreated;

    @ColumnInfo(name = "email")
    private String email;

    @ColumnInfo(name = "password")
    private String password;

    @ColumnInfo(name = "age")
    private String age;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "nric")
    private String nric;

    @ColumnInfo(name = "appointmentList")
    private List<Appointment> appointmentList;

    @ColumnInfo(name = "age")
    private String age;
}
