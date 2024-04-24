package com.taomish.msservice01.audit.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Difference implements Serializable {
    private String field;
    private String label;
    private Object oldValue;
    private Object newValue;
}
