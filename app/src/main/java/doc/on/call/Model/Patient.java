package doc.on.call.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Patient {
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("age")
    @Expose
    private int age;
    @SerializedName("appointments")
    @Expose
    private List<Appointment> appointments;
    @SerializedName("conditions")
    @Expose
    private List<String> condition;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("nric")
    @Expose
    private String nric;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("username")
    @Expose
    private String username;

    public Patient() {
        this.appointments = new ArrayList<Appointment>();
    }

    public String getAddress() {
        return this.address;
    }

    public int getAge() {
        return this.age;
    }

    public List<Appointment> getAppointments() {
        return this.appointments;
    }

    public List<String> getCondition() {
        return this.condition;
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

    public String getNric() {
        return this.nric;
    }

    public String getPhone() {
        return this.phone;
    }

    public String getUsername() {
        return this.username;
    }

    public void setAddress(String str) {
        this.address = str;
    }

    public void setAge(int i) {
        this.age = i;
    }

    public void setAppointments(List<Appointment> list) {
        this.appointments = list;
    }

    public void setCondition(List<String> list) {
        this.condition = list;
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

    public void setNric(String str) {
        this.nric = str;
    }

    public void setPhone(String str) {
        this.phone = str;
    }

    public void setUsername(String str) {
        this.username = str;
    }

    @Override
    public String toString() {
        return "Patient{" +
                "address='" + address + '\'' +
                ", age=" + age +
                ", appointments=" + appointments +
                ", condition=" + condition +
                ", email='" + email + '\'' +
                ", id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", nric='" + nric + '\'' +
                ", phone='" + phone + '\'' +
                ", username='" + username + '\'' +
                '}';
    }
}