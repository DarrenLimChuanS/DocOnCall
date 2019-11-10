package doc.on.call.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import doc.on.call.Model.Appointment;
import doc.on.call.Model.Patient;
import doc.on.call.R;
import doc.on.call.Repository.PatientRepository;

import static doc.on.call.Utilities.Commons.convertDateTime;
import static doc.on.call.Utilities.Constants.DT_DAY_MONTH;
import static doc.on.call.Utilities.Constants.DT_DAY_TIME;

public class AppointmentRecyclerAdapter extends RecyclerView.Adapter<AppointmentRecyclerAdapter.ViewHolder> {
    private static final String TAG = AppointmentRecyclerAdapter.class.getSimpleName();

    private Context context;
    private Patient patient;
    private PatientRepository mPatient;

    public AppointmentRecyclerAdapter(Context context, Patient patient) {
        this.context = context;
        this.patient = patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    @NonNull
    @Override
    public AppointmentRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.recycler_appointment, viewGroup, false);

        mPatient = new PatientRepository(context);

        return new AppointmentRecyclerAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AppointmentRecyclerAdapter.ViewHolder viewHolder, int i) {
        // Fetch patient's appointment
        final Appointment appointment = patient.getAppointments().get(i);
        final String appointmentId = appointment.getId();
        final String appointmentNumber = appointment.getAppointmentNumber();
        final String doctorName = appointment.getDoctorDetail().getName();

        // Set data into view holder
        viewHolder.tvAppointmentDate.setText(convertDateTime(appointment.getAppointmentDateTime(), DT_DAY_MONTH));
        viewHolder.tvAppointmentDayTime.setText(convertDateTime(appointment.getAppointmentDateTime(), DT_DAY_TIME));
        viewHolder.tvAppointmentNumber.setText(context.getString(R.string.label_appointment) + " " + appointmentNumber);
        viewHolder.tvAppointmentDoctor.setText(context.getString(R.string.label_doctor) + " " +  doctorName);
        viewHolder.tvAppointmentIssue.setText(appointment.getPatientDetail().getIssue());
        // Date comparison to check if toggle permission is displayed
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd kk:m:s");
        try {
            Date appointmentDate = sdf.parse(appointment.getAppointmentDateTime());
            if (new Date().after(appointmentDate)) {
                // Appointment is history
                viewHolder.imgAppointmentDelete.setVisibility(View.GONE);
                viewHolder.tvTogglePermission.setVisibility(View.GONE);
                viewHolder.swTogglePermission.setVisibility(View.GONE);
            } else {
                // Appointment is upcoming
                // Initialise initial permission
                if (appointment.getExtraPatientDetails() == null) {
                    viewHolder.swTogglePermission.setChecked(false);
                } else {
                    viewHolder.swTogglePermission.setChecked(true);
                }
                // Set Event Listener to Appointment Delete Icon
                viewHolder.imgAppointmentDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Confirmation Dialog
                        new AlertDialog.Builder(context)
                                .setTitle(context.getResources().getString(R.string.alert_delete_appointment_title))
                                .setMessage(context.getResources().getString(R.string.alert_delete_appointment_message))
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        // Delete Appointment
                                        mPatient.deleteAppointment(appointmentId);
                                    }})
                                .setNegativeButton(android.R.string.no, null).show();
                    }
                });
                viewHolder.swTogglePermission.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        // Toggle request to details permission
                        mPatient.respondToDetailsPermission(appointmentId, isChecked, doctorName);
                    }
                });
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return patient.getAppointments().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imgAppointmentDelete;
        private final TextView tvAppointmentDate;
        private final TextView tvAppointmentDayTime;
        private final TextView tvAppointmentNumber;
        private final TextView tvAppointmentDoctor;
        private final TextView tvAppointmentIssue;
        private final TextView tvTogglePermission;
        private final Switch swTogglePermission;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imgAppointmentDelete = (ImageView) itemView.findViewById(R.id.imgAppointmentDelete);
            tvAppointmentDate = (TextView) itemView.findViewById(R.id.tvAppointmentDate);
            tvAppointmentDayTime = (TextView) itemView.findViewById(R.id.tvAppointmentDayTime);
            tvAppointmentNumber = (TextView) itemView.findViewById(R.id.tvAppointmentNumber);
            tvAppointmentDoctor = (TextView) itemView.findViewById(R.id.tvAppointmentDoctor);
            tvAppointmentIssue = (TextView) itemView.findViewById(R.id.tvAppointmentIssue);
            tvTogglePermission = (TextView) itemView.findViewById(R.id.tvTogglePermission);
            swTogglePermission = (Switch) itemView.findViewById(R.id.swTogglePermission);
        }
    }
}
