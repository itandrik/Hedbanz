package com.transcendensoft.hedbanz.domain.entity;

import javax.sql.DataSource;

public enum PlayerStatus {
    ACTIVE(1),
    AFK(2),
    LEFT(3),
    UNDEFINED(100500);

    private int code;

    PlayerStatus(int code){
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static PlayerStatus getStatusByCode(int code){
        for (PlayerStatus playerStatus: PlayerStatus.values()) {
            if(playerStatus.getCode() == code)return playerStatus;
        }

        return UNDEFINED;
    }
}
