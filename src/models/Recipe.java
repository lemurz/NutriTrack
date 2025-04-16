package models;

import java.util.List;
import java.util.Set;
import interfaces.CalorieTrackable;
import interfaces.Taggable;

public class Recipe implements CalorieTrackable, Taggable {
    private String name;
    private List<Ingredient> ingredients;
    private List<String> steps;
    private int calories;
    private Set<String> tags;

    public Recipe(String name, List<Ingredient> ingredients, List<String> steps, int calories, Set<String> tags) {
        this.name = name;
        this.ingredients = ingredients;
        this.steps = steps;
        this.calories = calories;
        this.tags = tags;
    }

    public String getName() {
        return name;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public List<String> getSteps() {
        return steps;
    }

    public int getCalories() {
        return calories;
    }

    @Override
    public Set<String> getTags() {
        return tags;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public void setSteps(List<String> steps) {
        this.steps = steps;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public void setTags(Set<String> tags) {
        this.tags = tags;
    }
}