package com.leskiewicz.schoolsystem.degree;

import com.leskiewicz.schoolsystem.error.ErrorMessages;
import com.leskiewicz.schoolsystem.user.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class DegreeServiceImpl implements DegreeService {

  private final DegreeRepository degreeRepository;


  @Override
  public Degree getById(Long id) {
    return degreeRepository.findById(id).orElseThrow(
        () -> new EntityNotFoundException(ErrorMessages.objectWithIdNotFound("Degree", id)));
  }

  @Override
  public Page<Degree> getDegrees(Pageable pageable) {
    return degreeRepository.findAll(pageable);
  }


  @Override
  public Degree getByTitleAndFieldOfStudy(DegreeTitle title, String fieldOfStudy) {
    return degreeRepository.findByTitleAndFieldOfStudy(title, fieldOfStudy).orElseThrow(
        () -> new EntityNotFoundException("Degree with given title and field of study not found"));
  }
}
