package jp.co.axa.apidemo.services;

import java.util.Optional;
import jp.co.axa.apidemo.entities.Employee;
import jp.co.axa.apidemo.exception.EmployeeNotFoundException;
import jp.co.axa.apidemo.repositories.EmployeeRepository;
import jp.co.axa.apidemo.utils.SortDirection;
import jp.co.axa.apidemo.utils.SortableEmployeeColumns;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl implements EmployeeService {

  private final Logger log = LoggerFactory.getLogger(EmployeeServiceImpl.class);
  private EmployeeRepository employeeRepository;

  public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
    this.employeeRepository = employeeRepository;
  }

  public Page<Employee> getAllEmployees(Integer page, Integer pageSize,
                                        SortableEmployeeColumns orderBy,
                                        SortDirection sortDirection) {

    log.debug("Fetch all employees Page {}, Size {}, by {} {}",
        page, pageSize, orderBy, sortDirection);

    if (orderBy != null || sortDirection != null) {
      return employeeRepository.findAll(
          PageRequest.of(page, pageSize, prepareSorting(orderBy, sortDirection))
      );
    } else {
      return employeeRepository.findAll(PageRequest.of(page, pageSize));
    }
  }

  @Cacheable(cacheNames = "employeeById", key = "#employeeId", unless = "#result == null")
  public Optional<Employee> getEmployee(Long employeeId) {
    log.debug("Get employee {}", employeeId);

    return employeeRepository.findById(employeeId);
  }

  public void saveEmployee(Employee employee) {
    employeeRepository.save(employee);
  }

  @CacheEvict(cacheNames = "employeeById", key = "#employeeId")
  public void deleteEmployee(Long employeeId) {
    log.debug("Delete employee {}", employeeId);

    employeeRepository.deleteById(employeeId);
  }

  /**
   * Fetches a reference for the employee and if exists, performs an update.
   *
   * @param employee The employee to update
   */
  // Exceptions are handled by the controllerAdvice (Suppressing for sonarlint)
  @SuppressWarnings({"java:S3984", "java:S2583"})
  public void updateEmployee(Employee employee) {
    //Using getReference instead of find.
    //For just employee it should be the same, but with child elements can save some calls
    if (employeeRepository.getReferenceById(employee.getId()) == null) {
      throw new EmployeeNotFoundException("There is no employee with id: " + employee.getId());
    }
    employeeRepository.save(employee);
  }


  /**
   * Prepares the pagination sort (Order by condition).
   *
   * @param sortBy If not provided, defaults to ID.
   * @param sortDirection If not provided, defaults to ASC
   * @return Pagination sort
   */
  private Sort prepareSorting(SortableEmployeeColumns sortBy,
                                        SortDirection sortDirection) {

    if (sortBy == null) {
      sortBy = SortableEmployeeColumns.ID;
    }

    //Could've used reflection here, toLowerCase to speed up things
    if (SortDirection.DESC.equals(sortDirection)) {
      return Sort.by(sortBy.toString().toLowerCase()).descending();
    } else {
      return Sort.by(sortBy.toString().toLowerCase()).ascending();
    }
  }
}