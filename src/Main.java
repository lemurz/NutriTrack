import services.*;
import models.*;
import java.util.*;
import managers.*;
import csv.*;


public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final RecipeService recipeService = new RecipeService();
    private static final IngredientService ingredientService = new IngredientService();
    private static final MealPlanService mealPlanService = new MealPlanService();
    private static final RecipeManager recipeManager = new RecipeManager(recipeService, ingredientService, scanner);
    private static final IngredientManager ingredientManager = new IngredientManager(ingredientService, scanner);
    private static final MealPlanManager mealPlanManager = new MealPlanManager(mealPlanService, recipeService, scanner);

    public static void main(String[] args) {
        addSampleData();
        boolean exit = false;
        while (!exit) {
            printMainMenu();
            int choice = getValidatedIntInput();
            switch (choice) {
                case 1:
                    recipeManager.addRecipe();
                    break;
                case 2:
                    recipeManager.editRecipe();
                    break;
                case 3:
                    recipeManager.deleteRecipe();
                    break;
                case 4:
                    recipeManager.viewAllRecipes();
                    break;
                case 5:
                    recipeManager.searchForRecipe();
                    break;
                case 6:
                    ingredientManager.ingredientManagementMenu();
                    break;
                case 7:
                    mealPlanManager.mealPlanManagementMenu();
                    break;
                case 0:
                    exit = true;
                    System.out.println("Exiting application. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
        scanner.close();
    }

    private static void printMainMenu() {
        System.out.println("\n==== Welcome to NutriTrack ====");
        System.out.println("1. Add Recipe");
        System.out.println("2. Edit Recipe");
        System.out.println("3. Delete Recipe");
        System.out.println("4. View All Recipes");
        System.out.println("5. Search for Recipe");
        System.out.println("6. Ingredient Management");
        System.out.println("7. Meal Plan Management");
        System.out.println("0. Exit");
        System.out.print("Choose an option: ");
    }

    private static int getValidatedIntInput() {
        while (!scanner.hasNextInt()) {
            System.out.print("Please enter a valid number: ");
            scanner.next();
        }
        int value = scanner.nextInt();
        scanner.nextLine();
        return value;
    }

    private static void addSampleData() {
        CSVReader csvReader = new CSVReader(ingredientService, recipeService);
        csvReader.importDataFromCSV("f:\\IUT\\Third Sem\\OOP-II\\NutriTrack\\data\\sample_data.csv");
    }
}

