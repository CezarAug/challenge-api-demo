package jp.co.axa.apidemo.integration;

import static jp.co.axa.apidemo.utils.TestUtils.getJson;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SqlGroup({
    @Sql(value = "classpath:db/import.sql", executionPhase = BEFORE_TEST_METHOD)
})
class WebApplicationTest {

  @Autowired
  MockMvc mockMvc;

  @Test
  void shouldReturn401ForUnauthorizedRequests() throws Exception {
    this.mockMvc.perform(get("/")).andDo(print()).andExpect(status().isUnauthorized())
        .andExpect(content().string(containsString("")));
  }

  @Test
  @WithMockUser(roles = "USER")
  void shouldGetEmployeeById() throws Exception {
    this.mockMvc.perform(get("/api/v1/employees/1"))
        .andDo(print()).andExpect(status().isOk())
        .andExpect(content().contentType(MediaTypes.HAL_JSON))
        .andExpect(content().string(getJson("json/getEmployeeResponse.json")));
  }

  @Test
  @WithMockUser(roles = "USER")
  void shouldDeleteEmployeeById() throws Exception {
    this.mockMvc.perform(delete("/api/v1/employees/1"))
        .andDo(print()).andExpect(status().isOk())
        .andExpect(content().string(""));
  }

  @Test
  @WithMockUser(roles = "USER")
  void shouldPostEmployee() throws Exception {
    this.mockMvc.perform(
        post("/api/v1/employees")
            .contentType(MediaType.APPLICATION_JSON)
            .content(getJson("json/getEmployeeResponse.json"))
        )
        .andDo(print()).andExpect(status().isOk())
        .andExpect(content().string(""));
  }

  @Test
  @WithMockUser(roles = "USER")
  void shouldGetAllEmployees() throws Exception {
    this.mockMvc.perform(
            get("/api/v1/employees?page=0&size=10")
                .contentType(MediaType.APPLICATION_JSON)
                .content(getJson("json/getEmployeeResponse.json"))
        )
        .andDo(print()).andExpect(status().isOk())
        .andExpect(content().contentType(MediaTypes.HAL_JSON))
        .andExpect(content().string(getJson("json/getAllEmployeesResponse.json")));
  }

  @Test
  @WithMockUser(roles = "USER")
  void shouldPutEmployee() throws Exception {
    this.mockMvc.perform(
            put("/api/v1/employees/2")
                .contentType(MediaType.APPLICATION_JSON)
                .content(getJson("json/postEmployeeRequest.json"))
        )
        .andDo(print()).andExpect(status().isOk())
        .andExpect(content().string(""));

    this.mockMvc.perform(get("/api/v1/employees/2"))
        .andDo(print()).andExpect(status().isOk())
        .andExpect(content().contentType(MediaTypes.HAL_JSON))
        .andExpect(content().string(getJson("json/getUpdatedEmployeeResponse.json")));
  }


  @Test
  @WithMockUser(roles = "USER")
  void shouldRejectPostInvalidEmployee() throws Exception {
    this.mockMvc.perform(
            post("/api/v1/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(getJson("json/postEmployeeInvalidRequest.json"))
        )
        .andDo(print()).andExpect(status().isBadRequest())
        .andExpect(content().string(getJson("json/postEmployeeInvalidResponse.json")));
  }


  @Test
  @WithMockUser(roles = "USER")
  void shouldRejectDeleteInvalidEmployeeById() throws Exception {
    this.mockMvc.perform(delete("/api/v1/employees/10000000"))
        .andDo(print()).andExpect(status().isBadRequest())
        .andExpect(content().string(getJson("json/deleteOrPutEmployeeInvalidResponse.json")));
  }

  @Test
  @WithMockUser(roles = "USER")
  void shouldRejectPutInvalidEmployeeById() throws Exception {
    this.mockMvc.perform(delete("/api/v1/employees/10000000"))
        .andDo(print()).andExpect(status().isBadRequest())
        .andExpect(content().string(getJson("json/deleteOrPutEmployeeInvalidResponse.json")));
  }

  @Test
  @WithMockUser(roles = "USER")
  void shouldRejectGetInvalidEmployeeById() throws Exception {
    this.mockMvc.perform(get("/api/v1/employees/invalid"))
        .andDo(print()).andExpect(status().isBadRequest())
        .andExpect(content().string(getJson("json/getEmployeeInvalidResponse.json")));
  }

  @Test
  @WithMockUser(roles = "USER")
  void shouldRejectGetEmployeesWithInvalidQueryParams() throws Exception {
    this.mockMvc.perform(get("/api/v1/employees?page=0&size=0"))
        .andDo(print()).andExpect(status().isBadRequest())
        .andExpect(content().string(getJson("json/getAllEmployeesInvalidResponse.json")));
  }

  @Test
  @WithMockUser(roles = "USER")
  void shouldGetEmptyEmployeeById() throws Exception {
    this.mockMvc.perform(get("/api/v1/employees/90000"))
        .andDo(print()).andExpect(status().isOk())
        .andExpect(content().string(""));
  }

}
