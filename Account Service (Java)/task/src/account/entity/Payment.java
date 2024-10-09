package account.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "payments",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"employee", "period"})})
@Getter
@Setter
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "employee", referencedColumnName = "email")
    private User employee;

    private String period;

    private Long salary;
}
