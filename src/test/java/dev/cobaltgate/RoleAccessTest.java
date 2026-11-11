package dev.cobaltgate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(properties={
 "spring.datasource.url=jdbc:h2:mem:cobalt_roles;MODE=MySQL;DB_CLOSE_DELAY=-1",
 "spring.datasource.driver-class-name=org.h2.Driver",
 "spring.datasource.username=sa","spring.datasource.password=",
 "spring.jpa.hibernate.ddl-auto=validate",
 "DEMO_ADMIN_USER=portfolio","DEMO_ADMIN_PASSWORD=ChangeMeLocal",
 "DEMO_READER_USER=read-only","DEMO_READER_PASSWORD=ChangeMeReaderLocal"})
@AutoConfigureMockMvc
class RoleAccessTest {
 @Autowired MockMvc mvc;

 @Test void readerCannotCreateOrModifySubscriptions() throws Exception {
  mvc.perform(post("/api/v1/subscriptions")
   .with(httpBasic("read-only","ChangeMeReaderLocal"))
   .contentType(MediaType.APPLICATION_JSON)
   .content("{\"company\":\"Forbidden\",\"seatLimit\":4}"))
   .andExpect(status().isForbidden());
  mvc.perform(patch("/api/v1/subscriptions/1/limit")
   .with(httpBasic("read-only","ChangeMeReaderLocal"))
   .contentType(MediaType.APPLICATION_JSON)
   .content("{\"seatLimit\":10}"))
   .andExpect(status().isForbidden());
 }

 @Test void readerCanReadButUnauthenticatedUsersCannot() throws Exception {
  mvc.perform(get("/api/v1/subscriptions/999")
   .with(httpBasic("read-only","ChangeMeReaderLocal")))
   .andExpect(status().isNotFound());
  mvc.perform(get("/api/v1/subscriptions/999"))
   .andExpect(status().isUnauthorized());
 }

 @Test void invalidCredentialsAreRejected() throws Exception {
  mvc.perform(get("/api/v1/subscriptions/999")
   .with(httpBasic("portfolio","wrong-password")))
   .andExpect(status().isUnauthorized());
 }
}
