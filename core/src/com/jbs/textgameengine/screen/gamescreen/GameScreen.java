package com.jbs.textgameengine.screen.gamescreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.ScreenUtils;
import com.jbs.textgameengine.Settings;
import com.jbs.textgameengine.gamedata.entity.mob.action.Action;
import com.jbs.textgameengine.gamedata.entity.mob.action.general.Move;
import com.jbs.textgameengine.gamedata.entity.player.Player;
import com.jbs.textgameengine.gamedata.world.galaxy.Galaxy;
import com.jbs.textgameengine.gamedata.world.room.Room;
import com.jbs.textgameengine.screen.Screen;
import com.jbs.textgameengine.screen.gamescreen.userinterface.UserInterface;
import com.jbs.textgameengine.screen.gamescreen.userinterface.console.line.Line;
import com.jbs.textgameengine.screen.gamescreen.userinterface.prompt.InputBar;

import java.util.HashMap;

public class GameScreen extends Screen {
    public static UserInterface userInterface;
    public static HashMap<String, Galaxy> galaxyList;
    public static Player player;

    public GameScreen() {
        super();

        userInterface = new UserInterface();
        galaxyList = Galaxy.loadDebugGalaxy();

        Room startRoom = Galaxy.getRoom("Cotton Tail Nebula", "Lago Morpha", 1, "Center Of The Universe", 0);
        player = new Player(startRoom);

        initInputProcessor();
    }

    public void initInputProcessor() {
        Gdx.input.setInputProcessor(new InputAdapter() {

            // Keyboard Input //
            @Override
            public boolean keyDown(int keyCode) {
                String key = Input.Keys.toString(keyCode);

                // Exit Game //
                if(key.equals("Escape")) {
                    dispose();
                    System.exit(0);
                }

                // Toggle Full Screen //
                else if(key.equals("F5")) {
                    if(Gdx.graphics.isFullscreen()) {
                        Gdx.graphics.setWindowedMode(Settings.WINDOW_WIDTH, Settings.WINDOW_HEIGHT);
                    } else {
                        Graphics.DisplayMode currentMode = Gdx.graphics.getDisplayMode();
                        Gdx.graphics.setFullscreenMode(currentMode);
                    }
                }

                // InputBar Input //
                else if(InputBar.inputCharString.contains(key)) {
                    userInterface.inputBar.concatinateUserInput(key);
                }

                // Reset Backspace Timer //
                else if(key.equals("Delete")) {
                    userInterface.inputBar.backspaceTimer = 0;
                }

                // Enter User Input //
                else if(key.equals("Enter")) {
                    if(!userInterface.inputBar.userInput.isEmpty()) {
                        processUserInput(userInterface.inputBar.userInput);
                        userInterface.inputBar.enterUserInput();
                    }
                }

                // Alt. Movement (Control + Arrow Keys) //
                else if((Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT) || Gdx.input.isKeyPressed(Input.Keys.CONTROL_RIGHT))
                && (key.equals("Up") || key.equals("Down") || key.equals("Left") || key.equals("Right"))) {
                    String targetDirection = "";
                    if(key.equals("Up")) {targetDirection = "North";}
                    else if(key.equals("Down")) {targetDirection = "South";}
                    else if(key.equals("Left")) {targetDirection = "West";}
                    else if(key.equals("Right")) {targetDirection = "East";}

                    Move moveAction = new Move();
                    if(player.currentAction != null && moveAction.interruptAction) {
                        userInterface.console.writeToConsole(new Line("You stop what you are doing.", "4CONT5CONT5CONT4CONT4CONT5CONT1DY", "", true, true));
                        player.currentAction = null;
                    }

                    moveAction.actionType = "Direction";
                    moveAction.targetDirection = targetDirection;
                    moveAction.initiate(player);
                }

                // Scroll User Input //
                else if(key.equals("Up") || key.equals("Down")) {
                    userInterface.inputBar.scrollUserInput(key);
                }

                return true;
            }
        });
    }

    public String update() {
        userInterface.inputBar.update();

        return "";
    }

    public void render() {
        ScreenUtils.clear(0, 0, 0, 1);
        userInterface.render(shapeRenderer, spriteBatch);

        // Render FPS //
        spriteBatch.setProjectionMatrix(camera.combined);
        spriteBatch.begin();
        userInterface.console.font.setColor(Color.WHITE);
        userInterface.console.font.draw(spriteBatch, "FPS: " + String.valueOf(Gdx.graphics.getFramesPerSecond()), Settings.WINDOW_WIDTH - 107, Settings.WINDOW_HEIGHT - 5);
        spriteBatch.end();
    }

    public void processUserInput(String input) {
        boolean actionCheck = false;
        for(Action actionType : Action.actionList) {
            Action targetAction = actionType.getActionFromInput(input.toLowerCase(), player);
            if(targetAction != null) {
                if(player.currentAction != null && targetAction.interruptAction) {
                    userInterface.console.writeToConsole(new Line("You stop what you are doing.", "4CONT5CONT5CONT4CONT4CONT5CONT1DY", "", true, true));
                    player.currentAction = null;
                }
                targetAction.initiate(player);

                actionCheck = true;
                break;
            }
        }

        if(!actionCheck) {
            userInterface.console.writeToConsole(new Line("Huh?", "3CONT1DY", "", true, true));

//            userInterface.console.writeToConsole(new Line("This is a test line.", "19CONTR1Y", "", true, true));
//            userInterface.console.writeToConsole(new Line("This is a test.", "14CONTR1Y", "", true, true));
//            userInterface.console.writeToConsole(new Line("This is.", "7CONT1Y", "", true, true));
//            userInterface.console.writeToConsole(new Line("This.", "4CONT1Y", "", true, true));
//            userInterface.console.writeToConsole(new Line("This is a test line. It is really long. But not too long, or it would be way too long.", "85SHIBR1Y", "86GRABB", true, true));
//            userInterface.console.writeToConsole(new Line("This is a test line. It is super long so I can test all the colors. Yo this should be multiple lines, yo.", "104SHIBR1Y", "105GRABB-Y-G", true, true));
//            userInterface.console.writeToConsole(new Line("This is a test line. It is super long so I can test all the colors. Yo this should be multiple lines, yo.", "104SHIBR1Y", "105GRABB-Y", true, true));
//            userInterface.console.writeToConsole(new Line("This is a test line. It is super long so I can test all the colors. Yo this should be multiple lines, yo.", "104SHIBR1Y", "105GRABB", true, true));
//            userInterface.console.writeToConsole(new Line("This is a test line. It is super long so I can test all the colors. Yo this should be multiple lines, yo.", "104SHIBR1Y", "105GRAB", true, true));
        }
    }

    public void dispose() {
        userInterface.dispose();
    }
}
