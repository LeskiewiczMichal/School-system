package com.leskiewicz.schoolsystem.utils;

import org.springframework.data.domain.Page;

public interface Mapper<E, D> {

    D mapToDto(E user);
    Page<D> mapPageToDto(Page<E> users);
}
