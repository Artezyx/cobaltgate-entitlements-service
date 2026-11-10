package dev.cobaltgate.repository;
import dev.cobaltgate.model.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import jakarta.persistence.LockModeType;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Query;
import java.util.Optional;
public interface SubscriptionRepository extends JpaRepository<Subscription,Long> {
 @Lock(LockModeType.PESSIMISTIC_WRITE)
 @Query("select s from Subscription s where s.id = :id")
 Optional<Subscription> findLockedById(@Param("id") Long id);
}
