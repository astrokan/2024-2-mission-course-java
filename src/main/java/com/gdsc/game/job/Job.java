package com.gdsc.game.job;

import com.gdsc.game.action.skill.Skill;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@NoArgsConstructor
@Getter
public class Job {

    @Id
    @Column(name = "job_name")
    private String name;

    @OneToMany(mappedBy = "job")
    private List<Skill> skills;  // 직업이 가진 스킬들

}
