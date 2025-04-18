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

    public WeeklyMealPlan generateWeeklyMealPlan(String name, List<Recipe> allRecipes, int calorieLimit, Set<String> requiredTags) {
        List<DailyMealPlan> dailyMealPlans = new ArrayList<>();
        int recipeIndex = 0;
        int totalRecipes = allRecipes.size();

        for (int day = 0; day < 7; day++) {
            List<Recipe> dailyRecipes = new ArrayList<>();
            
            if (totalRecipes > 0) {
                dailyRecipes.add(allRecipes.get(recipeIndex));
                recipeIndex = (recipeIndex + 1) % totalRecipes; 
            }

            DailyMealPlan dailyMealPlan = new DailyMealPlan("Day " + (day + 1), dailyRecipes);
            dailyMealPlans.add(dailyMealPlan);
        }

        return new WeeklyMealPlan(name, dailyMealPlans);
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