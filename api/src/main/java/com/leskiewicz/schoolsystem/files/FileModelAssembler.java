package com.leskiewicz.schoolsystem.files;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class FileModelAssembler extends RepresentationModelAssemblerSupport<File, File> {

  public FileModelAssembler() {
    super(FileController.class, File.class);
  }

  @Override
  public File toModel(File file) {
    Link selfLink = linkTo(methodOn(FileController.class).downloadFile(file.getId())).withSelfRel();

    file.add(selfLink);

    return file;
  }
}
