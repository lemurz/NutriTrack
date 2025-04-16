package models;

import java.util.List;
import interfaces.CalorieTrackable;
import interfaces.Taggable;

public abstract class MealPlan implements CalorieTrackable, Taggable {
    protected List<Recipe> recipes;
    protected String name;

    public MealPlan(String name, List<Recipe> recipes) {
        this.name = name;
        this.recipes = recipes;
    }

    public String getName() {
        return name;
    }

    public List<Recipe> getRecipes() {
        return recipes;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRecipes(List<Recipe> recipes) {
        this.recipes = recipes;
    }
}