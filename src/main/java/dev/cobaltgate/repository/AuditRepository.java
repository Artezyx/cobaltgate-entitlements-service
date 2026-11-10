package dev.cobaltgate.repository;
import dev.cobaltgate.model.AuditEvent;
import org.springframework.data.jpa.repository.JpaRepository;
public interface AuditRepository extends JpaRepository<AuditEvent,Long> {}
