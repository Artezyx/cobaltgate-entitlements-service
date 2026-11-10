package dev.cobaltgate;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api/v1")
public class EntitlementController {
    private final Map<UUID, Subscription> subscriptions = new ConcurrentHashMap<>();

    public record Subscribe(@NotBlank String accountId, @NotBlank String plan, @Positive int seats) {}
    public record Subscription(UUID id, String accountId, String plan, int seats, boolean active) {}

    @GetMapping("/health")
    public Map<String, String> health() { return Map.of("status", "ok"); }

    @PostMapping("/subscriptions")
    public ResponseEntity<Subscription> subscribe(@Valid @RequestBody Subscribe request) {
        if (!request.plan().equals("starter") && !request.plan().equals("business")) {
            return ResponseEntity.badRequest().build();
        }
        var subscription = new Subscription(UUID.randomUUID(), request.accountId(), request.plan(), request.seats(), true);
        subscriptions.put(subscription.id(), subscription);
        return ResponseEntity.created(URI.create("/api/v1/subscriptions/" + subscription.id())).body(subscription);
    }

    @GetMapping("/subscriptions/{id}")
    public ResponseEntity<Subscription> get(@PathVariable UUID id) {
        Subscription record = subscriptions.get(id);
        return record == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(record);
    }
}
