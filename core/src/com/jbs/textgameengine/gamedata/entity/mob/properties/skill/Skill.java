package com.jbs.textgameengine.gamedata.entity.mob.properties.skill;

import com.jbs.textgameengine.gamedata.entity.Entity;
import com.jbs.textgameengine.gamedata.entity.mob.Mob;
import com.jbs.textgameengine.gamedata.entity.mob.action.combat.CombatAction;
import com.jbs.textgameengine.gamedata.world.room.Room;
import com.jbs.textgameengine.screen.gamescreen.GameScreen;
import com.jbs.textgameengine.screen.gamescreen.userinterface.console.line.Line;

import java.util.ArrayList;

public class Skill {
    public Line name;
    public ArrayList<String> nameKeyList;

    public int maxDistance;
    public int minDistance;

    public boolean singleOnly;
    public boolean allOnly;
    public boolean isHealing;

    public Skill(Line name) {
        this.name = name;
        nameKeyList = new ArrayList<>();

        maxDistance = 0;
        minDistance = 0;

        singleOnly = true;
        allOnly = false;
        isHealing = false;
    }

    public void hitMob(Mob targetMob) {
    }

    public int getMaxDistance(Mob parentEntity) {
        if(maxDistance > parentEntity.getMaxViewDistance()) {
            return parentEntity.getMaxViewDistance();
        }

        return maxDistance;
    }

    public int getMinDistance(Mob parentEntity) {
        return minDistance;
    }

    public boolean weaponCheck(Mob parentEntity) {
        return true;
    }

    public boolean ammoCheck(Mob parentEntity) {
        return true;
    }

