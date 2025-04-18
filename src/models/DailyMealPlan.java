package models;

import java.util.List;
import java.util.HashSet;
import java.util.Set;

import utils.CalorieCalculator;

public class DailyMealPlan extends MealPlan {
    public DailyMealPlan(String name, List<Recipe> recipes) {
        super(name, recipes);
    }

    @Override
    public int getCalories() {
        return recipes.stream().mapToInt(Recipe::getCalories).sum();
    }

    @Override
    public Set<String> getTags() {
        Set<String> tags = new HashSet<>();
        for (Recipe recipe : recipes) {
            tags.addAll(recipe.getTags());
        }
        return tags;
    }

    @Override
    public void displayDetails() {
        System.out.println("\n==== Daily Meal Plan: " + getName() + " ====");
        int totalCalories = CalorieCalculator.calculateTotalCalories(getRecipes());
        System.out.println("Total Calories: " + totalCalories);
        System.out.println("Recipes:");
        int recipeNum = 1;
        for (Recipe recipe : getRecipes()) {
            System.out.println(recipeNum + ". " + recipe.getName() + " (" + recipe.getCalories() + " cal)");
            recipeNum++;
        }
    }
}