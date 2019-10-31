package doc.on.call.ViewModel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.annotation.NonNull;

import doc.on.call.Model.Patient;
import doc.on.call.Repository.PatientRepository;

public class PatientViewModel extends AndroidViewModel {

    private PatientRepository mPatient;
    private LiveData<Patient> patient;

    public PatientViewModel(@NonNull Application application) {
        super(application);
        mPatient = new PatientRepository(application.getApplicationContext());
        patient = mPatient.getPatient();
    }

    public LiveData<Patient> getPatientLiveData() {
        return patient;
    }
}