package com.jbs.textgameengine.gamedata.entity.item;

import com.jbs.textgameengine.gamedata.entity.Entity;
import com.jbs.textgameengine.gamedata.world.Location;
import com.jbs.textgameengine.screen.gamescreen.userinterface.console.line.Line;

import java.util.ArrayList;

public class Item extends Entity {
    public int id;
    public String pocket;

    public ArrayList<Integer> keyList;
    public ArrayList<Item> containerItemList;

    public Item(Location startLocation) {
        super(startLocation);
        isItem = true;

        pocket = "General";

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
            item.keyList.add(12345);
        }

        item.nameKeyList = Entity.createNameKeyList(item.prefix + item.name.label);

        return item;
    }
}
