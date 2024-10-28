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
import com.jbs.textgameengine.gamedata.world.utility.AreaAndRoomData;
import com.jbs.textgameengine.gamedata.world.Location;
import com.jbs.textgameengine.gamedata.world.area.Area;
import com.jbs.textgameengine.gamedata.world.galaxy.Galaxy;
import com.jbs.textgameengine.gamedata.world.planetoid.Planet;
import com.jbs.textgameengine.gamedata.world.room.Room;
import com.jbs.textgameengine.screen.Screen;
import com.jbs.textgameengine.screen.gamescreen.userinterface.UserInterface;
import com.jbs.textgameengine.screen.gamescreen.userinterface.console.line.Line;
import com.jbs.textgameengine.screen.gamescreen.userinterface.prompt.InputBar;
import com.jbs.textgameengine.screen.utility.Mouse;

import java.util.HashMap;
import java.util.Random;

public class GameScreen extends Screen {
    public static UserInterface userInterface;
    public static Mouse mouse;

    public static HashMap<String, Galaxy> galaxyList;
    public static Player player;

    public static long gameTimer;
    public int frameTimer;
    public boolean fastMode;

    public GameScreen() {
        super();

        userInterface = new UserInterface();
        mouse = new Mouse();

        galaxyList = Galaxy.loadDebugGalaxy();

        Planet startPlanet = (Planet) galaxyList.get("Cotton Tail Nebula").solarSystemMap.get("Lago Morpha").planetoidList.get(1);
        startPlanet.generateOverworld();

        Location startLocation = startPlanet.areaMap.get("Center Of The Universe").roomList.get(0).location;
        //startLocation = startPlanet.areaMap.get("Overworld Area 4").roomList.get(0).location;
        player = new Player(startLocation);

        if(((Planet) player.location.planetoid).areaMap.containsKey("Overworld Area 1")) {
            userInterface.map.buffer(((Planet) player.location.planetoid).areaMap.get("Overworld Area 1").roomList.get(0).location);
        }
        if(!player.location.area.mapKey.equals("Overworld")) {
            userInterface.map.buffer(player.location);
        }
        userInterface.map.updateOffset(player.location.room);

        // Start Game Time At Noon //
        if(true) {
            player.location.planetoid.minuteCountDay = player.location.planetoid.minutesInDay / 2;
            player.location.planetoid.minuteCountYear = player.location.planetoid.minuteCountDay;
            ((Planet) (player.location.planetoid)).updateDayNightTimers();
        }

        gameTimer = 0;
        frameTimer = 0;
        fastMode = false;

        initInputProcessor();
    }

