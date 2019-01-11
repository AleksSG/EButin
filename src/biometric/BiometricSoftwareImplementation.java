package biometric;

import data.BiometricData;
import exceptions.BiometricVerificationFailedException;
import exceptions.data.NotValidBiometricDataException;

public class BiometricSoftwareImplementation implements BiometricSoftware {

    private BiometricReader bioReader;
    private BiometricScanner bioScanner;

    public BiometricSoftwareImplementation(BiometricReader bioReader, BiometricScanner bioScanner) {
        this.bioReader = bioReader;
        this.bioScanner = bioScanner;
    }

    @Override
    public void verifyBiometricData() throws BiometricVerificationFailedException {
        BiometricData dataScanned;
        BiometricData dataRead;

        try {
            System.out.println("We are going to start by scanning your face, put your face near the camera.");
            Thread.sleep(3000);
            System.out.println("Great, now we are going to scan your fingerprints, place your index finger from right hand.");
            Thread.sleep(3000);
            dataScanned = new BiometricData(bioScanner.scanFace(), bioScanner.scanFingerPrint());

            System.out.println("Great! Please, enter your passport.");

            dataRead = bioReader.readBiometricData();
            Thread.sleep(3000);
            System.out.println("Your BiometricData has been sucessfully read from your passport.");
        } catch (InterruptedException | NotValidBiometricDataException e) {
            e.printStackTrace();
            throw new BiometricVerificationFailedException();
        }

        if (dataRead.isNotSimilarTo(dataScanned))
            throw new BiometricVerificationFailedException();

        System.out.println("Everything is OK.");
    }
}
