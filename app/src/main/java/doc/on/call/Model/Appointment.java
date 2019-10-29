package doc.on.call.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Appointment {
    @SerializedName("appointmentDateTime")
    @Expose
    private String appointmentDateTime;
    @SerializedName("appointmentNumber")
    @Expose
    private String appointmentNumber;
    @SerializedName("diagnosis")
    @Expose
    private String diagnosis;
    @SerializedName("doctorDetail")
    @Expose
    private DoctorDetail doctorDetail;
    @SerializedName("done")
    @Expose
    private boolean done;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("patientDetail")
    @Expose
    private PatientDetail patientDetail;

    public String getAppointmentDateTime() {
        return this.appointmentDateTime;
    }

    public String getAppointmentNumber() {
        return this.appointmentNumber;
    }

    public String getDiagnosis() {
        return this.diagnosis;
    }

    public DoctorDetail getDoctorDetail() {
        return this.doctorDetail;
    }

    public String getId() {
        return this.id;
    }

    public PatientDetail getPatientDetail() {
        return this.patientDetail;
    }

    public boolean isDone() {
        return this.done;
    }

    public void setAppointmentDateTime(String str) {
        this.appointmentDateTime = str;
    }

    public void setAppointmentNumber(String str) {
        this.appointmentNumber = str;
    }

    public void setDiagnosis(String str) {
        this.diagnosis = str;
    }

    public void setDoctorDetail(DoctorDetail doctorDetail) {
        this.doctorDetail = doctorDetail;
    }

    public void setDone(boolean z) {
        this.done = z;
    }

    public void setId(String str) {
        this.id = str;
    }

    public void setPatientDetail(PatientDetail patientDetail) {
        this.patientDetail = patientDetail;
    }

    @Override
    public String toString() {
        return "Appointment{" +
                "appointmentDateTime='" + appointmentDateTime + '\'' +
                ", appointmentNumber='" + appointmentNumber + '\'' +
                ", diagnosis='" + diagnosis + '\'' +
                ", doctorDetail=" + doctorDetail +
                ", done=" + done +
                ", id='" + id + '\'' +
                ", patientDetail=" + patientDetail +
                '}';
    }
}