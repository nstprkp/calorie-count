package com.nst.food.model;

import jakarta.persistence.*;
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
    private Long id;

    private String dailyAmount;

    private String name;

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
