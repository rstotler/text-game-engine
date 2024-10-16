package com.jbs.textgameengine.gamedata.entity.mob.action.menu;

import com.jbs.textgameengine.gamedata.entity.mob.Mob;
import com.jbs.textgameengine.gamedata.entity.mob.action.Action;
import com.jbs.textgameengine.gamedata.entity.mob.properties.skill.Skill;
import com.jbs.textgameengine.screen.gamescreen.GameScreen;
import com.jbs.textgameengine.screen.gamescreen.userinterface.console.line.Line;

import java.util.*;
import java.util.stream.Collectors;

public class Skills extends Action {
    public Skills(Mob parentEntity) {
        super(parentEntity);
    }

    public Skills() {
        this(null);
    }

    public Action getActionFromInput(String input, Mob parentEntity) {
        ArrayList<String> inputList = new ArrayList<>(Arrays.asList(input.split(" ")));

        if(Arrays.asList("skills", "skill", "skil", "ski", "sk").contains(inputList.get(0))) {
            Skills skillsAction = new Skills(parentEntity);

            // Get Target Skill Group Data //
            String targetSkillGroup = "";
            if(inputList.size() > 1) {
                List<String> targetSkillGroupRawStringList = inputList.subList(1, inputList.size());
                ArrayList<String> targetSkillGroupStringList = new ArrayList<>();
                for(int i = 0; i < targetSkillGroupRawStringList.size(); i++) {
                    String targetSkillGroupStringRaw = targetSkillGroupRawStringList.get(i);
                    String targetSkillGroupString = targetSkillGroupStringRaw.substring(0, 1).toLowerCase();
                    if(targetSkillGroupStringRaw.length() > 1) {
                        targetSkillGroupString += targetSkillGroupStringRaw.substring(1);
                    }
                    targetSkillGroupStringList.add(targetSkillGroupString);

                }
                String targetSkillGroupRaw = targetSkillGroupStringList.stream().collect(Collectors.joining(" "));
                if(parentEntity.skillMap.containsKey(targetSkillGroupRaw)) {
                    targetSkillGroup = targetSkillGroupRaw;
                }
            }

            // Skills SkillGroup //
            if(!targetSkillGroup.isEmpty()) {
                skillsAction.targetEntityString = targetSkillGroup;
            }

            return skillsAction;
        }

        return null;
    }

    public void initiate() {
        ArrayList<String> skillGroupList = new ArrayList<>();
        int columnWidth = 26;

        if(!targetEntityString.isEmpty()) {skillGroupList.add(targetEntityString);}
        else {skillGroupList.addAll(parentEntity.skillMap.keySet());}

        for(String targetSkillGroup : skillGroupList) {
            GameScreen.userInterface.console.writeToConsole(new Line(targetSkillGroup + " Skills:", String.valueOf(targetSkillGroup.length()) + "CONT1W6CONT1DY", "", false, true));

            boolean isLastLine = false;
            String skillString = "";
            String skillColorCode = "";

            for(int i = 0; i < parentEntity.skillMap.get(targetSkillGroup).size(); i++) {
                Skill skill = parentEntity.skillMap.get(targetSkillGroup).get(i);
                if(i == parentEntity.skillMap.get(targetSkillGroup).size() - 1) {isLastLine = true;}

                skillString += skill.name.label;
                skillColorCode += skill.name.colorCode;

                int spaceSize = columnWidth - skill.name.label.length();
                skillString += Line.spaceString.substring(0, spaceSize);
                skillColorCode += String.valueOf(spaceSize) + "W";

                if((i + 1) % 3 == 0 || isLastLine) {
                    GameScreen.userInterface.console.writeToConsole(new Line(skillString, skillColorCode, "", isLastLine, true));
                    skillString = "";
                    skillColorCode = "";
                }
            }
        }
    }
}
