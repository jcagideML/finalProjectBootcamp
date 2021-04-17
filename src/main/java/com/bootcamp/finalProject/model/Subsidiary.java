package com.bootcamp.finalProject.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "subsidiaries")
public class Subsidiary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_subsidiary", length = 4)
    private Long idSubsidiary;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String address;
    @Column(nullable = false)
    private String phone;
    @Column(nullable = false)
    private String country;
    @Column(name = "days_to_shipping", nullable = false)
    private Integer daysToShipping;
    @OneToMany(mappedBy = "subsidiary", cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    private List<Order> orders;

    @OneToMany(mappedBy = "subsidiary", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private List<SubsidiaryStock> subsidiaryStocks;

    @OneToMany(mappedBy = "subsidiary", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<User> users;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Subsidiary that = (Subsidiary) o;
        return Objects.equals(name, that.name) && Objects.equals(address, that.address)
                && Objects.equals(phone, that.phone) && Objects.equals(country, that.country);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, address, phone, country);
    }
}