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
import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaDelete;

import com.redhatkeynote.achievement.AchievementService;
import com.redhatkeynote.achievement.entities.Achievement;
import com.redhatkeynote.achievement.entities.AchievementType;
import com.redhatkeynote.achievement.entities.Player;

/**
 * Persistent achievement service
 * @author kevin
 */
@Stateless
@Local(value=AchievementService.class)
public class PersistentAchievementService implements AchievementService {

    @PersistenceContext
    private EntityManager em;

    /**
     * Return the list of achievement types
     * @return the list of achievement types
     */
    public List<AchievementType> achievementTypes() {
        return em.createQuery("SELECT at FROM AchievementType at", AchievementType.class).getResultList();
    }

    /**
     * Reset all the achievements
     */
    public void reset() {
        CriteriaDelete<Player> criteria = em.getCriteriaBuilder().createCriteriaDelete(Player.class);
        criteria.from(Player.class);
        em.createQuery(criteria).executeUpdate();
    }

    /**
     * Get the achievements for a specific player
     * @param uuid The player's uuis
     * @return The current list of achievements
     */
    public List<Achievement> achievements(final String uuid) {
        final Player player = getPlayer(uuid);
        return player.getAchievements();
    }

    /**
     * Update an achievement
     * @param uuid The player uuid
     * @param achievementType The achievement type
     * @return The current value of the achievement or null if it doesn't exist
     */
    public Achievement updateAchievement(String uuid, String achievementType) {
        final Player player = getPlayer(uuid);
        for (Achievement achievement: player.getAchievements()) {
            if (achievement.getAchievementType().getType().equals(achievementType)) {
                achievement.setAchieved(true);
                return achievement;
            }
        }
        return null;
    }

    private Player getPlayer(String uuid) {
        final List<Player> results = em.createQuery("SELECT p FROM Player p WHERE p.player = ?1", Player.class).setParameter(1, uuid).getResultList();
        final Player player ;
        if (results.isEmpty()) {
            player = new Player(uuid);
            List<AchievementType> achievementTypes = achievementTypes();
            final List<Achievement> achievements = new ArrayList<Achievement>();
            for(AchievementType achievementType: achievementTypes) {
                final Achievement achievement = new Achievement();
                achievement.setAchievementType(achievementType);
                achievements.add(achievement);
            }
            player.setAchievements(achievements); 
            em.persist(player);
        } else {
            player = results.get(0);
        }
        return player;
    }
}
