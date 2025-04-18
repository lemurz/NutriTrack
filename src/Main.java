import services.*;
import models.*;
import java.util.*;
import managers.RecipeManager;
import managers.IngredientManager;
import managers.MealPlanManager;

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
        System.out.println("\n==== Recipe Management System ====");
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
        Ingredient egg = new Ingredient("Egg", 2, 78, 6, 1, 5);
        Ingredient milk = new Ingredient("Milk", 200, 42, 3.4, 5, 1);
        Ingredient flour = new Ingredient("Flour", 100, 364, 10, 76, 1);
        Ingredient sugar = new Ingredient("Sugar", 50, 387, 0, 100, 0);
    
        ingredientService.addOrUpdateIngredient(egg);
        ingredientService.addOrUpdateIngredient(milk);
        ingredientService.addOrUpdateIngredient(flour);
        ingredientService.addOrUpdateIngredient(sugar);
    
        List<Ingredient> pancakeIngredients = Arrays.asList(
                new Ingredient("Egg", 2, 78, 6, 1, 5),
                new Ingredient("Milk", 200, 42, 3.4, 5, 1),
                new Ingredient("Flour", 100, 364, 10, 76, 1),
                new Ingredient("Sugar", 20, 387, 0, 100, 0)
        );
    
        List<String> pancakeSteps = Arrays.asList(
                "Mix all ingredients together.",
                "Heat a pan and pour batter.",
                "Cook until golden brown on both sides."
        );
    
        Set<String> pancakeTags = new HashSet<>(Arrays.asList("breakfast", "sweet", "easy"));
        Recipe pancake = new Recipe("Pancake", pancakeIngredients, pancakeSteps, 350, pancakeTags);
    
        recipeService.addRecipe(pancake);
    
        List<Ingredient> omeletteIngredients = Arrays.asList(
                new Ingredient("Egg", 3, 78, 6, 1, 5),
                new Ingredient("Milk", 50, 42, 3.4, 5, 1)
        );
    
        List<String> omeletteSteps = Arrays.asList(
                "Beat eggs with milk.",
                "Pour mixture into a heated pan.",
                "Cook until set."
        );
    
        Set<String> omeletteTags = new HashSet<>(Arrays.asList("breakfast", "protein-rich"));
        Recipe omelette = new Recipe("Omelette", omeletteIngredients, omeletteSteps, 250, omeletteTags);
    
        recipeService.addRecipe(omelette);

        List<Ingredient> smoothieIngredients = Arrays.asList(
                new Ingredient("Milk", 200, 42, 3.4, 5, 1),
                new Ingredient("Sugar", 10, 387, 0, 100, 0)
        );
    
        List<String> smoothieSteps = Arrays.asList(
                "Blend milk and sugar together.",
                "Serve chilled."
        );
    
        Set<String> smoothieTags = new HashSet<>(Arrays.asList("drink", "sweet", "refreshing"));
        Recipe smoothie = new Recipe("Smoothie", smoothieIngredients, smoothieSteps, 150, smoothieTags);
    
        recipeService.addRecipe(smoothie);
    }
}

