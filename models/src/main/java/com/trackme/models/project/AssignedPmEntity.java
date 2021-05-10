package com.trackme.models.project;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@SuperBuilder
@Entity
@Table(name = "assigned_pm_tbl")
public class AssignedPmEntity extends AssignUser {
    public AssignedPmEntity() {
    }
}
