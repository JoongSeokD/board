package me.ljseokd.basicboard.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@EqualsAndHashCode(of = "id")
@NoArgsConstructor(access = PROTECTED)
public class Account {

    @Id @GeneratedValue
    @Column(name = "account_id")
    private Long id;

    @Column(unique = true)
    private String name;
    private String password;

    @OneToMany(mappedBy = "account")
    private List<Notice> notices = new ArrayList<>();

    public Account(String name, String password) {
        this.name = name;
        this.password = password;
    }
}
