package com.transcendensoft.hedbanz.data.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PlayerGuessingDTO {
    @SerializedName("player")
    @Expose
    private UserDTO player;
    @SerializedName("attempt")
    @Expose
    private Integer attempts;
    @SerializedName("questionId")
    @Expose
    private Long questionId;

    public PlayerGuessingDTO() {
    }

    public PlayerGuessingDTO(UserDTO player, Integer attempts, Long questionId) {
        this.player = player;
        this.attempts = attempts;
        this.questionId = questionId;
    }

    public UserDTO getPlayer() {
        return player;
    }

    public void setPlayer(UserDTO player) {
        this.player = player;
    }

    public Integer getAttempts() {
        return attempts;
    }

    public void setAttempts(Integer attempts) {
        this.attempts = attempts;
    }

    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    public static PlayerGuessingDtoBuilder Builder(){
        return new PlayerGuessingDTO(). new PlayerGuessingDtoBuilder();
    }

    public class PlayerGuessingDtoBuilder{
        private PlayerGuessingDtoBuilder(){}

        public PlayerGuessingDtoBuilder setPlayer(UserDTO player){
            PlayerGuessingDTO.this.player = player;
            return this;
        }

        public PlayerGuessingDtoBuilder setAttempts(Integer attempts){
            PlayerGuessingDTO.this.attempts = attempts;
            return this;
        }

        public PlayerGuessingDtoBuilder setQuestionId(Long questionId){
            PlayerGuessingDTO.this.questionId = questionId;
            return this;
        }

        public PlayerGuessingDTO build(){
            return PlayerGuessingDTO.this;
        }
    }
}
