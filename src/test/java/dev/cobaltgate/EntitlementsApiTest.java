package dev.cobaltgate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest(properties={
 "spring.datasource.url=jdbc:h2:mem:cobalt;MODE=MySQL;DB_CLOSE_DELAY=-1",
 "spring.datasource.driver-class-name=org.h2.Driver",
 "spring.datasource.username=sa", "spring.datasource.password=",
 "spring.jpa.hibernate.ddl-auto=validate", "DEMO_ADMIN_USER=portfolio","DEMO_ADMIN_PASSWORD=ChangeMeLocal"})
@AutoConfigureMockMvc
class EntitlementsApiTest {
 @Autowired MockMvc mvc;
 @Autowired ObjectMapper json;

 @Test void requiresAuthentication() throws Exception {
  mvc.perform(get("/api/v1/subscriptions/1")).andExpect(status().isUnauthorized());
 }
 @Test void allocatesAtMostLicensedSeats() throws Exception {
  MvcResult created = mvc.perform(post("/api/v1/subscriptions").with(httpBasic("portfolio","ChangeMeLocal"))
   .contentType(MediaType.APPLICATION_JSON).content("{\"company\":\"Nova Cloud\",\"seatLimit\":1}"))
   .andExpect(status().isCreated()).andReturn();
  long id=json.readTree(created.getResponse().getContentAsString()).get("id").asLong();
  String url="/api/v1/subscriptions/"+id+"/members";
  mvc.perform(post(url).with(httpBasic("portfolio","ChangeMeLocal"))
   .contentType(MediaType.APPLICATION_JSON).content("{\"email\":\"A@EXAMPLE.org\"}"))
   .andExpect(status().isOk()).andExpect(jsonPath("$.email").value("a@example.org"));
  mvc.perform(post(url).with(httpBasic("portfolio","ChangeMeLocal"))
   .contentType(MediaType.APPLICATION_JSON).content("{\"email\":\"a@example.org\"}"))
   .andExpect(status().isOk());
  mvc.perform(post(url).with(httpBasic("portfolio","ChangeMeLocal"))
   .contentType(MediaType.APPLICATION_JSON).content("{\"email\":\"b@example.org\"}"))
   .andExpect(status().isConflict());
  mvc.perform(get("/api/v1/subscriptions/"+id).with(httpBasic("portfolio","ChangeMeLocal")))
   .andExpect(status().isOk()).andExpect(jsonPath("$.seatsUsed").value(1));
 }
}
