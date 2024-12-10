package com.epi.coreservice.model;

import lombok.*;

import javax.persistence.*;
import java.util.Collections;
import java.util.List;

@Entity(name = "products")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Product extends BaseEntity {
    private String name;
    private String description;
    private String imageId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<Comment> Comments;
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<Offer> offers;

    @ElementCollection
    private List<String> keys = Collections.emptyList();
}
