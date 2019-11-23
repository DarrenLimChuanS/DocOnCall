package doc.on.call.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ExtraPatientDetails {
    @SerializedName("pastAppointments")
    @Expose
    private List<PastAppointment> pastAppointments = null;

    public List<PastAppointment> getPastAppointments() {
        return pastAppointments;
    }

    public void setPastAppointments(List<PastAppointment> pastAppointments) {
        this.pastAppointments = pastAppointments;
    }

    @Override
    public String toString() {
        return "ExtraPatientDetails{" +
                "pastAppointments=" + pastAppointments +
                '}';
    }
}
