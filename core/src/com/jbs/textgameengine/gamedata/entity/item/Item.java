package com.jbs.textgameengine.gamedata.entity.item;

import com.jbs.textgameengine.gamedata.entity.Entity;
import com.jbs.textgameengine.gamedata.entity.item.type.Gear;
import com.jbs.textgameengine.gamedata.entity.item.type.Weapon;
import com.jbs.textgameengine.gamedata.world.Location;
import com.jbs.textgameengine.screen.gamescreen.userinterface.console.line.Line;

import java.util.ArrayList;

public class Item extends Entity {
    public String type;
    public String pocket;
    public int quantity;
    public boolean isQuantity;

    public ArrayList<Integer> keyList;
    public ArrayList<Item> containerItemList;

    public Item(int id, Location startLocation) {
        super(id, startLocation);
        isItem = true;

        type = "General";
        pocket = "General";
        quantity = -1;
        isQuantity = false;

        keyList = null;
        containerItemList = null;
    }

    public static Item load(String itemType, int id, Location startLocation) {
        Item item = null;

        if(itemType.equals("Gear")) {item = Gear.load(id, startLocation);}
        else if(itemType.equals("Weapon")) {item = Weapon.load(id, startLocation);}

        else {
            item = new Item(id, startLocation);

            // 001 - A Silver Key //
            if(id == 1) {
                item.name = new Line("Silver Key", "7SHIAGR3CONT", "", true, true);
                item.keyList = new ArrayList<>();
                item.keyList.add(1234);
            }

            // 002 - A Key To Starship Heart of Gold //
            else if(id == 2) {
                item.name = new Line("Key to Starship Heart of Gold", "4CONT3CONT9CONT6SHIAR3CONT4SHIAY", "", true, true);
                item.keyList = new ArrayList<>();
                item.keyList.add(7777);
            }

            // 003 - A Piece of Gold //
            else if(id == 3) {
                item.name = new Line("Piece of Gold", "6CONT3CONT4SHIAY", "", true, true);
                item.isQuantity = true;
            }

            // Default Item //
            else {
                item.name = new Line("Default Item", "8CONT4CONT", "", true, true);
            }

            item.nameKeyList = Entity.createNameKeyList(item.prefix + item.name.label);

            if(item.isQuantity
            && item.quantity == -1) {
                item.quantity = 1;
            }
        }

        return item;
    }

    public static Item load(String itemType, int id, Location startLocation, int quantity) {
        Item quantityItem = load(itemType, id, startLocation);
        if(quantityItem.isQuantity) {
            quantityItem.quantity = quantity;
        }

        return quantityItem;
    }
}
