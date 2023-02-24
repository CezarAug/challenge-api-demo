package jp.co.axa.apidemo.infrastructure.rest.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

@NoArgsConstructor
public class EmployeeResponse extends RepresentationModel<EmployeeResponse> {

  @Getter
  @Setter
  private Long id;

  @Getter
  @Setter
  private String name;

  @Getter
  @Setter
  private Integer salary;

  @Getter
  @Setter
  private String department;
}
