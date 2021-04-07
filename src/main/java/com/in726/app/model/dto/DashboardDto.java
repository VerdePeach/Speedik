package com.in726.app.model.dto;

import com.in726.app.model.sub_functional_model.Link;
import lombok.Data;

import java.util.List;

@Data
public class DashboardDto {

    private String url;

    private String description;

    private String ownerName;

    private List<DashboardLinkDto> linksDto;
}
