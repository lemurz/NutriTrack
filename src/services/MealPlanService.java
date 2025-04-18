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
        // Split filtered recipes into 7 daily meal plans (simple round-robin or chunking)
        List<DailyMealPlan> dailyPlans = new ArrayList<>();
        int days = 7;
        int chunkSize = (int) Math.ceil(filtered.size() / (double) days);
        for (int i = 0; i < days; i++) {
            int start = i * chunkSize;
            int end = Math.min(start + chunkSize, filtered.size());
            if (start < end) {
                dailyPlans.add(new DailyMealPlan(name + " Day " + (i+1), filtered.subList(start, end)));
            }
        }
        return new WeeklyMealPlan(name, dailyPlans);
    }

    private List<Recipe> filterRecipes(List<Recipe> recipes, int calorieLimit, Set<String> requiredTags) {
        List<Recipe> filtered = new ArrayList<>();
        int totalCalories = 0;
        for (Recipe recipe : recipes) {
            if (!Collections.disjoint(recipe.getTags(), requiredTags) && (totalCalories + recipe.getCalories() <= calorieLimit)) {
                filtered.add(recipe);
                totalCalories += recipe.getCalories();
            }
        }
        return filtered;
    }
}