package com.gdsc.game.match.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

@Getter
public class PlayerCreateRequest {
    @Length(max = 20, message = "content length up to 20")
    private String playerName;

    @Length(max = 20, message = "content length up to 20")
    private String jobName;

    @NotNull
    @Positive(message = "Value must be positive")
    private int level;

}