    public static ArrayList<Entity> getTargetList(Room targetRoom, CombatAction combatAction) {
        ArrayList<Entity> targetList = new ArrayList<>();

        boolean displayDebugData = false;

        boolean combatTargetInRoom = false;
        boolean groupMemberInRoom = false;
        for(Entity mob : combatAction.parentEntity.targetList) {
            if(targetRoom.mobList.contains(mob)) {
                combatTargetInRoom = true;
                break;
            }
        }
        for(Entity mob : combatAction.parentEntity.groupList) {
            if(targetRoom.mobList.contains(mob)) {
                groupMemberInRoom = true;
                break;
            }
        }

        // Self Check //
        if(combatAction.skill.isHealing) {
            if(combatAction.actionType.equals("CombatAction")) {

                // No Combat Targets Or No Combat Targets In Room //
                if(combatAction.parentEntity.targetList.isEmpty()
                || !combatTargetInRoom) {
                    targetList.add(combatAction.parentEntity);
                }

                // Has Combat Targets In Room //
                else {
                    if(combatAction.skill.allOnly) {
                        targetList.add(combatAction.parentEntity);
                    }
                }
            }

            else if(combatAction.actionType.equals("CombatAction Entity/Self") && !combatAction.selfCheck) {
                if(combatAction.skill.allOnly) {
                    targetList.add(combatAction.parentEntity);
                }
            }

            else if(combatAction.actionType.equals("CombatAction Entity/Self") && combatAction.selfCheck) {
                targetList.add(combatAction.parentEntity);
            }

            else if(combatAction.actionType.equals("CombatAction Direction")) {} // Do Nothing

            else if(combatAction.actionType.equals("CombatAction All/Group") && combatAction.allCheck) {
                if(combatAction.skill.singleOnly && targetRoom.mobList.isEmpty()) {
                    targetList.add(combatAction.parentEntity);
                }
            }

            else if(combatAction.actionType.equals("CombatAction All/Group") && combatAction.groupCheck) {
                if(combatAction.skill.singleOnly && !groupMemberInRoom) {
                    targetList.add(combatAction.parentEntity);
                }
            }

            else if(combatAction.actionType.equals("CombatAction Direction #")) {} // Do Nothing

            else if(combatAction.actionType.equals("CombatAction Entity Direction")) {} // Do Nothing

            else if(combatAction.actionType.equals("CombatAction All/Group Direction") && combatAction.allCheck) {} // Do Nothing

            else if(combatAction.actionType.equals("CombatAction All/Group Direction") && combatAction.groupCheck) {} // Do Nothing

            else if(combatAction.actionType.equals("CombatAction Entity Direction #")) {} // Do Nothing

            else if(combatAction.actionType.equals("CombatAction All/Group Direction #") && combatAction.allCheck) {} // Do Nothing

            else if(combatAction.actionType.equals("CombatAction All/Group Direction #") && combatAction.groupCheck) {} // Do Nothing

            // Break Check //
            if(!targetList.isEmpty()) {
                if(combatAction.skill.singleOnly
                || (!combatAction.skill.allOnly && combatAction.selfCheck)
                || (!combatAction.skill.allOnly && !combatTargetInRoom && combatAction.actionType.equals("CombatAction"))) {
                    if(displayDebugData) {
                        System.out.println("\n----===[ Only Player ]===----");
                    }
                    return targetList;
                }
            }
        }

        // Mobs In Room Check //
        ArrayList<Entity> combinedMobList = new ArrayList<>();
        ArrayList<Entity> checkedList = new ArrayList<>();
        if(!combatAction.parentEntity.isPlayer) {
            combinedMobList.add(GameScreen.player);
        }
        combinedMobList.addAll(combatAction.parentEntity.groupList);
        combinedMobList.addAll(targetRoom.mobList);

        for(Entity mob : combinedMobList) {
            if(mob.location.room == targetRoom && !checkedList.contains(mob)
            && !(!combatAction.parentEntity.isPlayer && mob == combatAction.parentEntity)) {
                checkedList.add(mob);
                int currentSize = targetList.size();

                if(combatAction.actionType.equals("CombatAction")) {
                    if(combatAction.parentEntity.location.room == targetRoom
                    && !combatAction.parentEntity.groupList.contains(mob)) {
                        targetList.add(mob);
                    }

                    else if(combatAction.parentEntity.combatList.isEmpty()
                    && !combatAction.parentEntity.groupList.contains(mob)
                    && !combatAction.skill.isHealing
                    && combatAction.skill.allOnly) {
                        targetList.add(mob);
                    }

                    else if(combatAction.parentEntity.combatList.isEmpty()
                    && combatAction.parentEntity.groupList.contains(mob)
                    && combatAction.skill.isHealing
                    && combatAction.skill.allOnly) {
                        targetList.add(mob);
                    }

                    else if(!combatAction.parentEntity.targetList.isEmpty()
                    && combatAction.parentEntity.location.room != targetRoom
                    && !combatAction.parentEntity.groupList.contains(mob)) {
                        targetList.add(mob);
                    }
                }

                else if(combatAction.actionType.equals("CombatAction Entity/Self") && !combatAction.selfCheck) {
                    if((mob.nameKeyList.contains(combatAction.targetEntityString) || combatAction.skill.allOnly)
                    && !(!combatAction.skill.isHealing && combatAction.parentEntity.groupList.contains(mob))) {
                        targetList.add(mob);
                    }
                }

                else if(combatAction.actionType.equals("CombatAction Entity/Self") && combatAction.selfCheck) {
                    if(combatAction.skill.allOnly
                    && combatAction.parentEntity.groupList.contains(mob)) {
                        targetList.add(mob);
                    }
                }

                else if(combatAction.actionType.equals("CombatAction Direction")) {
                    if(!combatAction.skill.singleOnly
                    && !combatAction.skill.isHealing
                    && !combatAction.parentEntity.groupList.contains(mob)) {
                        targetList.add(mob);
                    }

                    else if(combatAction.skill.singleOnly
                    && !combatAction.skill.isHealing
                    && !combatAction.parentEntity.groupList.contains(mob)
                    && !combatTargetInRoom
                    && targetRoom.mobList.contains(mob)) {
                        targetList.add(mob);
                    }

                    else if(combatAction.skill.singleOnly
                    && combatAction.skill.isHealing
                    && !combatAction.parentEntity.groupList.contains(mob)
                    && !combatTargetInRoom
                    && targetRoom.mobList.contains(mob)) {
                        targetList.add(mob);
                    }

                    else if(combatAction.parentEntity.combatList.isEmpty()
                    && combatAction.parentEntity.groupList.contains(mob)
                    && combatAction.skill.isHealing
                    && !combatAction.skill.singleOnly) {
                        targetList.add(mob);
                    }
                }

                else if(combatAction.actionType.equals("CombatAction All/Group") && combatAction.allCheck) {
                    if(!(!combatAction.skill.isHealing && combatAction.parentEntity.groupList.contains(mob))) {
                        targetList.add(mob);
                    }
                }

                else if(combatAction.actionType.equals("CombatAction All/Group") && combatAction.groupCheck) {
                    if(combatAction.skill.isHealing
                    && combatAction.parentEntity.groupList.contains(mob)) {
                        targetList.add(mob);
                    }
                }

                else if(combatAction.actionType.equals("CombatAction Direction #")) {
                    if(!combatAction.skill.singleOnly
                    && !combatAction.skill.isHealing
                    && !combatAction.parentEntity.groupList.contains(mob)) {
                        targetList.add(mob);
                    }

                    else if(combatAction.skill.singleOnly
                    && !combatAction.skill.isHealing
                    && !combatAction.parentEntity.groupList.contains(mob)
                    && !combatTargetInRoom
                    && targetRoom.mobList.contains(mob)) {
                        targetList.add(mob);
                    }

                    else if(combatAction.skill.singleOnly
                    && combatAction.skill.isHealing
                    && !combatAction.parentEntity.groupList.contains(mob)
                    && !combatTargetInRoom
                    && targetRoom.mobList.contains(mob)) {
                        targetList.add(mob);
                    }

                    else if(combatAction.parentEntity.combatList.isEmpty()
                    && combatAction.parentEntity.groupList.contains(mob)
                    && combatAction.skill.isHealing
                    && !combatAction.skill.singleOnly) {
                        targetList.add(mob);
                    }
                }

                else if(combatAction.actionType.equals("CombatAction Entity Direction")) {
                    if((mob.nameKeyList.contains(combatAction.targetEntityString) || combatAction.skill.allOnly)
                    && !(!combatAction.skill.isHealing && combatAction.parentEntity.groupList.contains(mob))) {
                        targetList.add(mob);
                    }
                }

                else if(combatAction.actionType.equals("CombatAction All/Group Direction") && combatAction.allCheck) {
                    if(!(!combatAction.skill.isHealing && combatAction.parentEntity.groupList.contains(mob))) {
                        targetList.add(mob);
                    }
                }

                else if(combatAction.actionType.equals("CombatAction All/Group Direction") && combatAction.groupCheck) {
                    if(combatAction.skill.isHealing
                    && combatAction.parentEntity.groupList.contains(mob)) {
                        targetList.add(mob);
                    }
                }

                else if(combatAction.actionType.equals("CombatAction Entity Direction #")) {
                    if((mob.nameKeyList.contains(combatAction.targetEntityString) || combatAction.skill.allOnly)
                    && !(!combatAction.skill.isHealing && combatAction.parentEntity.groupList.contains(mob))) {
                        targetList.add(mob);
                    }
                }

                else if(combatAction.actionType.equals("CombatAction All/Group Direction #") && combatAction.allCheck) {
                    if(!(!combatAction.skill.isHealing && combatAction.parentEntity.groupList.contains(mob))) {
                        targetList.add(mob);
                    }
                }

                else if(combatAction.actionType.equals("CombatAction All/Group Direction #") && combatAction.groupCheck) {
                    if(combatAction.skill.isHealing
                    && combatAction.parentEntity.groupList.contains(mob)) {
                        targetList.add(mob);
                    }
                }

                // Break Check //
                if(targetList.size() > currentSize) {
                    if(combatAction.skill.singleOnly
                    || (!combatAction.skill.allOnly && !combatAction.targetEntityString.isEmpty())) {
                        break;
                    }
                }
            }
        }

        // Debug Output //
        if(displayDebugData) {
            System.out.println("\n----===[ Target List ]===----");
            for(Entity mob : targetList) {
                System.out.println(mob.name.label + ", " + combatAction.parentEntity.groupList.contains(mob));
            }
        }

        return targetList;
    }

    public String toString() {
        return getClass().toString().substring(getClass().toString().lastIndexOf(".") + 1);
    }
}
