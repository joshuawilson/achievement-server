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
package com.redhatkeynote.achievement;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.redhatkeynote.achievement.entities.Achievement;
import com.redhatkeynote.achievement.entities.AchievementType;

import io.swagger.annotations.ApiOperation;

// TODO update body of methods to include error checking and to use Response for returning results

@Path("/")
public class AchievementResource {

    @Inject
    AchievementService achievementService;

    @DELETE
    @Path("/reset")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation("Resets the achievements for all players")
    public void reset() {
        achievementService.reset();
    }

    @GET
    @Path("/achievement")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation("Returns the possible list of Achievements")
    public List<AchievementType> achievementTypes() {
        return achievementService.achievementTypes();
    }

    @GET
    @Path("/achievement/{uuid}")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation("Returns the list of Achievements for the specified user")
    public List<Achievement> achievements(@PathParam("uuid") String uuid) {
        return achievementService.achievements(uuid);
    }

    @PUT
    @Path("/achievement/{uuid}/{achievement}")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation("Updates the achievement for the specified user")
    public Achievement updateAchievement(@PathParam("uuid") String uuid, @PathParam("achievement") String achievement) {
        return achievementService.updateAchievement(uuid, achievement);
    }


    @GET
    @Path("/health")
    @Produces(MediaType.TEXT_PLAIN)
    @ApiOperation("Used to verify the health of the service")
    public String health() {
        return "Service is running";
    }
}
