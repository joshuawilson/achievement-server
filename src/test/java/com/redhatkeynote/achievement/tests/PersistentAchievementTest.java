/*
 * JBoss, Home of Professional Open Source
 * Copyright 2015, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
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
package com.redhatkeynote.achievement.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.redhatkeynote.achievement.AchievementService;
import com.redhatkeynote.achievement.entities.Achievement;
import com.redhatkeynote.achievement.entities.AchievementType;
import com.redhatkeynote.achievement.entities.Player;
import com.redhatkeynote.achievement.impl.CDIResources;
import com.redhatkeynote.achievement.impl.InMemoryAchievementService;
import com.redhatkeynote.achievement.impl.PersistentAchievementService;

@RunWith(Arquillian.class)
public class PersistentAchievementTest {

    @Deployment
    public static WebArchive deployment() throws IllegalArgumentException, FileNotFoundException {
        final WebArchive webArchive = ShrinkWrap.create(WebArchive.class, "test.war");
        webArchive.addAsWebInfResource(new File("src/main/webapp", "WEB-INF/beans.xml"));
        webArchive.addAsResource("META-INF/persistence.xml");
        webArchive.addAsWebInfResource("test-ds.xml");
        webArchive.addAsResource("initial_data.sql");
        webArchive.addClasses(AchievementService.class);
        webArchive.addClasses(Achievement.class, AchievementType.class, Player.class);
        webArchive.addClasses(CDIResources.class, InMemoryAchievementService.class, PersistentAchievementService.class);

        return webArchive;
    }

    @Inject
    private AchievementService achievementService;

    @Test
    @InSequence(1)
    public void achievementTypesShouldBePopulated() throws Exception {
        List<AchievementType> achievementTypes = achievementService.achievementTypes();

        assertNotNull("Achievement Types should not be null", achievementTypes);
        assertEquals("Should return 5 Achievement Types", 5, achievementTypes.size());
    }

    @Test
    @InSequence(2)
    public void retrieveAchievementsForPlayer() {
        List<Achievement> achievements = achievementService.achievements("1234567890");

        assertNotNull("Achievements should not be null", achievements);
        assertEquals("Should return 5 Achievements", 5, achievements.size());

        for(Achievement achievement : achievements) {
            assertNotNull("Achievement should not be null", achievement);
            assertFalse("Achievement should not be achieved", achievement.isAchieved());
        }
    }

    @Test
    @InSequence(3)
    public void updateAchievementsForPlayer() {
        updateAchievementsForPlayer("9999999999");
    }

    @Test
    @InSequence(4)
    public void updateThenResetAchievementsForPlayer() {
        final String uuid = "XXXXXXXXXX";
        updateAchievementsForPlayer(uuid);

        achievementService.reset();
        List<Achievement> achievements = achievementService.achievements(uuid);

        assertNotNull("Achievements should not be null", achievements);
        assertEquals("Should return 5 Achievements", 5, achievements.size());

        for(Achievement achievement: achievements) {
            assertFalse("Achievement should not be achieved", achievement.isAchieved());
        }
    }

    private void updateAchievementsForPlayer(final String uuid) {
        List<AchievementType> achievementTypes = achievementService.achievementTypes();
        assertNotNull("Achievement Types should not be null", achievementTypes);
        assertTrue("Should return at least one Achievement Type", achievementTypes.size() >= 1);

        AchievementType achievementType = achievementTypes.get(0);
        assertNotNull("Achievement Type should not be null", achievementType);

        Achievement updatedAchievement = achievementService.updateAchievement(uuid, achievementType.getType());
        assertNotNull("Updated Achievement should not be null", updatedAchievement);
        assertEquals("AchievementType should match", achievementType.getType(), updatedAchievement.getAchievementType().getType());
        assertTrue("Achievement should be achieved", updatedAchievement.isAchieved());

        List<Achievement> achievements = achievementService.achievements(uuid);
        Achievement found = null;
        for(Achievement achievement: achievements) {
            if (achievementType.getType().equals(achievement.getAchievementType().getType())) {
                found = achievement ;
                break;
            }
        }
        assertNotNull("Achievement should be found", found);
        assertTrue("Achievement should be achieved", found.isAchieved());
    }
}
