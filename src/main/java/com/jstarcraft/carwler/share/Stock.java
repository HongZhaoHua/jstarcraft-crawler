package com.jstarcraft.carwler.share;

import com.jstarcraft.core.resource.annotation.ResourceConfiguration;
import com.jstarcraft.core.resource.annotation.ResourceId;

import lombok.Data;

@Data
@ResourceConfiguration(prefix = "", suffix = ".xls")
public class Stock {

    @ResourceId
    private String code;

    private String share;

    private String name;

}
