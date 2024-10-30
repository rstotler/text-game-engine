package com.jbs.textgameengine.gamedata.entity.mob;

import com.jbs.textgameengine.gamedata.entity.Entity;
import com.jbs.textgameengine.gamedata.entity.item.Item;
import com.jbs.textgameengine.gamedata.entity.mob.action.Action;
import com.jbs.textgameengine.gamedata.entity.mob.action.combat.CombatAction;
import com.jbs.textgameengine.gamedata.entity.mob.action.combat.combateffect.Cooldown;
import com.jbs.textgameengine.gamedata.entity.mob.action.general.Move;
import com.jbs.textgameengine.gamedata.entity.mob.dialogue.Dialogue;
import com.jbs.textgameengine.gamedata.entity.mob.properties.skill.Skill;
import com.jbs.textgameengine.gamedata.entity.mob.properties.skill.combatskill.basic.*;
import com.jbs.textgameengine.gamedata.entity.mob.properties.skill.combatskill.debug.*;
import com.jbs.textgameengine.gamedata.entity.mob.properties.statuseffect.StatusEffect;
import com.jbs.textgameengine.gamedata.world.Location;
import com.jbs.textgameengine.gamedata.world.room.Room;
import com.jbs.textgameengine.gamedata.world.utility.AreaAndRoomData;
import com.jbs.textgameengine.gamedata.world.utility.TargetRoomData;
import com.jbs.textgameengine.screen.gamescreen.GameScreen;
import com.jbs.textgameengine.screen.gamescreen.userinterface.console.line.Line;

import java.util.*;

public class Mob extends Entity {
    public static ArrayList<String> gearSlotList = new ArrayList<>(Arrays.asList("Head", "Face", "Body", "About", "Hands", "Legs", "Feet", "Boots", "Neck", "Ring", "Main", "Off"));

    public String type;
    public HashMap<String, ArrayList<Item>> inventory;
    public HashMap<String, Item> gear;

    public HashMap<String, ArrayList<Skill>> skillMap;
    public HashMap<String, StatusEffect> statusEffectMap;

    public Action currentAction;
    public ArrayList<Mob> targetList;
    public ArrayList<Mob> groupList;
    public ArrayList<Mob> combatList;

    public String facingDirection;
    public Dialogue dialogue;

    public Mob(int id, Location startLocation) {
        super(id, startLocation);
        isMob = true;
        type = "Mob";

        inventory = loadInventory();
        gear = loadGear();

        skillMap = loadSkillMap();
        statusEffectMap = new HashMap<>();

        currentAction = null;
        targetList = new ArrayList<>();
        groupList = new ArrayList<>();
        combatList = new ArrayList<>();

        facingDirection = "North";
        dialogue = null;
    }

    public static Mob load(int id, Location startLocation) {
        Mob mob = new Mob(id, startLocation);

        // 001 - A Greeter Droid //
        if(id == 1) {
            mob.name = new Line("Greeter Droid", "8CONT5CONT", "", true, true);
            mob.roomDescription = new Line("is here, greeting visitors.", "3CONT4CONT2DY9CONT8CONT1DY", "", true, true);

            HashMap<String, ArrayList<String>> dialogueMap = new HashMap<>();
            dialogueMap.put("Default", new ArrayList<String>());
            dialogueMap.get("Default").add("Welcome to COTU Spaceport. Please enjoy your stay!");
            mob.dialogue = new Dialogue(mob, dialogueMap);
        }

        // 002 - A Shopkeeper Droid //
        else if(id == 2) {
            mob.name = new Line("Shopkeeper Droid", "11CONT5CONT", "", true, true);
            mob.roomDescription = new Line("is here, helping customers.", "3CONT4CONT2DY8CONT9CONT1DY", "", true, true);

            HashMap<String, ArrayList<String>> dialogueMap = new HashMap<>();
            dialogueMap.put("Default", new ArrayList<String>());
            dialogueMap.get("Default").add("Welcome to the gift shop. Please 'List' if you need assistance.");
            mob.dialogue = new Dialogue(mob, dialogueMap);
            mob.dialogue.resetOnPlayerEntrance = true;
        }

        // 003 - A Sasquatch //
        else if(id == 3) {
            mob.name = new Line("Sasquatch", "9CONT", "", true, true);
        }

        // 004 - A Skinny Alien Dude //
        else if(id == 4) {
            mob.name = new Line("Skinny Alien Dude", "7CONT6CONT4CONT", "", true, true);
        }

        // A Default Mob //
        else {
            mob.name = new Line("Default Mob", "8CONT3CONT", "", true, true);
        }

        mob.nameKeyList = Entity.createNameKeyList(mob.prefix + mob.name.label);

        return mob;
    }

