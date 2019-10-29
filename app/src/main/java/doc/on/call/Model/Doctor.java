package doc.on.call.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class Doctor {
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("pastAppointmentCollections")
    @Expose
    private List<PastAppointment> pastAppointments = null;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("upcomingAppointments")
    @Expose
    private List<Appointment> upcomingAppointments = null;
    @SerializedName("username")
    @Expose
    private String username;

    public String getAddress() {
        return this.address;
    }

    public String getEmail() {
        return this.email;
    }

    public String getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public List<PastAppointment> getPastAppointments() {
        return this.pastAppointments;
    }

    public String getPhone() {
        return this.phone;
    }

    public List<Appointment> getUpcomingAppointments() {
        return this.upcomingAppointments;
    }

    public String getUsername() {
        return this.username;
    }

    public void setAddress(String str) {
        this.address = str;
    }

    public void setEmail(String str) {
        this.email = str;
    }

    public void setId(String str) {
        this.id = str;
    }

    public void setName(String str) {
        this.name = str;
    }

    public void setPastAppointments(List<PastAppointment> list) {
        this.pastAppointments = list;
    }

    public void setPhone(String str) {
        this.phone = str;
    }

    public void setUpcomingAppointments(List<Appointment> list) {
        this.upcomingAppointments = list;
    }

    public void setUsername(String str) {
        this.username = str;
    }

    @Override
    public String toString() {
        return "Doctor{" +
                "address='" + address + '\'' +
                ", email='" + email + '\'' +
                ", id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", pastAppointments=" + pastAppointments +
                ", phone='" + phone + '\'' +
                ", upcomingAppointments=" + upcomingAppointments +
                ", username='" + username + '\'' +
                '}';
    }
}