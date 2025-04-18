package managers;

import models.*;
import services.*;
import utils.*;
import java.util.*;

public class MealPlanManager {
    private final MealPlanService mealPlanService;
    private final RecipeService recipeService;
    private final Scanner scanner;

    public MealPlanManager(MealPlanService mealPlanService, RecipeService recipeService, Scanner scanner) {
        this.mealPlanService = mealPlanService;
        this.recipeService = recipeService;
        this.scanner = scanner;
    }

    public void mealPlanManagementMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- Meal Plan Management ---");
            System.out.println("1. Generate Daily Meal Plan");
            System.out.println("2. Generate Weekly Meal Plan");
            System.out.println("3. View All Recipes");
            System.out.println("0. Back to Main Menu");
            System.out.print("Choose an option: ");
            int choice = getValidatedIntInput();
            switch (choice) {
                case 1:
                    generateMealPlan(false);
                    break;
                case 2:
                    generateMealPlan(true);
                    break;
                case 3:
                    viewAllRecipes();
                    break;
                case 0:
                    back = true;
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private void generateMealPlan(boolean weekly) {
        System.out.print("Enter calorie limit: ");
        int calorieLimit = getValidatedIntInput();
        Set<String> requiredTags = promptForMealPlanTags();
        
        if (weekly) {
            WeeklyMealPlan weeklyMealPlan = mealPlanService.generateWeeklyMealPlan("Weekly Plan", recipeService.getAllRecipes(), calorieLimit, requiredTags);
            displayWeeklyMealPlan(weeklyMealPlan, calorieLimit);
        } else {
            DailyMealPlan dailyMealPlan = mealPlanService.generateDailyMealPlan("Daily Plan", recipeService.getAllRecipes(), calorieLimit, requiredTags);
            displayDailyMealPlan(dailyMealPlan, calorieLimit);
        }
    }

    private Set<String> promptForMealPlanTags() {
        System.out.print("Enter required tags (comma separated, or leave blank): ");
        String tagsInput = scanner.nextLine();
        Set<String> requiredTags = new HashSet<>();
        if (!tagsInput.trim().isEmpty()) {
            requiredTags.addAll(Arrays.asList(tagsInput.split(",\\s*")));
        }
        return requiredTags;
    }

    private void displayDailyMealPlan(DailyMealPlan dailyMealPlan, int calorieLimit) {
        System.out.println("\n==== Daily Meal Plan: " + dailyMealPlan.getName() + " ====");
        int totalCalories = CalorieCalculator.calculateTotalCalories(dailyMealPlan.getRecipes());
        System.out.println("Total Calories: " + totalCalories + " / " + calorieLimit);
        System.out.println("Recipes:");
        int i = 1;
        for (Recipe recipe : dailyMealPlan.getRecipes()) {
            System.out.println(i + ". " + recipe.getName() + " (" + recipe.getCalories() + " cal)");
            i++;
        }
    }

    private void displayWeeklyMealPlan(WeeklyMealPlan weeklyMealPlan, int calorieLimit) {
        System.out.println("\n==== Weekly Meal Plan: " + weeklyMealPlan.getName() + " ====");
        List<DailyMealPlan> days = weeklyMealPlan.getDailyMealPlans();
        int dayNum = 1;
        for (DailyMealPlan day : days) {
            System.out.println("\nDay " + dayNum + ":");
            displayDailyMealPlan(day, calorieLimit);
            dayNum++;
        }
    }

    private void viewAllRecipes() {
        List<Recipe> recipes = recipeService.getAllRecipes();
        if (recipes.isEmpty()) {
            System.out.println("No recipes found.");
            return;
        }
        int recipeNum = 1;
        for (Recipe recipe : recipes) {
            System.out.println(recipeNum + ". " + recipe.getName());
            recipeNum++;
        }
    }

    private int getValidatedIntInput() {
        while (!scanner.hasNextInt()) {
            System.out.print("Please enter a valid number: ");
            scanner.next();
        }
        int value = scanner.nextInt();
        scanner.nextLine();
        return value;
    }
}