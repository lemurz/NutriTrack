package models;

import java.util.List;
import java.util.HashSet;
import java.util.Set;

public class WeeklyMealPlan extends MealPlan {
    private List<DailyMealPlan> dailyPlans;

    public WeeklyMealPlan(String name, List<DailyMealPlan> dailyPlans) {
        super(name, null);
        this.dailyPlans = dailyPlans;
    }

    @Override
    public int getCalories() {
        return dailyPlans.stream().mapToInt(DailyMealPlan::getCalories).sum();
    }

    @Override
    public Set<String> getTags() {
        Set<String> tags = new HashSet<>();
        for (DailyMealPlan plan : dailyPlans) {
            tags.addAll(plan.getTags());
        }
        return tags;
    }

    public List<DailyMealPlan> getDailyMealPlans() {
        return dailyPlans;
    }
}