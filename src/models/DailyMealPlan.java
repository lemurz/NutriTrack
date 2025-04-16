package models;

import java.util.List;
import java.util.HashSet;
import java.util.Set;

public class DailyMealPlan extends MealPlan {
    public DailyMealPlan(String name, List<Recipe> recipes) {
        super(name, recipes);
    }

    @Override
    public int getCalories() {
        return recipes.stream().mapToInt(Recipe::getCalories).sum();
    }

    @Override
    public Set<String> getTags() {
        Set<String> tags = new HashSet<>();
        for (Recipe recipe : recipes) {
            tags.addAll(recipe.getTags());
        }
        return tags;
    }
}