package com.leskiewicz.schoolsystem.degree;

import com.leskiewicz.schoolsystem.degree.dto.DegreeDto;
import com.leskiewicz.schoolsystem.degree.utils.DegreeMapper;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static com.leskiewicz.schoolsystem.builders.DegreeBuilder.createDegreeDtoListFrom;
import static com.leskiewicz.schoolsystem.builders.DegreeBuilder.createDegreeList;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class DegreeServiceTest {

    @Mock private DegreeRepository degreeRepository;
    @Mock private DegreeMapper degreeMapper;
    @InjectMocks private DegreeServiceImpl degreeService;

    List<Degree> degreesList = createDegreeList();
    List<DegreeDto> degreeDtosList = createDegreeDtoListFrom(degreesList);
    Page<Degree> degreesPage = new PageImpl<>(degreesList);
    Page<DegreeDto> degreeDtosPage = new PageImpl<>(degreeDtosList);

    @Test
    public void getDegreesReturnsPagedDegrees() {
        when(degreeRepository.findAll(any(Pageable.class))).thenReturn(degreesPage);
        when(degreeMapper.mapPageToDto(any(Page.class))).thenReturn(degreeDtosPage);

        Page<DegreeDto> result = degreeService.getDegrees(Pageable.unpaged());

        Assertions.assertEquals(2, result.getTotalElements());
        Assertions.assertEquals(
                degreeDtosList.get(0).getFieldOfStudy(), result.getContent().get(0).getFieldOfStudy());
        Assertions.assertEquals(
                degreeDtosList.get(1).getFieldOfStudy(), result.getContent().get(1).getFieldOfStudy());
    }

    @Nested
    public class getById {
        @Test
        public void returnsDegreeDto() {
            when(degreeRepository.findById(any(Long.class))).thenReturn(Optional.of(degreesList.get(0)));
            when(degreeMapper.convertToDto(any(Degree.class))).thenReturn(degreeDtosList.get(0));

            DegreeDto result = degreeService.getById(1L);

            Assertions.assertEquals(degreeDtosList.get(0).getFieldOfStudy(), result.getFieldOfStudy());
        }

        @Test
        public void throwsExceptionWhenDegreeNotFound() {
            when(degreeRepository.findById(any(Long.class))).thenReturn(Optional.empty());

            Assertions.assertThrows(EntityNotFoundException.class, () -> degreeService.getById(1L));
        }
    }


}