    public static HashMap<String, ArrayList<Item>> loadInventory() {
        HashMap<String, ArrayList<Item>> inventory = new HashMap<String, ArrayList<Item>>();
        inventory.put("General", new ArrayList<Item>());
        inventory.put("Gear", new ArrayList<Item>());
        inventory.put("Weapons", new ArrayList<Item>());
        inventory.put("Ammo", new ArrayList<Item>());
        inventory.put("Food", new ArrayList<Item>());

        return inventory;
    }

    public static HashMap<String, Item> loadGear() {
        HashMap<String, Item> gear = new HashMap<>();

        for(String gearSlot : gearSlotList) {
            if(Arrays.asList("Neck", "Ring").contains(gearSlot)) {
                gear.put(gearSlot + " 1", null);
                gear.put(gearSlot + " 2", null);
            } else if(Arrays.asList("Main", "Off").contains(gearSlot)) {
                gear.put(gearSlot + " Hand", null);
            } else {
                gear.put(gearSlot, null);
            }
        }

        return gear;
    }

    public static HashMap<String, ArrayList<Skill>> loadSkillMap() {
        HashMap<String, ArrayList<Skill>> newSkillMap = new HashMap<>();

        // Debug Skills //
        ArrayList<Skill> debugSkillList = new ArrayList<>();
        if(true) {
            debugSkillList.add(new SpinPunch());
            debugSkillList.add(new WindSlash());
            debugSkillList.add(new Fireball());
            debugSkillList.add(new Firestorm());
            debugSkillList.add(new Ice());
            debugSkillList.add(new Touch());
            debugSkillList.add(new Smooches());
            debugSkillList.add(new Heal());
            debugSkillList.add(new ForceTouch());
            debugSkillList.add(new WhiteWind());
            debugSkillList.add(new FullLife());
            newSkillMap.put("Debug", debugSkillList);
        }

        // Basic Combat Skills //
        ArrayList<Skill> basicCombatSkillList = new ArrayList<>();
        if(true) {
            basicCombatSkillList.add(new Dodge());
            basicCombatSkillList.add(new Block());
            basicCombatSkillList.add(new Sweep());
            basicCombatSkillList.add(new Punch());
            basicCombatSkillList.add(new Jab());
            basicCombatSkillList.add(new Kick());
            basicCombatSkillList.add(new Slash());
            basicCombatSkillList.add(new Stab());
            basicCombatSkillList.add(new Bash());
            basicCombatSkillList.add(new Smash());
            basicCombatSkillList.add(new Shoot());
            basicCombatSkillList.add(new Snipe());
            basicCombatSkillList.add(new Throw());
            newSkillMap.put("Basic Combat", basicCombatSkillList);
        }

        return newSkillMap;
    }

