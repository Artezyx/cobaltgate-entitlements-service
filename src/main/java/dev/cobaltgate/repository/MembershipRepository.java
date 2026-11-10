package dev.cobaltgate.repository;
import dev.cobaltgate.model.Membership;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
public interface MembershipRepository extends JpaRepository<Membership,Long> {
 Optional<Membership> findBySubscriptionIdAndEmail(Long subscriptionId, String email);
}
