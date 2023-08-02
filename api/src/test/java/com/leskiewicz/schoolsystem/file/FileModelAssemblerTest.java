package com.leskiewicz.schoolsystem.file;

import com.leskiewicz.schoolsystem.files.File;
import com.leskiewicz.schoolsystem.files.FileController;
import com.leskiewicz.schoolsystem.files.FileModelAssembler;
import com.leskiewicz.schoolsystem.testUtils.TestHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;

@ExtendWith(MockitoExtension.class)
public class FileModelAssemblerTest {

  @InjectMocks private FileModelAssembler fileModelAssembler;

  @Test
  public void testToModelAddsCorrectLinks() {
    File file = TestHelper.createFile();
    File result = fileModelAssembler.toModel(file);

    Link selfLink =
        WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(FileController.class).downloadFile(file.getId()))
            .withSelfRel();

    Assertions.assertEquals(selfLink, result.getLink("self").get());
  }
}
