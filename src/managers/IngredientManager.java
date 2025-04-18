package managers;

import models.*;
import services.*;
import java.util.*;

public class IngredientManager {
    private final IngredientService ingredientService;
    private final Scanner scanner;

    public IngredientManager(IngredientService ingredientService, Scanner scanner) {
        this.ingredientService = ingredientService;
        this.scanner = scanner;
    }

    public void addIngredient() {
        Ingredient ing = createIngredient();
        ingredientService.addOrUpdateIngredient(ing);
        System.out.println("Ingredient added/updated successfully!");
    }

    public void viewAllIngredients() {
        List<Ingredient> allIngredients = ingredientService.getAllIngredients();
        if (allIngredients.isEmpty()) {
            System.out.println("No ingredients found.");
        } else {
            int i = 1;
            System.out.println("Ingredients:");
            for (Ingredient ingredient : allIngredients) {
                System.out.println(i + ". " + ingredient.getName());
                i++;
            }
        }
        viewIngredientDetails(allIngredients);
    }

    public void searchForIngredient() {
        List<Ingredient> allIngredients = ingredientService.getAllIngredients();
        if (allIngredients.isEmpty()) {
            System.out.println("No ingredients found.");
            return;
        }
        System.out.print("\nEnter ingredient name to search: ");
        String searchTerm = scanner.nextLine().toLowerCase();
        List<Ingredient> matches = new ArrayList<>();
        int ingNum = 1;
        for (Ingredient ingredient : allIngredients) {
            String ingredientName = ingredient.getName().toLowerCase();
            if (ingredientName.contains(searchTerm)) {
                System.out.println(ingNum + ". " + ingredient.getName());
                matches.add(ingredient);
                ingNum++;
            }
        }
        if (matches.isEmpty()) {
            System.out.println("No ingredient found matching: " + searchTerm);
            return;
        }
        viewIngredientDetails(matches);
    }

    public void removeIngredient() {
        List<Ingredient> ingredients = ingredientService.getAllIngredients();
        if (ingredients.isEmpty()) {
            System.out.println("No ingredients to remove.");
            return;
        }
        int ingNum = 1;
        for (Ingredient ingredient : ingredients) {
            System.out.println(ingNum + ". " + ingredient.getName());
            ingNum++;
        }
        System.out.print("Enter ingredient number to remove: ");
        try {
            int index = Integer.parseInt(scanner.nextLine()) - 1;
            if (index >= 0 && index < ingredients.size()) {
                Ingredient ingredientToRemove = ingredients.get(index);
                ingredientService.removeIngredient(ingredientToRemove.getName());
                System.out.println("Ingredient removed successfully!");
            } else {
                System.out.println("Invalid ingredient number.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
        }
    }

    public void ingredientManagementMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- Ingredient Management ---");
            System.out.println("1. Add Ingredient");
            System.out.println("2. View All Ingredients");
            System.out.println("3. Search for Ingredient");
            System.out.println("4. Remove Ingredient");
            System.out.println("0. Back to Main Menu");
            System.out.print("Choose an option: ");
            int choice = getValidatedIntInput();
            switch (choice) {
                case 1:
                    addIngredient();
                    break;
                case 2:
                    viewAllIngredients();
                    break;
                case 3:
                    searchForIngredient();
                    break;
                case 4:
                    removeIngredient();
                    break;
                case 0:
                    back = true;
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private void viewIngredientDetails(List<Ingredient> ingredients) {
        System.out.print("Enter the number of the ingredient to view details: ");
        int choice = getValidatedIntInput();
        if (choice > 0 && choice <= ingredients.size()) {
            Ingredient selected = ingredients.get(choice - 1);
            System.out.println("\nIngredient Details:");
            System.out.println("Name: " + selected.getName());
            System.out.println("Quantity: " + selected.getQuantity());
            System.out.println("Calories per unit: " + selected.getCaloriesPerUnit());
            System.out.println("Protein per unit: " + selected.getProteinPerUnit());
            System.out.println("Carbs per unit: " + selected.getCarbsPerUnit());
            System.out.println("Fat per unit: " + selected.getFatPerUnit());
        } else if (choice != 0) {
            System.out.println("Invalid selection.");
        }
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