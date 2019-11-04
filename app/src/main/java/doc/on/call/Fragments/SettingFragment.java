package doc.on.call.Fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import doc.on.call.R;
import doc.on.call.Repository.PatientRepository;

public class SettingFragment extends Fragment {
    private static final String TAG = SettingFragment.class.getSimpleName();

    private Context context;
    private CardView cvUpdatePatient;
    private CardView cvChangePassword;
    private CardView cvDeletePatient;
    private CardView cvLogoutPatient;
    private PatientRepository mPatient;

    public SettingFragment (Context context) {
        this.context = context;
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View view = layoutInflater.inflate(R.layout.fragment_setting, viewGroup, false);

        mPatient = new PatientRepository(getContext());

        cvUpdatePatient = (CardView) view.findViewById(R.id.cvUpdatePatient);
        cvChangePassword = (CardView) view.findViewById(R.id.cvChangePassword);
        cvDeletePatient = (CardView) view.findViewById(R.id.cvDeletePatient);
        cvLogoutPatient = (CardView) view.findViewById(R.id.cvLogoutPatient);

        cvDeletePatient.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                getPatient();
            }
        });

        cvLogoutPatient.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                logoutPatient();
            }
        });

        return view;
    }

    public void logoutPatient() {
        mPatient.logoutPatient();
    }

    public void getPatient(){
        mPatient.getAllPatients();
    }
}