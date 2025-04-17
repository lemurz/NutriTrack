package utils;

import models.Recipe;
import java.util.List;

public class CalorieCalculator {

    public static int calculateTotalCalories(List<Recipe> recipes) {
        return recipes.stream().mapToInt(Recipe::getCalories).sum();
    }

    public static double recommendPortionSize(Recipe recipe, int calorieLimit) {
        if (recipe.getCalories() == 0) {
            return 0;
        }
        double portion = (double) calorieLimit / recipe.getCalories();
        return Math.min(1.0, portion); // Do not recommend more than 1 portion
    }
}