import services.*;
import models.*;
import java.util.*;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final RecipeService recipeService = new RecipeService();
    private static final IngredientService ingredientService = new IngredientService();
    private static final MealPlanService mealPlanService = new MealPlanService();

    public static void main(String[] args) {
        addSampleData();
        boolean exit = false;
        while (!exit) {
            printMainMenu();
            int choice = getValidatedIntInput();
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
                    searchForRecipe();
                    break;
                case 6:
                    ingredientManagementMenu();
                    break;
                case 7:
                    mealPlanManagementMenu();
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

    private static void addRecipe() {
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

    private static void editRecipe() {
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
        int index = getValidatedIntInput() - 1;
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

        int recipeNum = 1;
        for (Recipe recipe : recipes) {
            System.out.println(recipeNum + ". " + recipe.getName());
            recipeNum++;
        }

        viewRecipeDetails(recipes);
    }

    private static void viewRecipeDetails(List<Recipe> recipes) {
        System.out.println("\nEnter the number of the recipe to view details: ");
        
        int choice = getIntInput();

        if(choice > 0 && choice <= recipes.size()){
            Recipe selected = recipes.get(choice - 1);
            System.out.println("\nRecipe Details: ");
            System.out.println("Name: " + selected.getName());
            System.out.println("Calories: " + selected.getCalories());
            System.out.println("Tags: " + selected.getTags());
            System.out.println("Ingredients: ");

            for(Ingredient ing : selected.getIngredients()){
                System.out.println(" - " + ing.getName() + " (" + ing.getQuantity() + ") ");
            }

            System.out.println("Steps: ");
            int stepNum = 1;

            for(String step : selected.getSteps()){
                System.out.println(stepNum + ". " + step);
                stepNum++;
            }
        } else if (choice != 0){
            System.out.println("Invalid selection");
        }
    }

    private static void searchForRecipe(){
        List<Recipe> recipes = recipeService.getAllRecipes();
        if(recipes.isEmpty()){
            System.out.println("No recipes found.");
            return;
        }

        System.out.println("\nEnter recipe name to search: ");
        String searchTerm = scanner.nextLine().toLowerCase();
        List<Recipe> matches = new ArrayList<>();

        int recipeNum = 1;
        for(Recipe recipe : recipes){
            String recipeName = recipe.getName().toLowerCase();
            if(recipeName.contains(searchTerm)){
                System.out.println(recipeNum + ". " + recipe.getName());
                matches.add(recipe);
                recipeNum++;
            }
        };
        
        if(matches.isEmpty()){
            System.out.println("No recipe found matching: " + searchTerm);
            return;
        }

        viewRecipeDetails(matches);
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

    private static List<Ingredient> selectIngredientsForRecipe() {
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

    private static Ingredient createIngredient() {
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

        return new Ingredient(name, quantity, caloriesPerUnit, proteinPerUnit, carbsPerUnit, fatPerUnit);
    }

    private static void addIngredient(){
        Ingredient ing = createIngredient();
        ingredientService.addOrUpdateIngredient(ing);
        System.out.println("Ingredient added/updated successfully!");
    }

    private static void viewAllIngredients(){
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

    private static void viewIngredientDetails(List<Ingredient> ingredients){
        System.out.print("Enter the number of the ingredient to view details: ");
        int choice = getIntInput();

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

    private static void searchForIngredient(){
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

    private static void removeIngredient(){
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

    private static void ingredientManagementMenu() {
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

    private static void mealPlanManagementMenu() {
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
                    generateDailyMealPlan();
                    break;
                case 2:
                    generateWeeklyMealPlan();
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

    private static Set<String> promptForMealPlanTags() {
        System.out.print("Enter required tags (comma separated, or leave blank): ");
        String tagsInput = scanner.nextLine();
        Set<String> requiredTags = new HashSet<>();
        if (!tagsInput.trim().isEmpty()) {
            requiredTags.addAll(Arrays.asList(tagsInput.split(",\\s*")));
        }
        return requiredTags;
    }
    
    private static void displayDailyMealPlan(DailyMealPlan dailyMealPlan, int calorieLimit) {
        System.out.println("\n==== Daily Meal Plan: " + dailyMealPlan.getName() + " ====");

        int totalCalories = utils.CalorieCalculator.calculateTotalCalories(dailyMealPlan.getRecipes());
        System.out.println("Total Calories: " + totalCalories + " (Limit: " + calorieLimit + ")");
        System.out.println("Recipes:");
        List<Recipe> recipes = dailyMealPlan.getRecipes();

        int recipeNum = 1;
        double perRecipeLimit = recipes.isEmpty() ? 0 : (double)calorieLimit / recipes.size();
        for (Recipe recipe : recipes) {
            double portion = utils.CalorieCalculator.recommendPortionSize(recipe, (int)perRecipeLimit);
            System.out.printf("%d. %s (%d cal) - Recommended Portion: %.2f\n", i, recipe.getName(), recipe.getCalories(), portion);
            recipeNum++;
        }

        Set<String> allTags = new HashSet<>();
        for (Recipe recipe : recipes) {
            allTags.addAll(recipe.getTags());
        }
        System.out.println("Tags: " + allTags);
    }

    private static void generateDailyMealPlan() {
        System.out.println("\n--- Generate Daily Meal Plan ---");
        System.out.print("Enter meal plan name: ");
        String name = scanner.nextLine();
        System.out.print("Enter calorie limit: ");
        int calorieLimit = getIntInput();
        Set<String> requiredTags = promptForMealPlanTags();
        List<Recipe> allRecipes = recipeService.getAllRecipes();
        if (allRecipes.isEmpty()) {
            System.out.println("No recipes available to generate a meal plan.");
            return;
        }
        DailyMealPlan dailyMealPlan = mealPlanService.generateDailyMealPlan(name, allRecipes, calorieLimit, requiredTags);
        displayDailyMealPlan(dailyMealPlan, calorieLimit);
    }

    private static void displayWeeklyMealPlan(WeeklyMealPlan weeklyMealPlan, int calorieLimit) {
        List<Recipe> recipes = weeklyMealPlan.getRecipes();
        if (recipes.isEmpty()) {
            System.out.println("No recipes selected for the meal plan.");
            return;
        }
    
        System.out.println("\n==== Weekly Meal Plan: " + weeklyMealPlan.getName() + " ====");
        int totalCalories = utils.CalorieCalculator.calculateTotalCalories(recipes);
        System.out.println("Total Calories: " + totalCalories + " (Limit: " + calorieLimit + ")");
    
        int days = 7;
        int recipesPerDay = Math.max(1, recipes.size() / days); 
        for (int d = 0; d < days; d++) {
            System.out.println("\nDay " + (d + 1) + ":");
            int start = (d * recipesPerDay) % recipes.size(); 
            int end = Math.min(start + recipesPerDay, recipes.size());
            List<Recipe> dayRecipes = recipes.subList(start, end);
            double perRecipeLimit = dayRecipes.isEmpty() ? 0 : (double)calorieLimit / dayRecipes.size();
            int i = 1;
            for (Recipe recipe : dayRecipes) {
                double portion = utils.CalorieCalculator.recommendPortionSize(recipe, (int)perRecipeLimit);
                System.out.printf("  %d. %s (%d cal) - Recommended Portion: %.2f\n", i, recipe.getName(), recipe.getCalories(), portion);
                i++;
            }
        }
        Set<String> allTags = new HashSet<>();
        for (Recipe recipe : recipes) {
            allTags.addAll(recipe.getTags());
        }
        System.out.println("\nTags: " + allTags);
    }

    private static void generateWeeklyMealPlan() {
        System.out.println("\n--- Generate Weekly Meal Plan ---");
        System.out.print("Enter meal plan name: ");
        String name = scanner.nextLine();
        System.out.print("Enter calorie limit: ");
        int calorieLimit = getIntInput();
        Set<String> requiredTags = promptForMealPlanTags();
        List<Recipe> allRecipes = recipeService.getAllRecipes();
        if (allRecipes.isEmpty()) {
            System.out.println("No recipes available to generate a meal plan.");
            return;
        }

        WeeklyMealPlan weeklyMealPlan = mealPlanService.generateWeeklyMealPlan(name, allRecipes, calorieLimit, requiredTags);
        displayWeeklyMealPlan(weeklyMealPlan, calorieLimit);
    }
}

