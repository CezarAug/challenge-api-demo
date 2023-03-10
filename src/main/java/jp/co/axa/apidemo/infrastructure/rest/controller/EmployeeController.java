package jp.co.axa.apidemo.infrastructure.rest.controller;

import java.util.Optional;
import jp.co.axa.apidemo.entities.Employee;
import jp.co.axa.apidemo.infrastructure.mapper.EmployeeMapper;
import jp.co.axa.apidemo.infrastructure.rest.api.EmployeeApi;
import jp.co.axa.apidemo.infrastructure.rest.assembler.EmployeeAssembler;
import jp.co.axa.apidemo.infrastructure.rest.model.EmployeeRequest;
import jp.co.axa.apidemo.infrastructure.rest.model.EmployeeResponse;
import jp.co.axa.apidemo.services.EmployeeService;
import jp.co.axa.apidemo.utils.SortDirection;
import jp.co.axa.apidemo.utils.SortableEmployeeColumns;
import org.mapstruct.factory.Mappers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/v1",
    produces = MediaTypes.HAL_JSON_VALUE)
public class EmployeeController implements EmployeeApi {

  private final EmployeeService employeeService;

  private final EmployeeAssembler employeeAssembler;

  private final PagedResourcesAssembler<Employee> pagedResourcesAssembler;

  private final Logger log = LoggerFactory.getLogger(EmployeeController.class);
  EmployeeMapper mapper = Mappers.getMapper(EmployeeMapper.class);

  public EmployeeController(EmployeeService employeeService,
                            EmployeeAssembler employeeAssembler,
                            PagedResourcesAssembler<Employee> pagedResourcesAssembler) {
    this.employeeService = employeeService;
    this.employeeAssembler = employeeAssembler;
    this.pagedResourcesAssembler = pagedResourcesAssembler;
  }

  @GetMapping("/employees")
  public ResponseEntity<PagedModel<EntityModel<EmployeeResponse>>> getEmployees(
      Integer page,
      Integer size,
      SortableEmployeeColumns orderBy,
      SortDirection sortDirection
  ) {
    return new ResponseEntity<>(
        pagedResourcesAssembler.toModel(
            employeeService.getAllEmployees(page, size, orderBy, sortDirection),
            employeeAssembler),
        HttpStatus.OK);
  }

  @GetMapping(value = "/employees/{employeeId}")
  public ResponseEntity<EntityModel<EmployeeResponse>> getEmployee(Long employeeId) {

    Optional<Employee> employee = employeeService.getEmployee(employeeId);

    if (employee.isPresent()) {
      return new ResponseEntity<>(employeeAssembler.toModel(employee.get()), HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.OK);
    }
  }

  @PostMapping("/employees")
  public void saveEmployee(EmployeeRequest employeeRequest) {
    employeeService.saveEmployee(mapper.employeeRequestToEmployee(employeeRequest));
    log.info("Employee Saved Successfully");
  }

  @DeleteMapping("/employees/{employeeId}")
  public void deleteEmployee(Long employeeId) {
    employeeService.deleteEmployee(employeeId);
    log.info("Employee {} Deleted Successfully", employeeId);
  }

  @PutMapping("/employees/{employeeId}")
  public void updateEmployee(EmployeeRequest employee,
                             Long employeeId) {
    Employee emp = mapper.employeeRequestToEmployee(employee);
    emp.setId(employeeId);
    employeeService.updateEmployee(emp);
  }
}
