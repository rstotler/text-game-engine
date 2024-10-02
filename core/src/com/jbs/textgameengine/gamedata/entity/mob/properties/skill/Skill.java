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

        // Target Entity In Group Check //
        boolean targetEntityInGroup = false;
        for(Entity mob : combatAction.parentEntity.groupList) {
            if((mob.nameKeyList.contains(combatAction.targetEntityString) || combatAction.groupCheck)
            && targetRoom.mobList.contains(mob)) {
                targetEntityInGroup = true;
                break;
            }
        }

        // Self Check //
        if(isHealing && combatAction.parentEntity.location.room == targetRoom
        && (allOnly
        || combatAction.selfCheck
        || (combatAction.groupCheck && !(singleOnly && !combatAction.parentEntity.groupList.isEmpty() && targetRoom.mobList.contains(combatAction.parentEntity.groupList.get(0))))
        || (combatAction.allCheck)
        || (!combatAction.selfCheck && !combatAction.groupCheck && !combatAction.allCheck && combatAction.targetEntityString.isEmpty()))) {
            targetList.add(combatAction.parentEntity);

            if(singleOnly
            || (!allOnly && combatAction.selfCheck)
            || ((!allOnly && combatAction.targetEntityString.isEmpty()) && !(isHealing && (combatAction.groupCheck || combatAction.allCheck)))) {
                System.out.println("---------=======-------------");
                System.out.println("Only Player");
                return targetList;
            }
        }

        // Room Entities (Checks Group Mobs First For Healing Spells) //
        System.out.println("\n------==========------");
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
                || (singleOnly && combatAction.allCheck)
                || (singleOnly && combatAction.groupCheck && combatAction.parentEntity.groupList.contains(mob))
                || (singleOnly && mob.nameKeyList.contains(combatAction.targetEntityString))
                || (!allOnly && !singleOnly && combatAction.allCheck)
                || (!allOnly && !singleOnly && combatAction.groupCheck && combatAction.parentEntity.groupList.contains(mob))
                || (!allOnly && !singleOnly && mob.nameKeyList.contains(combatAction.targetEntityString))))) {
                    targetList.add(mob);

                    if(singleOnly
                    || (!singleOnly && !allOnly && combatAction.selfCheck)
                    || (!singleOnly && !allOnly && !combatAction.targetEntityString.isEmpty())) {
                        break;
                    }
                }
            }
        }

        for(Entity e : targetList) {
            if(e.isPlayer) {
                System.out.println("Player");
            } else {
                System.out.println(e.name.label+", "+combatAction.parentEntity.groupList.contains(e));
            }
        }
        return targetList;
    }

    public String toString() {
        return getClass().toString().substring(getClass().toString().lastIndexOf(".") + 1);
    }
}
