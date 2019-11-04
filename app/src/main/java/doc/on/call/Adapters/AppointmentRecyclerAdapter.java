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
        final Appointment appointment = patient.getAppointments().get(i);
        // Set Event Listener to Appointment Delete Icon
        viewHolder.imgAppointmentDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String appointmentId = appointment.getId();
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
        // Set data into view holder
        viewHolder.tvAppointmentDate.setText(convertDateTime(appointment.getAppointmentDateTime(), DT_DAY_MONTH));
        viewHolder.tvAppointmentDayTime.setText(convertDateTime(appointment.getAppointmentDateTime(), DT_DAY_TIME));
        viewHolder.tvAppointmentNumber.setText("Appointment " + appointment.getAppointmentNumber());
        viewHolder.tvAppointmentDoctor.setText(appointment.getDoctorDetail().getName());
        viewHolder.tvAppointmentIssue.setText(appointment.getPatientDetail().getIssue());
        // Initialise initial permission
        if (appointment.getExtraPatientDetails() == null) {
            viewHolder.swTogglePermission.setChecked(false);
        } else {
            viewHolder.swTogglePermission.setChecked(true);
        }
        viewHolder.swTogglePermission.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Toggle request to details permission
                mPatient.respondToDetailsPermission(appointment.getId(), isChecked);
            }
        });
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
        private final Switch swTogglePermission;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imgAppointmentDelete = (ImageView) itemView.findViewById(R.id.imgAppointmentDelete);
            tvAppointmentDate = (TextView) itemView.findViewById(R.id.tvAppointmentDate);
            tvAppointmentDayTime = (TextView) itemView.findViewById(R.id.tvAppointmentDayTime);
            tvAppointmentNumber = (TextView) itemView.findViewById(R.id.tvAppointmentNumber);
            tvAppointmentDoctor = (TextView) itemView.findViewById(R.id.tvAppointmentDoctor);
            tvAppointmentIssue = (TextView) itemView.findViewById(R.id.tvAppointmentIssue);
            swTogglePermission = (Switch) itemView.findViewById(R.id.swTogglePermission);
        }
    }
}
