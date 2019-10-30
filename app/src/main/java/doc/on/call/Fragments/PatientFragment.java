package doc.on.call.Fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import doc.on.call.R;
import doc.on.call.ViewModel.PatientViewModel;

public class PatientFragment extends Fragment {
    private PatientViewModel mViewModel;

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return layoutInflater.inflate(R.layout.fragment_patient, viewGroup, false);
    }

    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        this.mViewModel = (PatientViewModel) ViewModelProviders.of(this).get(PatientViewModel.class);
    }
}