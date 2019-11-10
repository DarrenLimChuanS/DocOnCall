package doc.on.call.Fragments;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import doc.on.call.Adapters.AppointmentRecyclerAdapter;
import doc.on.call.Model.Appointment;
import doc.on.call.Model.Patient;
import doc.on.call.R;
import doc.on.call.ViewModel.PatientViewModel;
import xyz.sangcomz.stickytimelineview.RecyclerSectionItemDecoration;
import xyz.sangcomz.stickytimelineview.TimeLineRecyclerView;
import xyz.sangcomz.stickytimelineview.model.SectionInfo;

import static doc.on.call.Utilities.Commons.convertDateTime;
import static doc.on.call.Utilities.Constants.DT_MONTH_YEAR;

public class PatientFragment extends Fragment {
    private static final String TAG = PatientFragment.class.getSimpleName();

    // Declare variables
    private View view;
    private Context context;
    private ProgressBar pbLoading;

    // Error: No Appointment
    private LinearLayout rvEmpty;
    private Button btnMakeAppointment;

    // Appointment Recycler
    private TimeLineRecyclerView rvAppointments;
    private LinearLayoutManager mLayoutManager;
    private AppointmentRecyclerAdapter mAdapter;
    private PatientViewModel mViewModel;
    private Patient patient = new Patient();

    public PatientFragment (Context context) {
        this.context = context;
    }


    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        view =  layoutInflater.inflate(R.layout.fragment_patient, viewGroup, false);

        // Fetch variables
        pbLoading = (ProgressBar)view.findViewById(R.id.pbLoading);
        pbLoading.setVisibility(View.VISIBLE);

        rvEmpty = (LinearLayout) view.findViewById(R.id.rvEmpty);
        btnMakeAppointment = (Button) view.findViewById(R.id.btnMakeAppointment);
        rvAppointments = (TimeLineRecyclerView)view.findViewById(R.id.rvAppointments);

        // Layout Manager
        mLayoutManager = new LinearLayoutManager(context, RecyclerView.VERTICAL, false);
        rvAppointments.setLayoutManager(mLayoutManager);

        // View Model
        mViewModel = (PatientViewModel) ViewModelProviders.of(this).get(PatientViewModel.class);
        mViewModel.getPatientLiveData().observe(this, new Observer<Patient>() {
            @Override
            public void onChanged(@Nullable Patient patientResponse) {
                pbLoading.setVisibility(View.GONE);
                if (patientResponse != null) {
                    patient = patientResponse;
                    if (!patient.getAppointments().isEmpty()) {
                        // Reverse the list to display in Chronological order
                        List<Appointment> appointmentList = new ArrayList<>();
                        appointmentList = patient.getAppointments();
                        Collections.reverse(appointmentList);
                        patient.setAppointments(appointmentList);
                        if (mAdapter != null) {
                            mAdapter.setPatient(patient);
                            rvAppointments.addItemDecoration(getSectionCallback(patient));
                            rvAppointments.setAdapter(mAdapter);
                            mAdapter.notifyDataSetChanged();
                        } else {
                            // Adapter
                            mAdapter = new AppointmentRecyclerAdapter(context, patient);
                            rvAppointments.addItemDecoration(getSectionCallback(patient));
                            rvAppointments.setAdapter(mAdapter);
                            mAdapter.notifyDataSetChanged();
                        }
                    } else {
                        // Display Error No Appointment
                        rvAppointments.setVisibility(View.GONE);
                        rvEmpty.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        // Event Listener
        btnMakeAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DocOnCallFragment docOnCallFragment = new DocOnCallFragment(context);
                ((FragmentActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, docOnCallFragment).commit();
            }
        });

        return view;
    }

    private RecyclerSectionItemDecoration.SectionCallback getSectionCallback(final Patient patientAppointment) {
        return new RecyclerSectionItemDecoration.SectionCallback() {

            @Nullable
            @Override
            public SectionInfo getSectionHeader(int position) {
                Appointment appointment = patientAppointment.getAppointments().get(position);
                Drawable dot;
                dot = AppCompatResources.getDrawable(context, R.drawable.ic_timeline);;
                String monthYearSection = "";
                monthYearSection = convertDateTime(appointment.getAppointmentDateTime(), DT_MONTH_YEAR);

                return new SectionInfo(monthYearSection, "", dot);
            }

            @Override
            public boolean isSection(int position) {
                // First appointment date time
                String firstMonthYear = convertDateTime(patientAppointment.getAppointments().get(position).getAppointmentDateTime(), DT_MONTH_YEAR);

                // Second appointment date time
                String secondMonthYear = convertDateTime(patientAppointment.getAppointments().get(position - 1).getAppointmentDateTime(), DT_MONTH_YEAR);

                return !firstMonthYear.equals(secondMonthYear);
            }
        };

    }

    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
    }
}