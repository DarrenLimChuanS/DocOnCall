package doc.on.call.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PastAppointment {
    @SerializedName("appointmentDateTime")
    @Expose
    private String appointmentDateTime;
    @SerializedName("category")
    @Expose
    private String category;
    @SerializedName("datetimeCreated")
    @Expose
    private String datetimeCreated;
    @SerializedName("id")
    @Expose
    private String id;

    public String getAppointmentDateTime() {
        return this.appointmentDateTime;
    }

    public String getCategory() {
        return this.category;
    }

    public String getDatetimeCreated() {
        return this.datetimeCreated;
    }

    public String getId() {
        return this.id;
    }

    public void setAppointmentDateTime(String str) {
        this.appointmentDateTime = str;
    }

    public void setCategory(String str) {
        this.category = str;
    }

    public void setDatetimeCreated(String str) {
        this.datetimeCreated = str;
    }

    public void setId(String str) {
        this.id = str;
    }

    @Override
    public String toString() {
        return "PastAppointment{" +
                "appointmentDateTime='" + appointmentDateTime + '\'' +
                ", category='" + category + '\'' +
                ", datetimeCreated='" + datetimeCreated + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}