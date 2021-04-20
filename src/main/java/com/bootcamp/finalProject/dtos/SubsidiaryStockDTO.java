package com.bootcamp.finalProject.dtos;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.Objects;

@Getter
@Setter
@ApiModel(description = "Basic fields of a stock in a subsidiary")
public class SubsidiaryStockDTO {

    @NotNull(message = "Quantity of the part is required")
    @ApiModelProperty(notes = "Quantity of the part in the given subsidiary",
            example = "607",
            required = true,
            position = 1)
    private String quantityInSubsidiary;

    @NotNull(message = "Code of the part is required")
    @ApiModelProperty(notes = "Id of the part",
            example = "23",
            required = true,
            position = 2)
    private String partCode;

    @ApiModelProperty(notes = "description of the stock",
            example = "soon to receive more",
            position = 3)
    private String description;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SubsidiaryStockDTO that = (SubsidiaryStockDTO) o;
        return Objects.equals(quantityInSubsidiary, that.quantityInSubsidiary) && Objects.equals(partCode, that.partCode) && Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(quantityInSubsidiary, partCode, description);
    }
}
