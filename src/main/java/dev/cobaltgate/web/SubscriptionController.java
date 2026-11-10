package dev.cobaltgate.web;
import dev.cobaltgate.model.*;
import dev.cobaltgate.service.EntitlementsService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;

@RestController @RequestMapping("/api/v1/subscriptions")
public class SubscriptionController {
 private final EntitlementsService service;
 public SubscriptionController(EntitlementsService service){this.service=service;}
 public record Create(@NotBlank @Size(max=140) String company,@Min(1) @Max(10000) int seatLimit){}
 public record Grant(@NotBlank @Email @Size(max=240) String email){}
 public record Expand(@Min(1) @Max(10000) int seatLimit){}
 public record View(Long id,String company,int seatLimit,int seatsUsed,long version) {
  static View from(Subscription s) {return new View(s.getId(),s.getCompany(),s.getSeatLimit(),s.getSeatsUsed(),s.getVersion());}
 }
 public record MemberView(Long id,String email) {static MemberView from(Membership m){return new MemberView(m.getId(),m.getEmail());}}
 @PostMapping
 public ResponseEntity<View> open(@Valid @RequestBody Create create){var s=service.open(create.company(),create.seatLimit());return ResponseEntity.created(URI.create("/api/v1/subscriptions/"+s.getId())).body(View.from(s));}
 @GetMapping("/{id}") public View find(@PathVariable Long id){return View.from(service.find(id));}
 @PostMapping("/{id}/members") public ResponseEntity<MemberView> grant(@PathVariable Long id,@Valid @RequestBody Grant grant){var m=service.grantSeat(id,grant.email());return ResponseEntity.ok(MemberView.from(m));}
 @PatchMapping("/{id}/limit") public View expand(@PathVariable Long id,@Valid @RequestBody Expand limit){return View.from(service.expand(id,limit.seatLimit()));}
}
