package com.redhatkeynote.achievement.entities;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="achievementType")
@Entity
public class AchievementType implements Serializable {
    /**
     * Generated Serial Version UID
     */
    private static final long serialVersionUID = 242962406891964434L;

    /**
     * The achievement type
     */
    @Id
    private String type;

    /**
     * The description of the achievement
     */
    private String description;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
