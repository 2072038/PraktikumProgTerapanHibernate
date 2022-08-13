package com.pterapan.demosql.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "items", schema = "items", catalog = "")
public class ItemsEntity {

    @Override
    public String toString() {
        return categoryByCategoryId.getName();
    }

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private int id;
    @Basic
    @Column(name = "name")
    private String name;
    @Basic
    @Column(name = "price")
    private Double price;
    @Basic
    @Column(name = "description")
    private String description;
    @Basic
    @Column(name = "category_id")
    private int categoryId;
    @ManyToOne
    @JoinColumn(name = "category_id", updatable = false, referencedColumnName = "id", nullable = false)
    private CategoryEntity categoryByCategoryId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemsEntity that = (ItemsEntity) o;
        return id == that.id && categoryId == that.categoryId && Objects.equals(name, that.name) && Objects.equals(price, that.price) && Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, price, description, categoryId);
    }

    public CategoryEntity getCategoryByCategoryId() {
        return categoryByCategoryId;
    }

    public void setCategoryByCategoryId(CategoryEntity categoryByCategoryId) {
        this.categoryByCategoryId = categoryByCategoryId;
    }
}
