package jp.co.axa.apidemo.infrastructure.rest.model;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
public class EmployeeRequest {
  @Getter
  @Setter
  @Size(max = 255)
  @NotNull(message = "Name must be provided")
  private String name;

  @Getter
  @Setter
  @NotNull
  @Min(1000)
  private Integer salary;

  @Getter
  @Setter
  @Size(max = 255)
  @NotNull(message = "Department must be provided")
  private String department;
}
