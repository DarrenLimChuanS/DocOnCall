package doc.on.call.Fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import doc.on.call.Model.Patient;
import doc.on.call.R;
import doc.on.call.ViewModel.PatientViewModel;

public class PatientFragment extends Fragment {
    private static final String TAG = SettingFragment.class.getSimpleName();
    private View view;
    private PatientViewModel mViewModel;
    private TextView tvPatient;

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        view =  layoutInflater.inflate(R.layout.fragment_patient, viewGroup, false);
        Log.d(TAG, "onCreateView");
        tvPatient = view.findViewById(R.id.patient);
        mViewModel = (PatientViewModel) ViewModelProviders.of(this).get(PatientViewModel.class);
        mViewModel.getPatientLiveData().observe(this, new Observer<Patient>() {
            @Override
            public void onChanged(@Nullable Patient patientResponse) {
                if (patientResponse != null) {
                    Log.d(TAG, "OnChanged");
                    String name = patientResponse.getName();
                    tvPatient.setText(name);
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