--
-- JBoss, Home of Professional Open Source
-- Copyright 2016, Red Hat, Inc. and/or its affiliates, and individual
-- contributors by the @authors tag. See the copyright.txt in the
-- distribution for a full listing of individual contributors.
--
-- Licensed under the Apache License, Version 2.0 (the "License");
-- you may not use this file except in compliance with the License.
-- You may obtain a copy of the License at
-- http://www.apache.org/licenses/LICENSE-2.0
-- Unless required by applicable law or agreed to in writing, software
-- distributed under the License is distributed on an "AS IS" BASIS,
-- WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
-- See the License for the specific language governing permissions and
-- limitations under the License.
--

BEGIN
INSERT INTO AchievementType (type, description) VALUES ('TEN_CONSEQ', '10 consecutive points');
INSERT INTO AchievementType (type, description) VALUES ('FIFTY_CONSEQ', '50 consecutive points');
INSERT INTO AchievementType (type, description) VALUES ('100_POINTS', '100 points');
INSERT INTO AchievementType (type, description) VALUES ('500_POINTS', '500 points');
INSERT INTO AchievementType (type, description) VALUES ('TOP_SCORER', 'Top scorer');
COMMIT
