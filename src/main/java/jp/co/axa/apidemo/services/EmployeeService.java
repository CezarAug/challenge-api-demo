package jp.co.axa.apidemo.services;

import java.util.Optional;
import jp.co.axa.apidemo.entities.Employee;
import jp.co.axa.apidemo.utils.SortDirection;
import jp.co.axa.apidemo.utils.SortableEmployeeColumns;
import org.springframework.data.domain.Page;

public interface EmployeeService {

  Page<Employee> getAllEmployees(
      Integer page, Integer pageSize, SortableEmployeeColumns orderBy, SortDirection sortDirection);

  Optional<Employee> getEmployee(Long employeeId);

  void saveEmployee(Employee employee);

  void deleteEmployee(Long employeeId);

  void updateEmployee(Employee employee);
}