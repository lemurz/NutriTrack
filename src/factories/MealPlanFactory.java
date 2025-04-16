package factories;

import models.*;
import java.util.List;
import java.util.Set;

public class MealPlanFactory {
    public static MealPlan create(String type, String name, List<Recipe> recipes) {
        if (type.equalsIgnoreCase("daily")) {
            return new DailyMealPlan(name, recipes);
        } else if (type.equalsIgnoreCase("weekly")) {
            return new WeeklyMealPlan(name, recipes);
        } else {
            throw new IllegalArgumentException("Unknown meal plan type: " + type);
        }
    }
}