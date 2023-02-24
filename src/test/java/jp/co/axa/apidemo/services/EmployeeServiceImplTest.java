package jp.co.axa.apidemo.services;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import jp.co.axa.apidemo.entities.Employee;
import jp.co.axa.apidemo.exception.EmployeeNotFoundException;
import jp.co.axa.apidemo.repositories.EmployeeRepository;
import jp.co.axa.apidemo.utils.SortDirection;
import jp.co.axa.apidemo.utils.SortableEmployeeColumns;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceImplTest {

  @Mock
  private EmployeeRepository employeeRepository;

  private EmployeeService employeeService;


  @BeforeEach
  void init() {
    employeeService = new EmployeeServiceImpl(employeeRepository);
  }

  @Test
  void getAllEmployees_withDefaultSorting() {
    employeeService.getAllEmployees(0, 10, null, null);
    verify(employeeRepository, times(1)).findAll(PageRequest.of(0, 10, Sort.unsorted()));
    verifyNoMoreInteractions(employeeRepository);
  }

  @Test
  void getAllEmployees_withSorting_defaultsToId() {
    employeeService.getAllEmployees(0, 10, null, SortDirection.DESC);
    verify(employeeRepository, times(1)).findAll(PageRequest.of(0, 10, Sort.by(SortableEmployeeColumns.ID.toString().toLowerCase()).descending()));
    verifyNoMoreInteractions(employeeRepository);
  }

  @Test
  void getAllEmployees_withOrderBy_defaultsToASC() {
    employeeService.getAllEmployees(0, 10, SortableEmployeeColumns.SALARY, null);
    verify(employeeRepository, times(1)).findAll(PageRequest.of(0, 10, Sort.by(SortableEmployeeColumns.SALARY.toString().toLowerCase()).ascending()));
    verifyNoMoreInteractions(employeeRepository);
  }

  @Test
  void getEmployeeById() {
    employeeService.getEmployee(101010L);
    verify(employeeRepository, times(1)).findById(101010L);
    verifyNoMoreInteractions(employeeRepository);
  }

  @Test
  void saveEmployee() {
    Employee employee = new Employee(10L, "Ednaldo Pereira", 100000, "CEO");

    employeeService.saveEmployee(employee);

    verify(employeeRepository, times(1)).save(employee);
    verifyNoMoreInteractions(employeeRepository);
  }

  @Test
  void deleteEmployee() {
    employeeService.deleteEmployee(9999L);
    verify(employeeRepository, times(1)).deleteById(9999L);
    verifyNoMoreInteractions(employeeRepository);
  }

  @Test
  void updateEmployee() {
    Employee employee = new Employee(20L, "Dwight Schrute", 1500, "Sales");

    when(employeeRepository.getReferenceById(employee.getId())).thenReturn(employee);

    employeeService.updateEmployee(employee);

    verify(employeeRepository, times(1)).getReferenceById(employee.getId());
    verify(employeeRepository, times(1)).save(employee);
    verifyNoMoreInteractions(employeeRepository);
  }

  @Test
  void updateEmployee_notFound() {
    Employee employee = new Employee(20L, "Dwight Schrute", 1500, "Sales");

    when(employeeRepository.getReferenceById(employee.getId())).thenReturn(null);

    Assertions.assertThrows(EmployeeNotFoundException.class, () -> employeeService.updateEmployee(employee));

    verify(employeeRepository, times(1)).getReferenceById(employee.getId());
    verify(employeeRepository, times(0)).save(employee);
    verifyNoMoreInteractions(employeeRepository);
  }
}