package com.leskiewicz.schoolsystem.files;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import java.util.Arrays;
import java.util.Objects;

@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "file")
public class File extends RepresentationModel<File> {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @NotNull
  @Column(name = "file_name")
  private String fileName;

  @NotNull
  @Column(name = "file_type")
  private String fileType;

  @NotNull
  @Column(name = "uploaded_by")
  private Long uploadedBy;

  @NotNull
  @Lob
  @Column(name = "file_data", columnDefinition = "BLOB")
  private byte[] fileData;

  @Override
  public String toString() {
    return "File{"
        + "id="
        + id
        + ", fileName='"
        + fileName
        + '\''
        + ", fileType='"
        + fileType
        + '\''
        + ", uploadedBy="
        + uploadedBy
        + ", fileData="
        + (fileData == null ? "null" : fileData.length + " bytes")
        + '}';
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, fileName, fileType, uploadedBy, Arrays.hashCode(fileData));
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    File file = (File) o;
    return Objects.equals(id, file.id)
        && Objects.equals(fileName, file.fileName)
        && Objects.equals(fileType, file.fileType)
        && Objects.equals(uploadedBy, file.uploadedBy)
        && Arrays.equals(fileData, file.fileData);
  }
}
