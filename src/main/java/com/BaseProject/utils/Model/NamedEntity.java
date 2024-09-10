package com.BaseProject.utils.Model;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;

@MappedSuperclass
@Data
public class NamedEntity {

    @Column(name = "name")
    private String name;
}
