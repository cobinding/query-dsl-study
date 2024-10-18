package study.querydsl.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // JPA에서 만들어주는 protected 수준의 기본 생성자
@ToString(of = {"id", "name"})
public class Team {

    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;
    private String teamName;

    @OneToMany(mappedBy = "team")
    List<Member> members = new ArrayList<>();

    public Team(String teamName) {
        this.teamName = teamName;
    }
}
