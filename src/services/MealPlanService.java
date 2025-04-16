package services;

import models.*;
import java.util.*;

public class MealPlanService {
    public DailyMealPlan generateDailyMealPlan(String name, List<Recipe> recipes, int calorieLimit, Set<String> requiredTags) {
        List<Recipe> filtered = filterRecipes(recipes, calorieLimit, requiredTags);
        return new DailyMealPlan(name, filtered);
    }

    public WeeklyMealPlan generateWeeklyMealPlan(String name, List<Recipe> recipes, int calorieLimit, Set<String> requiredTags) {
        List<Recipe> filtered = filterRecipes(recipes, calorieLimit, requiredTags);
        return new WeeklyMealPlan(name, filtered);
    }

    private List<Recipe> filterRecipes(List<Recipe> recipes, int calorieLimit, Set<String> requiredTags) {
        List<Recipe> filtered = new ArrayList<>();
        int totalCalories = 0;
        for (Recipe recipe : recipes) {
            if (recipe.getTags().containsAll(requiredTags) && (totalCalories + recipe.getCalories() <= calorieLimit)) {
                filtered.add(recipe);
                totalCalories += recipe.getCalories();
            }
        }
        return filtered;
    }
}