    public void update() {

        // Lose Sight Of Targets Check (Via Room Darkness) //
        if(true) {
            ArrayList<Mob> removeTargetList = new ArrayList<>();
            for(int i = targetList.size() - 1; i >= 0; i--) {
                Mob targetMob = targetList.get(i);
                if(!targetMob.location.room.isLit()) {
                    targetList.remove(i);
                    removeTargetList.add(targetMob);
                }
            }

            if(!removeTargetList.isEmpty()
            && isPlayer) {
                if(removeTargetList.size() == 1) {
                    Mob targetMob = removeTargetList.get(0);
                    GameScreen.userInterface.console.writeToConsole(new Line("You lose sight of " + targetMob.prefix.toLowerCase() + targetMob.name.label + ".", "4CONT5CONT6CONT3CONT" + String.valueOf(targetMob.prefix.length()) + "CONT" + targetMob.name.colorCode + "1DY", "", true, true));
                }
                else if(!targetList.isEmpty()) {
                    GameScreen.userInterface.console.writeToConsole(new Line("You lose sight of some of your targets.", "4CONT5CONT6CONT3CONT5CONT3CONT5CONT7CONT1DY", "", true, true));
                }
                else if(targetList.isEmpty()) {
                    GameScreen.userInterface.console.writeToConsole(new Line("You lose sight of your targets.", "4CONT5CONT6CONT3CONT5CONT7CONT1DY", "", true, true));
                }
            }
        }

        // Dialogue Check //
        if(!isPlayer
        && dialogue != null
        && location.room == GameScreen.player.location.room) {
            dialogue.update();
        }

        // Combat Check (Mob) //
        boolean initiateAttackCheck = false;
        if(!isPlayer
        && !combatList.isEmpty()) {

            // Combat Target Is In Different Room (Move Toward Target) //
            if(location.room != combatList.get(0).location.room) {
                TargetRoomData targetRoomData = TargetRoomData.findTargetRoomFromStartRoom(location.room, combatList.get(0).location.room, getMaxViewDistance());
                if(targetRoomData.targetRoom != null
                && !targetRoomData.directionList.isEmpty()) {
                    Action moveAction = new Move(this);
                    moveAction = moveAction.getActionFromInput(targetRoomData.directionList.get(0).toLowerCase(), this);
                    moveAction.initiate();
                }
            }

            // Combat Target Is In Same Room (Attack Target) //
            else if(currentAction == null) {
                Skill randomSkill = getRandomCombatSkill();
                if(randomSkill != null) {
                    Action combatAction = new CombatAction(this, randomSkill);
                    combatAction = combatAction.getActionFromInput(randomSkill.name.label.toLowerCase() + " player", this);
                    combatAction.initiate();
                    initiateAttackCheck = true;
                }
            }
        }

        // Current Action Check //
        if(!initiateAttackCheck
        && currentAction != null) {
            currentAction.performActionTimer -= 1;
            if(currentAction.performActionTimer <= 0) {
                Action previousAction = currentAction;

                currentAction.performAction();
                currentAction = null;

                // Cooldown, Pause After Attack (Mob Only) //
                if(!isPlayer
                && previousAction.isCombatAction) {
                    currentAction = new Cooldown(3);
                }
            }
        }
    }

    public boolean interruptAction() {
        if(currentAction != null
        && !currentAction.isCombatAction) {
            GameScreen.userInterface.console.writeToConsole(new Line("You stop " + currentAction.toString().toLowerCase() + "ing.", "4CONT5CONT" + String.valueOf(currentAction.toString().length()) + "CONT3DDW1DY", "", true, true));
            currentAction = null;
            return true;
        }

        return false;
    }

    public void combatActionDistanceCancelCheck() {
        if(currentAction != null
        && currentAction.isCombatAction) {
            TargetRoomData targetRoomData = TargetRoomData.getTargetRoomFromStartRoom(location.room, currentAction.movementList, false, false);
            if(targetRoomData.distance > currentAction.skill.getMaxDistance(this)
            || !targetRoomData.message.isEmpty()) {
                if(isPlayer) {
                    GameScreen.userInterface.console.writeToConsole(new Line("You stop " + currentAction.skill.name.label.toLowerCase() + "ing.", "4CONT5CONT" + String.valueOf(currentAction.skill.name.label.length()) + "CONT3DDW1DY", "", true, true));
                }
                currentAction = null;
            }
        }
    }

