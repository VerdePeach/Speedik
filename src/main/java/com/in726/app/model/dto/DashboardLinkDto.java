package com.in726.app.model.dto;

import com.in726.app.model.sub_functional_model.Link;
import lombok.Data;


@Data
public class DashboardLinkDto {

    private Link link;

    private String successPercent;
}
