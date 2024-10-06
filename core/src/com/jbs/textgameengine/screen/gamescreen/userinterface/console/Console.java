package com.jbs.textgameengine.screen.gamescreen.userinterface.console;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.jbs.textgameengine.Settings;
import com.jbs.textgameengine.screen.Screen;
import com.jbs.textgameengine.screen.gamescreen.userinterface.UserInterfaceElement;
import com.jbs.textgameengine.screen.gamescreen.userinterface.console.line.Line;
import com.jbs.textgameengine.screen.utility.Rect;

import java.util.ArrayList;

public class Console extends UserInterfaceElement {
    public static int padding = 10;

    public BitmapFont font;
    public GlyphLayout glyphLayout;

    public ArrayList<Line> lineList;
    public ArrayList<Line> lineBufferList;
    public int updateTimer;

    public Console() {
        font = new BitmapFont(Gdx.files.internal("fonts/Consolas_28.fnt"), Gdx.files.internal("fonts/Consolas_28.png"), false);
        font.setFixedWidthGlyphs("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890!@#$%^&*()-=_+[]{};':\",<.>/? ");
        glyphLayout = new GlyphLayout(font, " ");

        lineList = new ArrayList<>();
        lineBufferList = new ArrayList<>();
        updateTimer = 0;

        int consoleX = 0;
        int consoleY = Settings.INPUT_BAR_HEIGHT;
        int consoleWidth = Settings.INPUT_BAR_WIDTH;
        int consoleHeight = Settings.WINDOW_HEIGHT - Settings.ROOM_VIEW_HEIGHT - Settings.INPUT_BAR_HEIGHT;
        rect = new Rect(consoleX, consoleY, consoleWidth, consoleHeight);

        rect.fillColor = new Color(0/255f, 0/255f, 10/255f, 1);
    }

    public void update() {
        if(!lineBufferList.isEmpty()) {
            updateTimer += 1;
            if(updateTimer >= 1) {
                updateTimer = 0;
                lineList.add(0, lineBufferList.get(0));
                lineBufferList.remove(0);
            }
        }
    }

    public void render() {
        super.render();

        int lineX = padding;
        int lineY = (int) rect.y + (int) glyphLayout.height + padding;

        for(int i = 0; i < lineList.size(); i++) {
            Line line = lineList.get(i);

            if(i != 0) {
                lineY += (int) glyphLayout.height + line.getSpaceHeight();
            }

            Line.draw(line, lineX, lineY, font, Screen.camera);

            if(i == lineList.size() - 1
            || lineY + glyphLayout.height + lineList.get(i + 1).getSpaceHeight() >= rect.y + rect.height - padding) {
                break;
            }
        }
    }

    public void writeToConsole(Line line) {
        int maxLineCharCount = (rect.width - (padding * 2)) / (int) glyphLayout.width;

        if(line.label.length() > maxLineCharCount) {
            ArrayList<Line> wrappedLineList = Line.wrap(line, maxLineCharCount);

            for(int i = 0; i < wrappedLineList.size(); i++) {
                Line wrappedLine = wrappedLineList.get(i);
                lineBufferList.add(wrappedLine);
            }
        } else {
            lineBufferList.add(line);
        }
    }
}
