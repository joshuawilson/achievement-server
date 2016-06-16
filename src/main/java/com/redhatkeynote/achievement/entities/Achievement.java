package com.redhatkeynote.achievement.entities;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="achievement")
@Entity
public class Achievement implements Serializable {
    /**
     * Generated Serial Version UID
     */
    private static final long serialVersionUID = -5413646931667094519L;

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    /**
     * The achievement code
     */
    private AchievementType achievementType;

    /**
     * Has the achievement been reached?
     */
    private boolean achieved;

    public AchievementType getAchievementType() {
        return achievementType;
    }

    public void setAchievementType(AchievementType achievementType) {
        this.achievementType = achievementType;
    }

    public boolean isAchieved() {
        return achieved;
    }

    public synchronized void setAchieved(boolean achieved) {
        this.achieved = achieved;
    }
}
