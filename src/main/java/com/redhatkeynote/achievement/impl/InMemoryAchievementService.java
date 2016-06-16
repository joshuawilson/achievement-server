/**
 * JBoss, Home of Professional Open Source
 * Copyright 2016, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.redhatkeynote.achievement.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.enterprise.inject.Alternative;

import com.redhatkeynote.achievement.AchievementService;
import com.redhatkeynote.achievement.entities.Achievement;
import com.redhatkeynote.achievement.entities.AchievementType;

/**
 * In memory achievement service
 * @author kevin
 */
@Alternative
public class InMemoryAchievementService implements AchievementService {

    private static final List<AchievementType> ACHIEVEMENT_TYPES;

    private static final Map<String, Map<String, Achievement>> USER_ACHIEVEMENTS = new HashMap<String, Map<String, Achievement>>();

    /* (non-Javadoc)
     * @see com.redhatkeynote.achievement.impl.AchievementResource#achievementTypes()
     */
    @Override
    public List<AchievementType> achievementTypes() {
        return ACHIEVEMENT_TYPES;
    }

    /* (non-Javadoc)
     * @see com.redhatkeynote.achievement.impl.AchievementResource#reset()
     */
    @Override
    public void reset() {
        USER_ACHIEVEMENTS.clear();
    }

    /* (non-Javadoc)
     * @see com.redhatkeynote.achievement.impl.AchievementResource#achievements(java.lang.String)
     */
    @Override
    public List<Achievement> achievements(final String uuid) {
        Map<String, Achievement> current = USER_ACHIEVEMENTS.get(uuid);
        if (current == null) {
            current = addNewAchievements(uuid);
        }
        return new ArrayList<Achievement>(current.values());
    }

    /* (non-Javadoc)
     * @see com.redhatkeynote.achievement.impl.AchievementResource#updateAchievement(java.lang.String, java.lang.String)
     */
    @Override
    public Achievement updateAchievement(String uuid, String achievementType) {
        Map<String, Achievement> current = USER_ACHIEVEMENTS.get(uuid);
        if (current == null) {
            current = addNewAchievements(uuid);
        }
        final Achievement achievement = current.get(achievementType);
        if (achievement != null) {
            achievement.setAchieved(true);
        }
        return achievement;
    }

    private Map<String, Achievement> addNewAchievements(String uuid) {
        ConcurrentHashMap<String, Achievement> newAchievements = new ConcurrentHashMap<String, Achievement>();
        for(AchievementType type: ACHIEVEMENT_TYPES) {
            final Achievement achievement = new Achievement();
            achievement.setAchievementType(type);
            newAchievements.put(type.getType(), achievement);
        }
        Map<String, Achievement> previous = USER_ACHIEVEMENTS.putIfAbsent(uuid, newAchievements);
        return (previous == null ? newAchievements : previous);
    }

    private static void addAchievementType(final List<AchievementType> achievementTypes, final String achievementType, final String description) {
        final AchievementType type = new AchievementType();
        type.setType(achievementType);
        type.setDescription(description);

        achievementTypes.add(type);
    }

    static {
        final List<AchievementType> achievementTypes = new ArrayList<AchievementType>();

        addAchievementType(achievementTypes, "TEN_CONSEQ", "10 consecutive points");
        addAchievementType(achievementTypes, "FIFTY_CONSEQ", "50 consecutive points");
        addAchievementType(achievementTypes, "100_POINTS", "100 points");
        addAchievementType(achievementTypes, "500_POINTS", "500 points");
        addAchievementType(achievementTypes, "TOP_SCORER", "Top scorer");

        ACHIEVEMENT_TYPES = achievementTypes;
    }
}
