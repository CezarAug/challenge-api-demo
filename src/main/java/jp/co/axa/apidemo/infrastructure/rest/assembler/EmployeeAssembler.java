package jp.co.axa.apidemo.infrastructure.rest.assembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import jp.co.axa.apidemo.entities.Employee;
import jp.co.axa.apidemo.infrastructure.mapper.EmployeeMapper;
import jp.co.axa.apidemo.infrastructure.rest.controller.EmployeeController;
import jp.co.axa.apidemo.infrastructure.rest.model.EmployeeResponse;
import org.mapstruct.factory.Mappers;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

/**
 * Prepares Employee HATEOAS links.
 */
@Component
public class EmployeeAssembler implements
    RepresentationModelAssembler<Employee, EntityModel<EmployeeResponse>> {

  private EmployeeMapper mapper = Mappers.getMapper(EmployeeMapper.class);

  @Override
  public EntityModel<EmployeeResponse> toModel(Employee entity) {

    return EntityModel.of(
        mapper.employeeToEmployeeResponse(entity),
        linkTo(methodOn(EmployeeController.class).getEmployee(entity.getId())).withSelfRel());
  }
}
