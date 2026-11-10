package dev.cobaltgate.model;
import jakarta.persistence.*;
import java.time.Instant;
@Entity @Table(name="memberships",uniqueConstraints=@UniqueConstraint(columnNames={"subscription_id","email"}))
public class Membership {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @ManyToOne(fetch=FetchType.LAZY,optional=false) @JoinColumn(name="subscription_id") private Subscription subscription;
 @Column(nullable=false,length=240) private String email;
 @Column(name="created_at",insertable=false,updatable=false) private Instant createdAt;
 protected Membership(){}
 public Membership(Subscription subscription, String email){this.subscription=subscription;this.email=email;}
 public Long getId(){return id;}
 public String getEmail(){return email;}
}
