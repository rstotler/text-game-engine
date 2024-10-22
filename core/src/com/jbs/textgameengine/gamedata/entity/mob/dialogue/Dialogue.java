package com.jbs.textgameengine.gamedata.entity.mob.dialogue;

import com.jbs.textgameengine.gamedata.entity.mob.Mob;
import com.jbs.textgameengine.gamedata.entity.mob.action.Action;
import com.jbs.textgameengine.gamedata.entity.mob.action.other.Say;
import com.jbs.textgameengine.screen.gamescreen.GameScreen;

import java.util.*;

public class Dialogue {
    public Mob parentEntity;

    public HashMap<String, ArrayList<String>> dialogueMap;
    public String dialogueSection;
    public int dialogueIndex;

    public int speakTimer;
    public int speakTimerMax;
    public boolean resetOnPlayerEntrance;

    public Dialogue(Mob parentEntity, HashMap<String, ArrayList<String>> dialogueMap) {
        this.parentEntity = parentEntity;

        this.dialogueMap = dialogueMap;
        dialogueSection = "Default";
        dialogueIndex = 0;

        speakTimerMax = 240;
        speakTimer = speakTimerMax - 5;
        resetOnPlayerEntrance = false;
    }

    public void update() {
        speakTimer += 1;
        if(speakTimer >= speakTimerMax) {
            speakTimer = 0;

            String currentDialogueString = null;
            if(dialogueMap.containsKey(dialogueSection)
            && dialogueIndex < dialogueMap.get(dialogueSection).size()) {
                currentDialogueString = dialogueMap.get(dialogueSection).get(dialogueIndex);
            }

            if(currentDialogueString != null) {
                Action sayAction = new Say(parentEntity);
                sayAction = sayAction.getActionFromInput("say " + currentDialogueString, parentEntity);
                sayAction.initiate();

                dialogueIndex += 1;
                if(dialogueIndex >= dialogueMap.get(dialogueSection).size()) {
                    dialogueIndex = 0;
                }
            }
        }
    }
}
