package com.mahersoua.bakingapp.dataproviders;

public class DataProvider {

    private static int currentStep;
    
    public static int getCurrentStep() {
        return currentStep;
    }

    public static void setCurrentStep(int currentStep) {
        DataProvider.currentStep = currentStep;
    }


}
