package dev.cobaltgate.model;
import jakarta.persistence.*;
import java.time.Instant;
@Entity @Table(name="subscriptions")
public class Subscription {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @Column(nullable=false,length=140) private String company;
 @Column(name="seat_limit",nullable=false) private int seatLimit;
 @Column(name="seats_used",nullable=false) private int seatsUsed;
 @Version @Column(nullable=false) private long version;
 @Column(name="created_at",insertable=false,updatable=false) private Instant createdAt;
 protected Subscription() {}
 public Subscription(String company,int seatLimit) { this.company=company; this.seatLimit=seatLimit; }
 public Long getId(){return id;}
 public String getCompany(){return company;}
 public int getSeatLimit(){return seatLimit;}
 public int getSeatsUsed(){return seatsUsed;}
 public long getVersion(){return version;}
 public Instant getCreatedAt(){return createdAt;}
 public void reserveSeat(){ if(seatsUsed>=seatLimit) throw new IllegalStateException("Seat capacity reached"); seatsUsed++; }
 public void grow(int newLimit){ if(newLimit<seatLimit) throw new IllegalArgumentException("Cannot decrease seat entitlement"); seatLimit=newLimit; }
}
