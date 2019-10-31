package doc.on.call.Fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import doc.on.call.Adapters.AppointmentRecyclerAdapter;
import doc.on.call.Model.Patient;
import doc.on.call.R;
import doc.on.call.ViewModel.PatientViewModel;

public class PatientFragment extends Fragment {
    private static final String TAG = SettingFragment.class.getSimpleName();

    // Declare variables
    private View view;
    private ProgressBar pbLoading;
    private TextView tvPatient;

    private RecyclerView rvAppointments;
    private LinearLayoutManager mLayoutManager;
    private AppointmentRecyclerAdapter mAdapter;
    private PatientViewModel mViewModel;
    private Patient patient = new Patient();


    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        view =  layoutInflater.inflate(R.layout.fragment_patient, viewGroup, false);
        Log.d(TAG, "onCreateView");

        // Fetch variables
        pbLoading = (ProgressBar)view.findViewById(R.id.pbLoading);
        tvPatient = (TextView)view.findViewById(R.id.rvEmpty);
        rvAppointments = (RecyclerView)view.findViewById(R.id.rvAppointments);

        // Layout Manager
        mLayoutManager = new LinearLayoutManager(getContext());
        rvAppointments.setLayoutManager(mLayoutManager);
        rvAppointments.setHasFixedSize(true);

        mViewModel = (PatientViewModel) ViewModelProviders.of(this).get(PatientViewModel.class);
        mViewModel.getPatientLiveData().observe(this, new Observer<Patient>() {
            @Override
            public void onChanged(@Nullable Patient patientResponse) {
                if (patientResponse != null) {
                    Log.d(TAG, "OnChanged");
                    String name = patientResponse.getName();
                    tvPatient.setText(name);
                    patient = patientResponse;
                    if (mAdapter != null) {
                        rvAppointments.setAdapter(mAdapter);
                        mAdapter.notifyDataSetChanged();
                    } else {
                        // Adapter
                        mAdapter = new AppointmentRecyclerAdapter(getContext(), patient);
                        rvAppointments.setAdapter(mAdapter);
                    }
                }
            }
        });

        return view;
    }

    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPauseFrag");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG, "onDestroyView");
    }
}