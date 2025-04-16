package models;

public class Ingredient {
    private String name;
    private double quantity; // in grams or units
    private double caloriesPerUnit;
    private double proteinPerUnit;
    private double carbsPerUnit;
    private double fatPerUnit;

    public Ingredient(String name, double quantity, double caloriesPerUnit, double proteinPerUnit, double carbsPerUnit, double fatPerUnit) {
        this.name = name;
        this.quantity = quantity;
        this.caloriesPerUnit = caloriesPerUnit;
        this.proteinPerUnit = proteinPerUnit;
        this.carbsPerUnit = carbsPerUnit;
        this.fatPerUnit = fatPerUnit;
    }

    public String getName() {
        return name;
    }

    public double getQuantity() {
        return quantity;
    }

    public double getCaloriesPerUnit() {
        return caloriesPerUnit;
    }

    public double getProteinPerUnit() {
        return proteinPerUnit;
    }

    public double getCarbsPerUnit() {
        return carbsPerUnit;
    }

    public double getFatPerUnit() {
        return fatPerUnit;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public void setCaloriesPerUnit(double caloriesPerUnit) {
        this.caloriesPerUnit = caloriesPerUnit;
    }

    public void setProteinPerUnit(double proteinPerUnit) {
        this.proteinPerUnit = proteinPerUnit;
    }

    public void setCarbsPerUnit(double carbsPerUnit) {
        this.carbsPerUnit = carbsPerUnit;
    }

    public void setFatPerUnit(double fatPerUnit) {
        this.fatPerUnit = fatPerUnit;
    }
}