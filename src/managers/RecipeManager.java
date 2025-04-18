package managers;

import models.*;
import services.*;
import java.util.*;

public class RecipeManager {
    private final RecipeService recipeService;
    private final IngredientService ingredientService;
    private final Scanner scanner;

    public RecipeManager(RecipeService recipeService, IngredientService ingredientService, Scanner scanner) {
        this.recipeService = recipeService;
        this.ingredientService = ingredientService;
        this.scanner = scanner;
    }

    public void addRecipe() {
        System.out.println("\n--- Add Recipe ---");
        System.out.print("Enter recipe name: ");
        String name = scanner.nextLine();
        System.out.print("Enter calories: ");
        int calories = getValidatedIntInput();
        System.out.print("Enter tags (comma separated): ");
        String tagsInput = scanner.nextLine();
        Set<String> tags = new HashSet<>(Arrays.asList(tagsInput.split(",\\s*")));
        List<Ingredient> ingredients = selectIngredientsForRecipe();
        List<String> steps = new ArrayList<>();
        System.out.println("Enter steps (type 'done' to finish):");
        while (true) {
            String step = scanner.nextLine();
            if (step.equalsIgnoreCase("done")) break;
            steps.add(step);
        }
        Recipe recipe = new Recipe(name, ingredients, steps, calories, tags);
        recipeService.addRecipe(recipe);
        System.out.println("Recipe added successfully!");
    }

    public void editRecipe() {
        System.out.println("\n--- Edit Recipe ---");
        List<Recipe> recipes = recipeService.getAllRecipes();
        if (recipes.isEmpty()) {
            System.out.println("No recipes to edit.");
            return;
        }
        viewAllRecipes();
        System.out.print("Enter recipe number to edit: ");
        int index = getValidatedIntInput() - 1;
        if (index < 0 || index >= recipes.size()) {
            System.out.println("Invalid recipe number.");
            return;
        }
        System.out.print("Enter new recipe name: ");
        String name = scanner.nextLine();
        System.out.print("Enter new calories: ");
        int calories = getValidatedIntInput();
        System.out.print("Enter new tags (comma separated): ");
        String tagsInput = scanner.nextLine();
        Set<String> tags = new HashSet<>(Arrays.asList(tagsInput.split(",\\s*")));
        List<Ingredient> ingredients = recipes.get(index).getIngredients();
        List<String> steps = recipes.get(index).getSteps();
        Recipe updatedRecipe = new Recipe(name, ingredients, steps, calories, tags);
        recipeService.editRecipe(index, updatedRecipe);
        System.out.println("Recipe updated successfully!");
    }

    public void deleteRecipe() {
        System.out.println("\n--- Delete Recipe ---");
        List<Recipe> recipes = recipeService.getAllRecipes();
        if (recipes.isEmpty()) {
            System.out.println("No recipes to delete.");
            return;
        }
        viewAllRecipes();
        System.out.print("Enter recipe number to delete: ");
        int index = getValidatedIntInput() - 1;
        if (index < 0 || index >= recipes.size()) {
            System.out.println("Invalid recipe number.");
            return;
        }
        recipeService.deleteRecipe(index);
        System.out.println("Recipe deleted successfully!");
    }

    public void viewAllRecipes() {
        System.out.println("\n--- All Recipes ---");
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
        viewRecipeDetails(recipes);
    }

    public void searchForRecipe() {
        List<Recipe> recipes = recipeService.getAllRecipes();
        if (recipes.isEmpty()) {
            System.out.println("No recipes found.");
            return;
        }
        System.out.println("\nEnter recipe name to search: ");
        String searchTerm = scanner.nextLine().toLowerCase();
        List<Recipe> matches = new ArrayList<>();
        int recipeNum = 1;
        for (Recipe recipe : recipes) {
            String recipeName = recipe.getName().toLowerCase();
            if (recipeName.contains(searchTerm)) {
                System.out.println(recipeNum + ". " + recipe.getName());
                matches.add(recipe);
                recipeNum++;
            }
        }
        if (matches.isEmpty()) {
            System.out.println("No recipe found matching: " + searchTerm);
            return;
        }
        viewRecipeDetails(matches);
    }

    private void viewRecipeDetails(List<Recipe> recipes) {
        System.out.print("Enter the number of the recipe to view details: ");
        int choice = getValidatedIntInput();
        if (choice > 0 && choice <= recipes.size()) {
            Recipe selected = recipes.get(choice - 1);
            System.out.println("\nRecipe Details: ");
            System.out.println("Name: " + selected.getName());
            System.out.println("Calories: " + selected.getCalories());
            System.out.println("Tags: " + selected.getTags());
            System.out.println("Ingredients: ");
            for (Ingredient ing : selected.getIngredients()) {
                System.out.println(" - " + ing.getName() + " (" + ing.getQuantity() + ") ");
            }
            System.out.println("Steps: ");
            int stepNum = 1;
            for (String step : selected.getSteps()) {
                System.out.println(stepNum + ". " + step);
                stepNum++;
            }
        } else if (choice != 0) {
            System.out.println("Invalid selection");
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

    private List<Ingredient> selectIngredientsForRecipe() {
        List<Ingredient> selectedIngredients = new ArrayList<>();
        while (true) {
            System.out.println("\nChoose ingredients for the recipe:");
            List<Ingredient> allIngredients = ingredientService.getAllIngredients();
            if (!allIngredients.isEmpty()) {
                int ingNum = 1;
                for (Ingredient ing : allIngredients) {
                    System.out.println(ingNum + ". " + ing.getName());
                    ingNum++;
                }
            } else {
                System.out.println("No ingredients available.");
            }
            System.out.println("A. Add new ingredient");
            System.out.println("D. Done selecting ingredients");
            System.out.print("Select an ingredient by number, or choose an option: ");
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("A")) {
                Ingredient newIng = createIngredient();
                ingredientService.addOrUpdateIngredient(newIng);
                selectedIngredients.add(newIng);
            } else if (input.equalsIgnoreCase("D")) {
                if (selectedIngredients.isEmpty()) {
                    System.out.println("Please select at least one ingredient.");
                } else {
                    break;
                }
            } else {
                try {
                    int index = Integer.parseInt(input) - 1;
                    List<Ingredient> all = ingredientService.getAllIngredients();
                    if (index >= 0 && index < all.size()) {
                        Ingredient chosen = all.get(index);
                        if (!selectedIngredients.contains(chosen)) {
                            selectedIngredients.add(chosen);
                        } else {
                            System.out.println("Ingredient already selected.");
                        }
                    } else {
                        System.out.println("Invalid selection.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input.");
                }
            }
        }
        return selectedIngredients;
    }

    private Ingredient createIngredient() {
        System.out.print("Enter ingredient name: ");
        String name = scanner.nextLine();
        System.out.print("Enter ingredient quantity (in gm or unit): ");
        double quantity = scanner.nextDouble();
        System.out.print("Enter calories per unit: ");
        double caloriesPerUnit = scanner.nextDouble();
        System.out.print("Enter protein per unit: ");
        double proteinPerUnit = scanner.nextDouble();
        System.out.print("Enter carbs per unit: ");
        double carbsPerUnit = scanner.nextDouble();
        System.out.print("Enter fat per unit: ");
        double fatPerUnit = scanner.nextDouble();
        scanner.nextLine();
        return new Ingredient(name, quantity, caloriesPerUnit, proteinPerUnit, carbsPerUnit, fatPerUnit);
    }
}