    public ArrayList<Mob> loseSightOfTargetsCheck() {
        ArrayList<Mob> loseSightOfTargetList = new ArrayList<>(targetList);

        if(!targetList.isEmpty()) {
            AreaAndRoomData surroundingRoomData = AreaAndRoomData.getSurroundingAreaAndRoomData(location.room, getMaxViewDistance(), false, false);
            for(Room room : surroundingRoomData.roomList) {
                for(int i = loseSightOfTargetList.size() - 1; i >= 0; i--) {
                    if(room.mobList.contains(loseSightOfTargetList.get(i))) {
                        loseSightOfTargetList.remove(i);
                    }
                }
            }
            for(int i = targetList.size() - 1; i >= 0; i--) {
                if(loseSightOfTargetList.contains(targetList.get(i))) {
                    targetList.remove(i);
                }
            }
        }

        return loseSightOfTargetList;
    }

    public float getWeight() {
        float currentWeight = 0.0f;

        for(String pocket : inventory.keySet()) {
            for(Item item : inventory.get(pocket)) {
                currentWeight += item.getWeight();
            }
        }
        for(String gearSlot : gear.keySet()) {
            Item item = gear.get(gearSlot);
            if(item != null) {
                currentWeight += item.getWeight();
            }
        }

        return currentWeight;
    }

    public float getMaxWeight() {
        return 100.0f;
    }

    public int getMaxViewDistance() {
        return 5;
    }

    public ArrayList<Skill> getCombatSkills() {
        ArrayList<Skill> combatSkillList = new ArrayList<>();
        combatSkillList.addAll(skillMap.get("Debug"));
        combatSkillList.addAll(skillMap.get("Basic Combat"));

        return combatSkillList;
    }

    public Skill getRandomCombatSkill() {
        Skill targetSkill = null;

        ArrayList<Skill> skillList = new ArrayList<>();
        if(skillMap.containsKey("Debug")) {
            for(Skill skill : skillMap.get("Debug")) {
                if(!skill.isHealing) {
                    skillList.add(skill);
                }
            }
        }

        if(!skillList.isEmpty()) {
            int randomIndex = new Random().nextInt(skillList.size());
            targetSkill = skillList.get(randomIndex);
        }

        return targetSkill;
    }

    public void addItemToInventory(Item targetItem) {

        // Quantity Item Already In Inventory Check //
        boolean quantityItemInInventory = false;
        if(targetItem.isQuantity) {
            for(Entity item : inventory.get(targetItem.pocket)) {
                if(targetItem.id == item.id
                && targetItem.type.equals(((Item) item).type)) {
                    ((Item) item).quantity += targetItem.quantity;
                    quantityItemInInventory = true;
                    break;
                }
            }
        }

        // Default Add Item To Inventory //
        if(!quantityItemInInventory) {
            inventory.get(targetItem.pocket).add(targetItem);
        }
    }

    public Item getItemFromInventory(String key) {
        for(String pocket : inventory.keySet()) {
            for(Item item : inventory.get(pocket)) {
                if(item.nameKeyList.contains(key)) {
                    return item;
                }
            }
        }

        return null;
    }

    public Item getItemFromGear(String key) {
        for(String gearSlot : gear.keySet()) {
            Item item = gear.get(gearSlot);
            if(item != null && item.nameKeyList.contains(key)) {
                return item;
            }
        }

        return null;
    }

    public void changeFacingDirection(String rotateDirection) {
        ArrayList<String> directionList = new ArrayList<>(Arrays.asList("North", "East", "South", "West"));
        int currentIndex = -1;
        if(directionList.contains(facingDirection)) {
            currentIndex = directionList.indexOf(facingDirection);
            if(rotateDirection.equals("Left")) {currentIndex -= 1;}
            else if(rotateDirection.equals("Right")) {currentIndex += 1;}

            if(currentIndex >= directionList.size()) {currentIndex = 0;}
            else if(currentIndex < 0) {currentIndex = directionList.size() - 1;}

            facingDirection = directionList.get(currentIndex);
        }

        // Update RoomView (Player Only) //
        if(isPlayer) {
            GameScreen.userInterface.roomView.buffer(location, facingDirection);
        }
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

    public boolean canDualWield() {
        return true;
    }

    public boolean isGlowing() {
        if(glowing) {
            return true;
        }

        for(String gearSlot : gear.keySet()) {
            Item gearItem = gear.get(gearSlot);
            if(gearItem != null && gearItem.glowing) {
                return true;
            }
        }

        return false;
    }
}
