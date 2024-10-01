package com.jbs.textgameengine.gamedata.entity.mob;

import com.jbs.textgameengine.gamedata.entity.Entity;
import com.jbs.textgameengine.gamedata.entity.item.Item;
import com.jbs.textgameengine.gamedata.entity.mob.action.Action;
import com.jbs.textgameengine.gamedata.entity.mob.properties.skill.Skill;
import com.jbs.textgameengine.gamedata.entity.mob.properties.skill.combatskill.general.*;
import com.jbs.textgameengine.gamedata.world.Location;
import com.jbs.textgameengine.screen.gamescreen.GameScreen;
import com.jbs.textgameengine.screen.gamescreen.userinterface.console.line.Line;

import java.util.*;

public class Mob extends Entity {
    public HashMap<String, ArrayList<Item>> inventory;
    public HashMap<String, Item> gear;

    public HashMap<String, ArrayList<Skill>> skillMap;

    public Action currentAction;
    public ArrayList<Mob> targetList;
    public ArrayList<Mob> combatList;

    public Mob(Location startLocation) {
        super(startLocation);
        isMob = true;

        inventory = loadInventory();
        gear = loadGear();

        skillMap = loadSkillMap();

        currentAction = null;
        targetList = new ArrayList<>();
        combatList = new ArrayList<>();
    }

    public static Mob load(int id, Location startLocation) {
        Mob mob = new Mob(startLocation);

        // 1 - Greeter Droid //
        if(id == 1) {
            mob.name = new Line("Greeter Droid", "8CONT5CONT", "", true, true);
            mob.roomDescription = new Line("is here, greeting visitors.", "3CONT4CONT2DY9CONT8CONT1DY", "", true, true);
        }

        mob.nameKeyList = Entity.createNameKeyList(mob.prefix + mob.name.label);

        return mob;
    }

    public static HashMap<String, ArrayList<Item>> loadInventory() {
        HashMap<String, ArrayList<Item>> inventory = new HashMap<String, ArrayList<Item>>();
        inventory.put("General", new ArrayList<Item>());

        return inventory;
    }

    public static HashMap<String, Item> loadGear() {
        HashMap<String, Item> gear = new HashMap<>();
        gear.put("Head", null);
        gear.put("Body", null);
        gear.put("Arms", null);
        gear.put("Hands", null);
        gear.put("Legs", null);
        gear.put("Boots", null);
        gear.put("Neck 1", null);
        gear.put("Neck 2", null);
        gear.put("Ring 1", null);
        gear.put("Ring 2", null);

        return gear;
    }

    public static HashMap<String, ArrayList<Skill>> loadSkillMap() {
        HashMap<String, ArrayList<Skill>> newSkillMap = new HashMap<>();

        ArrayList<Skill> combatSkillList = new ArrayList<>();
        combatSkillList.add(new Punch());
        combatSkillList.add(new Fireball());
        combatSkillList.add(new Firestorm());
        combatSkillList.add(new Heal());
        combatSkillList.add(new SpinPunch());
        newSkillMap.put("Combat", combatSkillList);

        return newSkillMap;
    }

    public void update() {
        if(currentAction != null) {
        }
    }

    public boolean interruptAction() {
        if(currentAction != null) {
            GameScreen.userInterface.console.writeToConsole(new Line("You stop what you are doing.", "4CONT5CONT5CONT4CONT4CONT5CONT1DY", "", true, true));
            currentAction = null;
            return true;
        }

        return false;
    }

    public void addItemToInventory(Item targetItem) {
        inventory.get(targetItem.pocket).add(targetItem);
    }

    public boolean hasKey(int keyNum) {

        // Check Inventory //
        for(String pocket : inventory.keySet()) {
            for(Item item : inventory.get(pocket)) {
                if(item.keyList != null
                && item.keyList.contains(keyNum)) {
                    return true;
                }

                // Container Check //
                if(item.containerItemList != null) {
                    for(Item containerItem : item.containerItemList) {
                        if(item.keyList != null
                        && item.keyList.contains(keyNum)) {
                            return true;
                        }
                    }
                }
            }
        }

        // Check Gear //
        for(String gearSlot : gear.keySet()) {
            Item item = gear.get(gearSlot);
            if(item != null) {
                if(item.keyList != null
                && item.keyList.contains(keyNum)) {
                    return true;
                }

                // Container Check //
                if(item.containerItemList != null) {
                    for(Item containerItem : item.containerItemList) {
                        if(item.keyList != null
                        && item.keyList.contains(keyNum)) {
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }
}
