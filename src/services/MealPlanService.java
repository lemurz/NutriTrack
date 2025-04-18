package services;

import models.*;
import java.util.*;

public class MealPlanService {
    private final Map<String, DailyMealPlan> dailyMealPlans = new HashMap<>();
    private final Map<String, WeeklyMealPlan> weeklyMealPlans = new HashMap<>();
    public DailyMealPlan generateDailyMealPlan(String name, List<Recipe> recipes, int calorieLimit, Set<String> requiredTags) {
        List<Recipe> filtered = filterRecipes(recipes, calorieLimit, requiredTags);
        DailyMealPlan plan = new DailyMealPlan(name, filtered);
        dailyMealPlans.put(name, plan);
        return plan;
    }

    public WeeklyMealPlan generateWeeklyMealPlan(String name, List<Recipe> recipes, int calorieLimit, Set<String> requiredTags) {
        List<Recipe> filtered = filterRecipes(recipes, calorieLimit, requiredTags);
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
        WeeklyMealPlan plan = new WeeklyMealPlan(name, dailyPlans);
        weeklyMealPlans.put(name, plan);
        return plan;
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

    public List<DailyMealPlan> getAllDailyMealPlans() {
        return new ArrayList<>(dailyMealPlans.values());
    }

    public List<WeeklyMealPlan> getAllWeeklyMealPlans() {
        return new ArrayList<>(weeklyMealPlans.values());
    }

    public MealPlan viewMealPlan(String name) {
        if (dailyMealPlans.containsKey(name)) {
            return dailyMealPlans.get(name);
        } else if (weeklyMealPlans.containsKey(name)) {
            return weeklyMealPlans.get(name);
        }
        return null;
    }
}