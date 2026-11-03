package fr.asr.calculator.data.entity;

import static fr.asr.calculator.constant.ApplicationConstant.SCHEMA_NAME;

import jakarta.persistence.*;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(schema = SCHEMA_NAME)
public class Profile extends Auditable implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String company;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column(nullable = false)
    private Boolean enabled;

    @OneToOne
    @JoinColumn(name = "manager_id")
    private Profile manager;

    public enum Role {
        ADMIN,
        MANAGER,
        USER;
    }
}
