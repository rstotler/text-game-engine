package com.jbs.textgameengine.gamedata.world.room.door;

public class Door {
    public String type;
    public int keyNum;    // -9999: No Lock On Door/No Key Required
    public String status; // Open, Closed, Locked

    public Door(String type, int keyNum) {
        this.type = type;
        this.keyNum = keyNum;

        if(keyNum != -9999) {
            status = "Locked";
        }
        else {
            status = "Closed";
        }
    }
}
