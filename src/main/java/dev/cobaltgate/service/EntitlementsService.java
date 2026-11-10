package dev.cobaltgate.service;
import dev.cobaltgate.model.*;
import dev.cobaltgate.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.NoSuchElementException;
import java.util.Locale;

@Service
public class EntitlementsService {
 private final SubscriptionRepository subscriptions;
 private final MembershipRepository members;
 private final AuditRepository audits;
 public EntitlementsService(SubscriptionRepository subscriptions,MembershipRepository members,AuditRepository audits) {
  this.subscriptions=subscriptions;this.members=members;this.audits=audits;
 }
 @Transactional
 public Subscription open(String company,int seats) {
  if(seats<=0) throw new IllegalArgumentException("Seat limit must be positive");
  var subscription=subscriptions.save(new Subscription(company.trim(),seats));
  audits.save(new AuditEvent(subscription,"subscription.created","Seats: "+seats));
  return subscription;
 }
 @Transactional(readOnly=true)
 public Subscription find(Long id){return subscriptions.findById(id).orElseThrow(()->new NoSuchElementException("Subscription not found"));}
 @Transactional
 public Membership grantSeat(Long id,String email) {
  // Lock the parent row first to serialize seat assignment and avoid oversubscription.
  var subscription=subscriptions.findLockedById(id).orElseThrow(()->new NoSuchElementException("Subscription not found"));
  var canonical=email.trim().toLowerCase(Locale.ROOT);
  var existing=members.findBySubscriptionIdAndEmail(id,canonical);
  if(existing.isPresent()) return existing.get();
  subscription.reserveSeat();
  var member=members.save(new Membership(subscription,canonical));
  audits.save(new AuditEvent(subscription,"member.granted",canonical));
  return member;
 }
 @Transactional
 public Subscription expand(Long id,int limit) {
  var subscription=subscriptions.findLockedById(id).orElseThrow(()->new NoSuchElementException("Subscription not found"));
  subscription.grow(limit);
  audits.save(new AuditEvent(subscription,"subscription.expanded","New limit: "+limit));
  return subscription;
 }
}
