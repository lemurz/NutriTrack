package factories;

import models.*;
import java.util.List;
import java.util.Set;

public class MealPlanFactory {
    public static MealPlan create(String type, String name, List<?> items) {
        if (type.equalsIgnoreCase("daily")) {
            @SuppressWarnings("unchecked")
            List<Recipe> recipes = (List<Recipe>) items;
            return new DailyMealPlan(name, recipes);
        } else if (type.equalsIgnoreCase("weekly")) {
            @SuppressWarnings("unchecked")
            List<DailyMealPlan> dailyPlans = (List<DailyMealPlan>) items;
            return new WeeklyMealPlan(name, dailyPlans);
        } else {
            throw new IllegalArgumentException("Unknown meal plan type: " + type);
        }
    }
}