package csv;

import services.IngredientService;
import services.RecipeService;
import models.Ingredient;
import models.Recipe;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Arrays;
import java.util.HashSet;

public class CSVReader {
    private final IngredientService ingredientService;
    private final RecipeService recipeService;

    public CSVReader(IngredientService ingredientService, RecipeService recipeService) {
        this.ingredientService = ingredientService;
        this.recipeService = recipeService;
    }

    public void importDataFromCSV(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] fields = line.split(",");
                if (fields[0].equalsIgnoreCase("ingredient")) {
                    Ingredient ingredient = parseIngredient(fields);
                    ingredientService.addOrUpdateIngredient(ingredient);
                } else if (fields[0].equalsIgnoreCase("recipe")) {
                    Recipe recipe = parseRecipe(fields);
                    recipeService.addRecipe(recipe);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading CSV file: " + e.getMessage());
        }
    }

    private Ingredient parseIngredient(String[] fields) {
        return new Ingredient(fields[1], Integer.parseInt(fields[2]), Integer.parseInt(fields[3]),
                Double.parseDouble(fields[4]), Integer.parseInt(fields[5]), Integer.parseInt(fields[6]));
    }

    private Recipe parseRecipe(String[] fields) {
        List<Ingredient> ingredients = parseIngredients(fields[2]);
        List<String> steps = parseSteps(fields[3]);
        Set<String> tags = parseTags(fields[4]);
        return new Recipe(fields[1], ingredients, steps, Integer.parseInt(fields[5]), tags);
    }

    private List<Ingredient> parseIngredients(String field) {
        List<Ingredient> ingredients = new ArrayList<>();
        String[] ingredientFields = field.split(";");
        for (String ingredientField : ingredientFields) {
            String[] details = ingredientField.split(":");
            ingredients.add(new Ingredient(
                details[0], 
                Double.parseDouble(details[1]), 
                Double.parseDouble(details[2]), 
                Double.parseDouble(details[3]),
                Double.parseDouble(details[4]), 
                Double.parseDouble(details[5])
            ));
        }
        return ingredients;
    }

    private List<String> parseSteps(String field) {
        return Arrays.asList(field.split(";"));
    }

    private Set<String> parseTags(String field) {
        return new HashSet<>(Arrays.asList(field.split(";")));
    }
}