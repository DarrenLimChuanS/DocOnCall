package doc.on.call.ViewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import doc.on.call.Model.Patient;
import doc.on.call.Repository.PatientRepository;

public class PatientViewModel extends AndroidViewModel {

    private PatientRepository mPatient;

    public PatientViewModel(@NonNull Application application) {
        super(application);
        mPatient = new PatientRepository(application.getApplicationContext());
    }

    public LiveData<Patient> getPatientLiveData() {
        return mPatient.getPatient();
    }
}