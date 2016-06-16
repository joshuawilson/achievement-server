package com.redhatkeynote.achievement;

import java.util.List;

import com.redhatkeynote.achievement.entities.Achievement;
import com.redhatkeynote.achievement.entities.AchievementType;

public interface AchievementService {
    /**
     * Return the list of achievement types
     * @return the list of achievement types
     */
    public List<AchievementType> achievementTypes();

    /**
     * Reset all the achievements
     */
    public void reset();

    /**
     * Get the achievements for a specific user
     * @param uuid The user's uuis
     * @return The current list of achievements
     */
    public List<Achievement> achievements(String uuid);

    /**
     * Update an achievement
     * @param uuid The user uuid
     * @param achievementType The achievement type
     * @return The current value of the achievement or null if it doesn't exist
     */
    public Achievement updateAchievement(String uuid, String achievementType);
}
