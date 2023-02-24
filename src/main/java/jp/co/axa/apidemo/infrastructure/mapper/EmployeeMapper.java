package jp.co.axa.apidemo.infrastructure.mapper;

import jp.co.axa.apidemo.entities.Employee;
import jp.co.axa.apidemo.infrastructure.rest.model.EmployeeRequest;
import jp.co.axa.apidemo.infrastructure.rest.model.EmployeeResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface EmployeeMapper {

  @Mapping(target = "id", ignore = true)
  Employee employeeRequestToEmployee(EmployeeRequest request);

  EmployeeResponse employeeToEmployeeResponse(Employee employee);

}
