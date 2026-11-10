package dev.cobaltgate.model;
import jakarta.persistence.*;
import java.time.Instant;
@Entity @Table(name="audit_log")
public class AuditEvent {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @ManyToOne(fetch=FetchType.LAZY,optional=false) @JoinColumn(name="subscription_id") private Subscription subscription;
 @Column(nullable=false,length=100) private String action;
 @Column(nullable=false,length=255) private String detail;
 @Column(name="created_at",insertable=false,updatable=false) private Instant createdAt;
 protected AuditEvent(){}
 public AuditEvent(Subscription subscription,String action,String detail){this.subscription=subscription;this.action=action;this.detail=detail;}
}
