package jp.co.axa.apidemo.infrastructure.rest.assembler;

import static org.junit.jupiter.api.Assertions.assertEquals;

import jp.co.axa.apidemo.entities.Employee;
import jp.co.axa.apidemo.infrastructure.rest.model.EmployeeResponse;
import org.junit.jupiter.api.Test;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.LinkRelation;

class EmployeeAssemblerTest {

  private EmployeeAssembler employeeAssembler = new EmployeeAssembler();
  @Test
  void shouldMapEmployeeIntoHateoasEmployeeResponse() {

    Employee employee = new Employee(1L, "Test Name", 9000, "QA");
    EntityModel<EmployeeResponse> employeeResource = employeeAssembler.toModel(employee);


    assertEquals(
        new EmployeeResponse(
            employee.getId(),
            employee.getName(),
            employee.getSalary(),
            employee.getDepartment()),
        employeeResource.getContent(),
        "Hal content is populated with correct mapped employee response");

    assertEquals(1, employeeResource.getLinks().stream().count());
    assertEquals(
        "/api/v1/employees/1",
        employeeResource.getLinks().getLink(LinkRelation.of("self")).get().getHref(),
        "Employee response has a proper Self link"
    );
  }
}