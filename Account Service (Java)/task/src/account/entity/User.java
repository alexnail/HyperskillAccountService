package account.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private String name;
    private String lastname;

    @Column(unique = true)
    private String email;
    private String password;

    @OneToMany(mappedBy = "employee")
    private Set<Payment> payments;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user2group")
    private Set<Group> groups;
}
