package com.mahersoua.bakingapp.utils;

import com.mahersoua.bakingapp.models.IngredientModel;

import java.util.ArrayList;

public class ArrayToString {

    public static String buildIngredientString(ArrayList<IngredientModel> list) {

        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < list.size(); i++) {
            IngredientModel ingredientModel = list.get(i);
            stringBuilder.append(ingredientModel.getIngredient());
            stringBuilder.append(ingredientModel.getQuantity());
            stringBuilder.append(ingredientModel.getMeasure());
            if (i < list.size() - 1) {
                stringBuilder.append(" / ");
            }
        }
        return stringBuilder.toString();
    }
}
