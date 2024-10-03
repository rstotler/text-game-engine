package com.jbs.textgameengine.gamedata.entity.mob.properties.skill;

import com.jbs.textgameengine.gamedata.entity.Entity;
import com.jbs.textgameengine.gamedata.entity.mob.Mob;
import com.jbs.textgameengine.gamedata.entity.mob.action.combat.CombatAction;
import com.jbs.textgameengine.gamedata.world.room.Room;

import java.util.ArrayList;

public class Skill {
    public ArrayList<String> nameKeyList;

    public int maxDistance;
    public int minDistance;

    public boolean singleOnly;
    public boolean allOnly;
    public boolean isHealing;

    public Skill() {
        nameKeyList = new ArrayList<>();

        maxDistance = 0;
        minDistance = 0;

        singleOnly = true;
        allOnly = false;
        isHealing = false;
    }

    public int getMaxDistance(Mob parentEntity) {
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

    public ArrayList<Entity> getTargetList(Room targetRoom, CombatAction combatAction) {
        ArrayList<Entity> targetList = new ArrayList<>();

        // Target Entity In Group & Target Combat Entity In Room Checks //
        boolean targetEntityInGroup = false;
        boolean targetCombatEntityInRoom = false;
        for(Entity mob : combatAction.parentEntity.groupList) {
            if((mob.nameKeyList.contains(combatAction.targetEntityString) || combatAction.groupCheck)
            && targetRoom.mobList.contains(mob)) {
                targetEntityInGroup = true;
                break;
            }
        }
        for(Entity mob : combatAction.parentEntity.targetList) {
            if(targetRoom.mobList.contains(mob)) {
                targetCombatEntityInRoom = true;
                break;
            }
        }

        // Self Check //
        if(isHealing && combatAction.parentEntity.location.room == targetRoom
        && !(singleOnly && !combatAction.parentEntity.targetList.isEmpty() && targetRoom.mobList.contains(combatAction.parentEntity.targetList.get(0)))
        && (allOnly
        || combatAction.selfCheck
        || (combatAction.groupCheck && !(singleOnly && !combatAction.parentEntity.groupList.isEmpty() && targetRoom.mobList.contains(combatAction.parentEntity.groupList.get(0))))
        || (combatAction.allCheck)
        || (!combatAction.selfCheck && !combatAction.groupCheck && !combatAction.allCheck && combatAction.targetEntityString.isEmpty() && !targetCombatEntityInRoom)
        || (!combatAction.selfCheck && !combatAction.groupCheck && !combatAction.allCheck && combatAction.targetEntityString.isEmpty() && targetCombatEntityInRoom && !singleOnly))) {
            targetList.add(combatAction.parentEntity);

            if(singleOnly
            || (!allOnly && combatAction.selfCheck)
            || ((!allOnly && combatAction.targetEntityString.isEmpty()) && !(isHealing && (combatAction.groupCheck || combatAction.allCheck || (targetCombatEntityInRoom && !singleOnly))))) {
                // System.out.println("\n---------===[ Only Player ]===---------");
                return targetList;
            }
        }

        // Room Entities (Checks Group Mobs First For Healing Spells) //
        // System.out.println("\n---------===[ Room: " + targetRoom.name.label + " ]===---------");
        ArrayList<Entity> combinedMobList = new ArrayList<>();
        ArrayList<Entity> checkedList = new ArrayList<>();
        combinedMobList.addAll(combatAction.parentEntity.groupList);
        combinedMobList.addAll(targetRoom.mobList);

        for(Entity mob : combinedMobList) {
            if(mob.location.room == targetRoom
            && !checkedList.contains(mob)) {
                checkedList.add(mob);

                if((!isHealing && !combatAction.parentEntity.groupList.contains(mob)
                && (allOnly
                || (singleOnly && (combatAction.allCheck || mob.nameKeyList.contains(combatAction.targetEntityString) || (combatAction.targetEntityString.isEmpty() && combatAction.parentEntity.targetList.contains(mob))))
                || (!singleOnly && !allOnly && (combatAction.allCheck || mob.nameKeyList.contains(combatAction.targetEntityString) || (combatAction.targetEntityString.isEmpty() && combatAction.parentEntity.targetList.contains(mob))))))

                || (isHealing
                && ((allOnly && combatAction.allCheck)
                || (allOnly && combatAction.groupCheck && combatAction.parentEntity.groupList.contains(mob))
                || (allOnly && combatAction.selfCheck && combatAction.parentEntity.groupList.contains(mob))
                || (allOnly && !combatAction.allCheck && !combatAction.groupCheck && !combatAction.selfCheck && combatAction.parentEntity.groupList.contains(mob))
                || (allOnly && !combatAction.targetEntityString.isEmpty() && !targetEntityInGroup)
                || (allOnly && !combatAction.allCheck && !combatAction.groupCheck && !combatAction.selfCheck && combatAction.targetEntityString.isEmpty() && !combatAction.targetDirection.isEmpty() && !targetEntityInGroup)
                || (allOnly && !combatAction.allCheck && !combatAction.groupCheck && !combatAction.selfCheck && combatAction.targetEntityString.isEmpty() && combatAction.targetDirection.isEmpty() && ((combatAction.parentEntity.targetList.isEmpty() && targetEntityInGroup) || targetCombatEntityInRoom))
                || (singleOnly && combatAction.allCheck)
                || (singleOnly && combatAction.groupCheck && combatAction.parentEntity.groupList.contains(mob))
                || (singleOnly && mob.nameKeyList.contains(combatAction.targetEntityString))
                || (singleOnly && combatAction.parentEntity.targetList.contains(mob))
                || (singleOnly && combatAction.targetEntityString.isEmpty() && combatAction.targetDirection.isEmpty() && combatAction.parentEntity.targetList.contains(mob))
                || (!allOnly && !singleOnly && combatAction.allCheck)
                || (!allOnly && !singleOnly && combatAction.groupCheck && combatAction.parentEntity.groupList.contains(mob))
                || (!allOnly && !singleOnly && mob.nameKeyList.contains(combatAction.targetEntityString))
                || (!allOnly && !singleOnly && targetCombatEntityInRoom && !combatAction.groupCheck)))) {
                    targetList.add(mob);

                    if(singleOnly
                    || (!singleOnly && !allOnly && combatAction.selfCheck)
                    || (!singleOnly && !allOnly && !combatAction.targetEntityString.isEmpty())) {
                        break;
                    }
                }
            }
        }

        // Debug Output //
        // for(Entity mob : targetList) {
        //     if(mob.isPlayer) {System.out.println("Player");}
        //     else {System.out.println(mob.name.label);}
        // }

        return targetList;
    }

    public String toString() {
        return getClass().toString().substring(getClass().toString().lastIndexOf(".") + 1);
    }
}
