package biometric;

import data.BiometricData;
import exceptions.data.NotValidBiometricDataException;

public class BiometricReaderImplementation implements BiometricReader {

    private BiometricData bioDataRead;

    public BiometricReaderImplementation(BiometricData bioDataRead) {
        this.bioDataRead = bioDataRead;
    }
    
    @Override
    public BiometricData readBiometricData() throws NotValidBiometricDataException {
        return bioDataRead;
    }
}
