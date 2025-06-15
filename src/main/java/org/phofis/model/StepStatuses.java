package org.phofis.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@AllArgsConstructor
@Getter
@Setter
public class StepStatuses {
    private List<StepStatus> stepStatuses;
}
