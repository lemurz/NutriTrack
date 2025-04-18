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
            System.out.println("4. View Existing Meal Plans");
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
                case 4:
                    viewExistingMealPlans();
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
        
        MealPlan mealPlan;
        if (weekly) {
            mealPlan = mealPlanService.generateWeeklyMealPlan("Weekly Plan", recipeService.getAllRecipes(), calorieLimit, requiredTags);
        } else {
            mealPlan = mealPlanService.generateDailyMealPlan("Daily Plan", recipeService.getAllRecipes(), calorieLimit, requiredTags);
        }
        mealPlan.displayDetails();
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

    private void viewExistingMealPlans() {
        System.out.println("\n--- Existing Meal Plans ---");
        List<DailyMealPlan> dailyPlans = mealPlanService.getAllDailyMealPlans();
        List<WeeklyMealPlan> weeklyPlans = mealPlanService.getAllWeeklyMealPlans();
        if (dailyPlans.isEmpty() && weeklyPlans.isEmpty()) {
            System.out.println("No meal plans found.");
            return;
        }
        int num = 1;
        Map<Integer, MealPlan> planIndex = new HashMap<>();
        num = listMealPlans(dailyPlans, num, planIndex);
        listMealPlans(weeklyPlans, num, planIndex);
        System.out.print("Enter the number of the meal plan to view details (0 to go back): ");
        int choice = getValidatedIntInput();
        if (choice == 0) return;
        MealPlan selectedPlan = planIndex.get(choice);
        if (selectedPlan == null) {
            System.out.println("Invalid selection.");
            return;
        }
        selectedPlan.displayDetails();
    }

    private int listMealPlans(List<? extends MealPlan> plans, int startNum, Map<Integer, MealPlan> planIndex) {
        for (MealPlan plan : plans) {
            System.out.println(startNum + ". " + plan.getName());
            planIndex.put(startNum, plan);
            startNum++;
        }
        return startNum;
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