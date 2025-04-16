package services;

import models.Ingredient;
import java.util.*;

public class IngredientService {
    private final Map<String, Ingredient> ingredientMap = new HashMap<>();

    public void addOrUpdateIngredient(Ingredient ingredient) {
        ingredientMap.put(ingredient.getName().toLowerCase(), ingredient);
    }

    public Ingredient getIngredient(String name) {
        return ingredientMap.get(name.toLowerCase());
    }

    public List<Ingredient> getAllIngredients() {
        return new ArrayList<>(ingredientMap.values());
    }

    public void removeIngredient(String name) {
        ingredientMap.remove(name.toLowerCase());
    }
}