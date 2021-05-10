package com.trackme.models.project;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@SuperBuilder
@Entity
@Table(name = "assigned_cust_tbl")
public class AssignedCustEntity extends AssignUser{

    public AssignedCustEntity() {
    }
}
