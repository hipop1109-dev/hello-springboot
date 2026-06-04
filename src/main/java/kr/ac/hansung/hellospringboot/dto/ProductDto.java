package kr.ac.hansung.hellospringboot.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {

    @NotBlank(message = "Product name cannot be empty")
    private String name;

    @Min(value = 0, message = "Price must be greater than or equal to 0")
    private int price;

    @Min(value = 0, message = "Stock must be greater than or equal to 0")
    private int stock;

    private String description;
}
