package services;

import models.Recipe;
import java.util.*;

public class RecipeService {
    private final List<Recipe> recipes = new ArrayList<>();

    public void addRecipe(Recipe recipe) {
        recipes.add(recipe);
    }

    public void editRecipe(int index, Recipe updatedRecipe) {
        if (index >= 0 && index < recipes.size()) {
            recipes.set(index, updatedRecipe);
        }
    }

    public void deleteRecipe(int index) {
        if (index >= 0 && index < recipes.size()) {
            recipes.remove(index);
        }
    }

    public List<Recipe> getAllRecipes() {
        return new ArrayList<>(recipes);
    }

    public Recipe getRecipe(int index) {
        if (index >= 0 && index < recipes.size()) {
            return recipes.get(index);
        }
        return null;
    }

    public List<Recipe> filterByTags(Set<String> tags) {
        List<Recipe> filtered = new ArrayList<>();
        for (Recipe recipe : recipes) {
            if (recipe.getTags().containsAll(tags)) {
                filtered.add(recipe);
            }
        }
        return filtered;
    }
}