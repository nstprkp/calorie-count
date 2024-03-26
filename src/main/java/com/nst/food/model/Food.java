package com.nst.food.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table
public class Food {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Min(value = 1, message = "ID number must be more than zero")
    private Long id;

    @Min(value = 0, message = "Daily amount must not be a negative number")
    private String dailyAmount;

    @NotBlank(message = "Name of food can't be nullable")
    private String name;

    @Min(value = 0, message = "Calorie number must not be a negative")
    private Integer calorie;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "type_product_id")
    private TypeProduct typeProduct;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "name_vitamin",
            joinColumns = @JoinColumn(name = "name_id"),
            inverseJoinColumns = @JoinColumn(name = "vitamin_id"))
    private Set<Vitamin> vitamins;
}
