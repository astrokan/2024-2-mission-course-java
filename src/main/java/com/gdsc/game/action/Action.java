package com.gdsc.game.action;


public interface Action {

    String getName(); // 행동 이름 반환
    int execute(); // 행동 실행

    int getMinDamage();
    int getMaxDamage();
}
