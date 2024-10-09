package com.jbs.textgameengine.gamedata.entity.item;

import com.jbs.textgameengine.gamedata.entity.Entity;
import com.jbs.textgameengine.gamedata.world.Location;
import com.jbs.textgameengine.screen.gamescreen.userinterface.console.line.Line;

import java.util.ArrayList;

public class Item extends Entity {
    public int id;
    public String pocket;
    public int quantity;
    public boolean isQuantity;

    public ArrayList<Integer> keyList;
    public ArrayList<Item> containerItemList;

    public Item(Location startLocation) {
        super(startLocation);
        isItem = true;

        pocket = "General";
        quantity = -1;
        isQuantity = false;

        keyList = null;
        containerItemList = null;
    }

    public static Item load(int id, Location startLocation) {
        Item item = new Item(startLocation);

        // 1 - A Silver Key //
        if(id == 1) {
            item.name = new Line("Silver Key", "7SHIAGR3CONT", "", true, true);
            item.keyList = new ArrayList<>();
            item.keyList.add(1234);
        }

        // 2 - A Key To Starship Heart of Gold //
        else if(id == 2) {
            item.name = new Line("Key to Starship Heart of Gold", "4CONT3CONT9CONT6SHIAR3CONT4SHIAY", "", true, true);
            item.keyList = new ArrayList<>();
            item.keyList.add(7777);
        }

        // Default Item //
        else {
            item.name = new Line("Default Item", "8CONT4CONT", "", true, true);
        }

        item.nameKeyList = Entity.createNameKeyList(item.prefix + item.name.label);

        return item;
    }

    public static Item load(int id, Location startLocation, int quantity) {
        Item quantityItem = load(id, startLocation);
        if(quantityItem.isQuantity) {
            quantityItem.quantity = quantity;
        }

        return quantityItem;
    }
}
