package com.celtica.doctor.Diagnosis;

public class DiagnoResult {
    public Diagnostique diagnostique;
    public Maladie maladie;
    public double probabilité;
    public String specialisation;

    public DiagnoResult(Maladie maladie, double probabilité, String specialisation) {

        this.maladie = maladie;
        this.probabilité = probabilité;
        this.specialisation = specialisation;
    }

    public DiagnoResult(Maladie maladie, double probabilité) {
        this.maladie = maladie;
        this.probabilité = probabilité;
    }
}
