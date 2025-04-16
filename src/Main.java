import services.*;
import models.*;
import java.util.*;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final RecipeService recipeService = new RecipeService();

    public static void main(String[] args) {
        boolean exit = false;
        while (!exit) {
            printMainMenu();
            int choice = getUserChoice();
            switch (choice) {
                case 1:
                    addRecipe();
                    break;
                case 2:
                    editRecipe();
                    break;
                case 3:
                    deleteRecipe();
                    break;
                case 4:
                    viewAllRecipes();
                    break;
                case 5:
                    // Placeholder: IngredientService integration
                    System.out.println("Ingredient management coming soon...");
                    break;
                case 6:
                    // Placeholder: MealPlanService integration
                    System.out.println("Meal plan management coming soon...");
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
        System.out.println("5. Ingredient Management (coming soon)");
        System.out.println("6. Meal Plan Management (coming soon)");
        System.out.println("0. Exit");
        System.out.print("Choose an option: ");
    }

    private static int getUserChoice() {
        while (!scanner.hasNextInt()) {
            System.out.print("Please enter a valid number: ");
            scanner.next();
        }
        int choice = scanner.nextInt();
        scanner.nextLine(); 
        return choice;
    }

    private static void addRecipe() {
        System.out.println("\n--- Add Recipe ---");
        System.out.print("Enter recipe name: ");
        String name = scanner.nextLine();
        System.out.print("Enter calories: ");
        int calories = getIntInput();
        System.out.print("Enter tags (comma separated): ");
        String tagsInput = scanner.nextLine();
        Set<String> tags = new HashSet<>(Arrays.asList(tagsInput.split(",\s*")));
        List<Ingredient> ingredients = new ArrayList<>();
        List<String> steps = new ArrayList<>();
        Recipe recipe = new Recipe(name, ingredients, steps, calories, tags);
        recipeService.addRecipe(recipe);
        System.out.println("Recipe added successfully!");
    }

    private static void editRecipe() {
        System.out.println("\n--- Edit Recipe ---");
        List<Recipe> recipes = recipeService.getAllRecipes();
        if (recipes.isEmpty()) {
            System.out.println("No recipes to edit.");
            return;
        }
        viewAllRecipes();
        System.out.print("Enter recipe number to edit: ");
        int index = getIntInput() - 1;
        if (index < 0 || index >= recipes.size()) {
            System.out.println("Invalid recipe number.");
            return;
        }
        System.out.print("Enter new recipe name: ");
        String name = scanner.nextLine();
        System.out.print("Enter new calories: ");
        int calories = getIntInput();
        System.out.print("Enter new tags (comma separated): ");
        String tagsInput = scanner.nextLine();
        Set<String> tags = new HashSet<>(Arrays.asList(tagsInput.split(",\s*")));
        List<Ingredient> ingredients = recipes.get(index).getIngredients();
        List<String> steps = recipes.get(index).getSteps();
        Recipe updatedRecipe = new Recipe(name, ingredients, steps, calories, tags);
        recipeService.editRecipe(index, updatedRecipe);
        System.out.println("Recipe updated successfully!");
    }

    private static void deleteRecipe() {
        System.out.println("\n--- Delete Recipe ---");
        List<Recipe> recipes = recipeService.getAllRecipes();
        if (recipes.isEmpty()) {
            System.out.println("No recipes to delete.");
            return;
        }
        viewAllRecipes();
        System.out.print("Enter recipe number to delete: ");
        int index = getIntInput() - 1;
        if (index < 0 || index >= recipes.size()) {
            System.out.println("Invalid recipe number.");
            return;
        }
        recipeService.deleteRecipe(index);
        System.out.println("Recipe deleted successfully!");
    }

    private static void viewAllRecipes() {
        System.out.println("\n--- All Recipes ---");
        List<Recipe> recipes = recipeService.getAllRecipes();
        if (recipes.isEmpty()) {
            System.out.println("No recipes found.");
            return;
        }
        int i = 1;
        for (Recipe recipe : recipes) {
            System.out.println(i + ". " + recipe.getName());
            i++;
        }
    }

    private static int getIntInput() {
        while (!scanner.hasNextInt()) {
            System.out.print("Please enter a valid number: ");
            scanner.next();
        }
        int value = scanner.nextInt();
        scanner.nextLine(); 
        return value;
    }
}
