package com.mahersoua.bakingapp.dataproviders;

public class DataProvider {

    private static int currentStep;

    public static long getCurrentPlayerPosition() {
        return currentPlayerPosition;
    }

    public static void setCurrentPlayerPosition(long currentPlayerPosition) {
        DataProvider.currentPlayerPosition = currentPlayerPosition;
    }

    private static long currentPlayerPosition;

    public static int getCurrentStep() {
        return currentStep;
    }

    public static void setCurrentStep(int currentStep) {
        DataProvider.currentStep = currentStep;
    }


}
