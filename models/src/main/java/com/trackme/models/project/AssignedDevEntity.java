package com.trackme.models.project;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@SuperBuilder
@Entity
@Table(name = "assigned_dev_tbl")
public class AssignedDevEntity extends AssignUser{

    public AssignedDevEntity() {
    }
}
