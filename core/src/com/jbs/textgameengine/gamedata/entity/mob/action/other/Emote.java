package com.jbs.textgameengine.gamedata.entity.mob.action.other;

import com.jbs.textgameengine.gamedata.entity.mob.Mob;
import com.jbs.textgameengine.gamedata.entity.mob.action.Action;
import com.jbs.textgameengine.screen.gamescreen.GameScreen;
import com.jbs.textgameengine.screen.gamescreen.userinterface.console.line.Line;

import java.util.*;
import java.util.stream.Collectors;

public class Emote extends Action {
    public Emote(Mob parentEntity, String actionType) {
        super(parentEntity);

        this.actionType = actionType;
    }

    public Emote() {
        this(null, null);
    }

    public Action getActionFromInput(String input, Mob parentEntity) {
        ArrayList<String> inputList = new ArrayList<>(Arrays.asList(input.split(" ")));

        if(Arrays.asList("hmm", "hm").contains(inputList.get(0))
        && inputList.size() == 1) {
            return new Emote(parentEntity, "Hmm");
        }

        else if(Arrays.asList("nod").contains(inputList.get(0))
        && inputList.size() == 1) {
            return new Emote(parentEntity, "Nod");
        }

        else if(Arrays.asList("nodnod").contains(inputList.get(0))
        && inputList.size() == 1) {
            return new Emote(parentEntity, "Nodnod");
        }

        else if(Arrays.asList("tap").contains(inputList.get(0))
        && inputList.size() == 1) {
            return new Emote(parentEntity, "Tap");
        }

        else if(Arrays.asList("boggle").contains(inputList.get(0))
        && inputList.size() == 1) {
            return new Emote(parentEntity, "Boggle");
        }

        else if(Arrays.asList("jump").contains(inputList.get(0))
        && inputList.size() == 1) {
            return new Emote(parentEntity, "Jump");
        }

        else if(Arrays.asList("ahah").contains(inputList.get(0))
        && inputList.size() == 1) {
            return new Emote(parentEntity, "Ahah");
        }

        else if(Arrays.asList("gaze").contains(inputList.get(0))
        && inputList.size() == 1) {
            return new Emote(parentEntity, "Gaze");
        }

        else if(Arrays.asList("cackle").contains(inputList.get(0))
        && inputList.size() == 1) {
            return new Emote(parentEntity, "Cackle");
        }

        else if(Arrays.asList("cheer").contains(inputList.get(0))
        && inputList.size() == 1) {
            return new Emote(parentEntity, "Cheer");
        }

        else if(Arrays.asList("sigh").contains(inputList.get(0))
        && inputList.size() == 1) {
            return new Emote(parentEntity, "Sigh");
        }

        return null;
    }

    public void initiate() {
        if(actionType.equals("Hmm")) {
            if(parentEntity.isPlayer) {
                GameScreen.userInterface.console.writeToConsole(new Line("You scratch your chin and go, 'Hmm..'", "4CONT8CONT5CONT5CONT4CONT2CONT3DY5CONT1DY", "", true, true));
            }
        }

        else if(actionType.equals("Nod")) {
            if(parentEntity.isPlayer) {
                GameScreen.userInterface.console.writeToConsole(new Line("You nod your head in agreement.", "4CONT4CONT5CONT5CONT3CONT9CONT1DY", "", true, true));
            }
        }

        else if(actionType.equals("Nodnod")) {
            if(parentEntity.isPlayer) {
                GameScreen.userInterface.console.writeToConsole(new Line("You nodnod.", "4CONT6CONT1DY", "", true, true));
            }
        }

        else if(actionType.equals("Tap")) {
            if(parentEntity.isPlayer) {
                GameScreen.userInterface.console.writeToConsole(new Line("You tap your foot impatiently..", "4CONT4CONT5CONT5CONT11CONT2DY", "", true, true));
            }
        }

        else if(actionType.equals("Boggle")) {
            if(parentEntity.isPlayer) {
                GameScreen.userInterface.console.writeToConsole(new Line("You boggle in complete incomprehension.", "4CONT7CONT3CONT9CONT15CONT1DY", "", true, true));
            }
        }

        else if(actionType.equals("Jump")) {
            if(parentEntity.isPlayer) {
                GameScreen.userInterface.console.writeToConsole(new Line("You jump up and down.", "4CONT5CONT3CONT4CONT4CONT1DY", "", true, true));
            }
        }

        else if(actionType.equals("Ahah")) {
            if(parentEntity.isPlayer) {
                GameScreen.userInterface.console.writeToConsole(new Line("Comprehension dawns upon you.", "14CONT6CONT5CONT3CONT1DY", "", true, true));
            }
        }

        else if(actionType.equals("Gaze")) {
            if(parentEntity.isPlayer) {
                if(!parentEntity.location.room.inside) {
                    GameScreen.userInterface.console.writeToConsole(new Line("You gaze into the sky, transfixed in thought.", "4CONT5CONT5CONT4CONT3CONT2DY11CONT3CONT7CONT1DY", "", true, true));
                } else {
                    GameScreen.userInterface.console.writeToConsole(new Line("You stare off into space, deep in thought.", "4CONT6CONT4CONT5CONT5CONT2DY5CONT3CONT7CONT1DY", "", true, true));
                }
            }
        }

        else if(actionType.equals("Cackle")) {
            if(parentEntity.isPlayer) {
                if(new Random().nextInt(4) == 0) {
                    GameScreen.userInterface.console.writeToConsole(new Line("You throw your head back and cackle with insane glee!", "4CONT6CONT5CONT5CONT5CONT4CONT7CONT5CONT7CONT4CONT1DY", "", true, true));
                } else {
                    GameScreen.userInterface.console.writeToConsole(new Line("You cackle with insane glee!", "4CONT7CONT5CONT7CONT4CONT1DY", "", true, true));
                }
            }
        }

        else if(actionType.equals("Cheer")) {
            if(parentEntity.isPlayer) {
                GameScreen.userInterface.console.writeToConsole(new Line("And the peasants rejoiced.", "4CONT4CONT9CONT8CONT1DY", "", true, true));
            }
        }

        else if(actionType.equals("Sigh")) {
            if(parentEntity.isPlayer) {
                GameScreen.userInterface.console.writeToConsole(new Line("You sigh..", "4CONT4CONT2DY", "", true, true));
            }
        }
    }
}