    public void initInputProcessor() {
        Gdx.input.setInputProcessor(new InputAdapter() {

            // Mouse Input - Scroll Console //
            @Override
            public boolean scrolled(float amountX, float amountY) {
                if(mouse.hoverUIElement != null
                && mouse.hoverUIElement.toString().equals("Console")){
                    userInterface.console.scroll((int) amountY * -1);
                }

                else if(mouse.hoverUIElement != null
                && mouse.hoverUIElement.toString().equals("Map")) {
                    userInterface.map.zoom((int) amountY);
                }

                return false;
            }

            // Keyboard Input //
            @Override
            public boolean keyDown(int keyCode) {
                String key = Input.Keys.toString(keyCode);
                boolean shiftIsPressed = Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT) || Gdx.input.isKeyPressed(Input.Keys.SHIFT_RIGHT);
                boolean controlIsPressed = Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT) || Gdx.input.isKeyPressed(Input.Keys.CONTROL_RIGHT);

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

                // Toggle Fast Mode //
                else if(controlIsPressed
                && key.equals("F")) {
                    fastMode = !fastMode;
                }

                // InputBar Input //
                else if(InputBar.inputCharString.contains(key)
                && !controlIsPressed
                && !shiftIsPressed) {
                    userInterface.inputBar.concatinateUserInput(key);
                }

                // Reset Backspace Timer //
                else if(key.equals("Delete")
                && !controlIsPressed) {
                    userInterface.inputBar.backspaceTimer = 0;
                }

                // Enter User Input //
                else if(key.equals("Enter")
                && !controlIsPressed) {
                    if(!userInterface.inputBar.userInput.isEmpty()) {
                        processUserInput(userInterface.inputBar.userInput);
                        userInterface.inputBar.enterUserInput();
                    }
                }

                // Change Facing Direction //
                else if(controlIsPressed
                && shiftIsPressed
                && (key.equals("Left") || key.equals("Right"))) {
                    GameScreen.player.changeFacingDirection(key);
                }

                // Alt. Movement (Control + Arrow Keys) //
                else if(controlIsPressed
                && (key.equals("Up") || key.equals("Down") || key.equals("Left") || key.equals("Right"))) {
                    String targetDirection = "";
                    if(key.equals("Up")) {targetDirection = "North";}
                    else if(key.equals("Down")) {targetDirection = "South";}
                    else if(key.equals("Left")) {targetDirection = "West";}
                    else if(key.equals("Right")) {targetDirection = "East";}

                    Move moveAction = new Move(player);
                    player.interruptAction();

                    moveAction.actionType = "Direction";
                    moveAction.targetDirection = targetDirection;
                    moveAction.initiate();
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
        mouse.update();
        userInterface.inputBar.update();
        userInterface.console.update();

        // Update Solar System //
        if(frameTimer == 0
        && player.location != null
        && player.location.solarSystem != null) {
            player.location.solarSystem.update();
        }

        // Update Player //
        player.update();

        // Update Player, Area & Surrounding Rooms //
        if((frameTimer == 0 || frameTimer == 30)
        && player.location != null
        && player.location.area != null
        && player.location.room != null) {
            AreaAndRoomData surroundingAreaAndRoomData = AreaAndRoomData.getSurroundingAreaAndRoomData(player.location.room, player.getMaxViewDistance(), true, true);
            for(Area area : surroundingAreaAndRoomData.areaList) {area.update();}
            for(Room room : surroundingAreaAndRoomData.roomList) {room.update();}
        }

        // Update Game & Frame Timers //
        gameTimer += 1;
        if(!fastMode) {frameTimer += 1;}
        if(fastMode || frameTimer >= 60) {
            frameTimer = 0;
        }

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
                if(!targetAction.toString().equals("CombatAction")
                && targetAction.targetCount == 0) {targetAction.targetCount = 1;}

                if(targetAction.parentEntity != null) {
                    targetAction.initiate();
                }

                actionCheck = true;
                break;
            }
        }

        if(!actionCheck) {
            int messageIndex = new Random().nextInt(3);
            if(messageIndex == 0) {
                userInterface.console.writeToConsole(new Line("Huh?", "3CONT1DY", "", true, true));
            }
            else if(messageIndex == 1) {
                userInterface.console.writeToConsole(new Line("I didn't quite get that.", "2W4CONT1DY2DDW6CONT4CONT4CONT1DY", "", true, true));
            }
            else {
                userInterface.console.writeToConsole(new Line("Care to try that again?", "5CONT3CONT4CONT5CONT5CONT1DY", "", true, true));
            }

            // userInterface.console.writeToConsole(new Line("This is a test line.", "19CONTR1Y", "", true, true));
            // userInterface.console.writeToConsole(new Line("This is a test.", "14CONTR1Y", "", true, true));
            // userInterface.console.writeToConsole(new Line("This is.", "7CONT1Y", "", true, true));
            // userInterface.console.writeToConsole(new Line("This.", "4CONT1Y", "", true, true));
            // userInterface.console.writeToConsole(new Line("This is a test line. It is really long. But not too long, or it would be way too long.", "85SHIBR1Y", "86GRABB", true, true));
            // userInterface.console.writeToConsole(new Line("This is a test line. It is super long so I can test all the colors. Yo this should be multiple lines, yo.", "104SHIBR1Y", "105GRABB-Y-G", true, true));
            // userInterface.console.writeToConsole(new Line("This is a test line. It is super long so I can test all the colors. Yo this should be multiple lines, yo.", "104SHIBR1Y", "105GRABB-Y", true, true));
            // userInterface.console.writeToConsole(new Line("This is a test line. It is super long so I can test all the colors. Yo this should be multiple lines, yo.", "104SHIBR1Y", "105GRABB", true, true));
            // userInterface.console.writeToConsole(new Line("This is a test line. It is super long so I can test all the colors. Yo this should be multiple lines, yo.", "104SHIBR1Y", "105GRAB", true, true));
        }
    }

    public void dispose() {
        userInterface.dispose();
    }
}
