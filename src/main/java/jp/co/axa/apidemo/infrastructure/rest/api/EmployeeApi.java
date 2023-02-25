package jp.co.axa.apidemo.infrastructure.rest.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import jp.co.axa.apidemo.infrastructure.rest.model.EmployeeRequest;
import jp.co.axa.apidemo.infrastructure.rest.model.EmployeeResponse;
import jp.co.axa.apidemo.infrastructure.rest.model.ErrorResponse;
import jp.co.axa.apidemo.utils.SortDirection;
import jp.co.axa.apidemo.utils.SortableEmployeeColumns;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;


@SecurityRequirement(name = "basicAuth")
@Tag(name = "Employee", description = "Employee API")
public interface EmployeeApi {


  @Operation(summary = "List all employees",
      description = "List all employees. It can be ordered by any employee field.")

  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successful operation"),
      @ApiResponse(responseCode = "400", description = "Invalid input", content = {
          @Content(
              mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponse.class))
      })
  })
  @GetMapping(value = "/employees")
  ResponseEntity<PagedModel<EntityModel<EmployeeResponse>>> getEmployees(
      @Parameter(description = "Current page number", required = true)
      @RequestParam(defaultValue = "0") Integer page,

      @Parameter(description = "Page size", required = true)
      @RequestParam(defaultValue = "10") Integer size,

      @Parameter(description = "Employee attribute to Order By")
      @RequestParam(required = false) @Valid SortableEmployeeColumns orderBy,

      @Parameter(description = "Sort direction")
      @RequestParam(required = false) @Valid SortDirection sortDirection
  );

  @Operation(summary = "Get Employee by ID",
      description = "Get an employee by its id.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successful operation"),
      @ApiResponse(responseCode = "400", description = "Invalid input", content = {
          @Content(
              mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponse.class))
      })
  })
  @GetMapping(value = "/employees/{employeeId}")
  ResponseEntity<EntityModel<EmployeeResponse>> getEmployee(
      @Parameter(description = "ID of employee to return", required = true)
      @PathVariable(name = "employeeId") Long employeeId);


  @Operation(summary = "Create a new Employee",
      description = "Creates a new Employee.")
  @ApiResponse(responseCode = "400", description = "Invalid input", content = {
      @Content(
          mediaType = "application/json",
          schema = @Schema(implementation = ErrorResponse.class))
  })
  @PostMapping("/employees")
  void saveEmployee(
      @Parameter(description = "Employee to be inserted", required = true)
      @Valid @RequestBody EmployeeRequest employeeRequest);


  @Operation(summary = "Delete an Employee",
      description = "Deletes an Employee by ID.")
  @ApiResponse(responseCode = "400", description = "Invalid input", content = {
      @Content(
          mediaType = "application/json",
          schema = @Schema(implementation = ErrorResponse.class))
  })
  @DeleteMapping("/employees/{employeeId}")
  void deleteEmployee(
      @Parameter(description = "Employee ID to be deleted", required = true)
      @PathVariable(name = "employeeId") Long employeeId);

  @Operation(summary = "Update an Employee",
      description = "Updates an Employee by ID.")
  @ApiResponse(responseCode = "400", description = "Invalid input", content = {
      @Content(
          mediaType = "application/json",
          schema = @Schema(implementation = ErrorResponse.class))
  })
  @PutMapping("/employees/{employeeId}")
  void updateEmployee(

      @Parameter(description = "Employee to be updated", required = true)
      @Valid @RequestBody EmployeeRequest employee,

      @Parameter(description = "Employee ID to be updated", required = true)
      @PathVariable(name = "employeeId") Long employeeId);
}
