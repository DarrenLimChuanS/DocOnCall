package doc.on.call.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import doc.on.call.Model.Appointment;
import doc.on.call.Model.Patient;
import doc.on.call.R;

public class AppointmentRecyclerAdapter extends RecyclerView.Adapter<AppointmentRecyclerAdapter.ViewHolder> {
    private static final String TAG = AppointmentRecyclerAdapter.class.getSimpleName();

    private Context context;
    private Patient patient;

    public AppointmentRecyclerAdapter(Context context, Patient patient) {
        this.context = context;
        this.patient = patient;
    }

    @NonNull
    @Override
    public AppointmentRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.recycler_appointment, viewGroup, false);
        return new AppointmentRecyclerAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AppointmentRecyclerAdapter.ViewHolder viewHolder, int i) {
        Appointment appointment = patient.getAppointments().get(i);
        viewHolder.appointmentDateTime.setText(appointment.getAppointmentDateTime());
    }

    @Override
    public int getItemCount() {
        return patient.getAppointments().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView appointmentDateTime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            appointmentDateTime = (TextView) itemView.findViewById(R.id.appointmentDateTime);
        }
    }
}
