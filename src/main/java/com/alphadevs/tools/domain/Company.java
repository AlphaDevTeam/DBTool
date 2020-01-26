package com.alphadevs.tools.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * Company Entity.\n@author Mihindu Karunarathne.
 */
@ApiModel(description = "Company Entity.\n@author Mihindu Karunarathne.")
@Entity
@Table(name = "company")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Company implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "company_code", nullable = false)
    private String companyCode;

    @NotNull
    @Column(name = "company_name", nullable = false)
    private String companyName;

    @NotNull
    @Column(name = "company_reg_number", nullable = false)
    private String companyRegNumber;

    @NotNull
    @Pattern(regexp = "^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")
    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "rating")
    private Double rating;

    @Column(name = "phone")
    private String phone;

    @Column(name = "address_line_1")
    private String addressLine1;

    @Column(name = "address_line_2")
    private String addressLine2;

    @Column(name = "city")
    private String city;

    @Column(name = "country")
    private String country;

    @Lob
    @Column(name = "image")
    private byte[] image;

    @Column(name = "image_content_type")
    private String imageContentType;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "api_key")
    private String apiKey;

    @Column(name = "expire_on")
    private LocalDate expireOn;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("companies")
    private LicenseType licenseType;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "company_configitems",
               joinColumns = @JoinColumn(name = "company_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "configitems_id", referencedColumnName = "id"))
    private Set<ConfigurationItems> configitems = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public Company companyCode(String companyCode) {
        this.companyCode = companyCode;
        return this;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public String getCompanyName() {
        return companyName;
    }

    public Company companyName(String companyName) {
        this.companyName = companyName;
        return this;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyRegNumber() {
        return companyRegNumber;
    }

    public Company companyRegNumber(String companyRegNumber) {
        this.companyRegNumber = companyRegNumber;
        return this;
    }

    public void setCompanyRegNumber(String companyRegNumber) {
        this.companyRegNumber = companyRegNumber;
    }

    public String getEmail() {
        return email;
    }

    public Company email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Double getRating() {
        return rating;
    }

    public Company rating(Double rating) {
        this.rating = rating;
        return this;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public String getPhone() {
        return phone;
    }

    public Company phone(String phone) {
        this.phone = phone;
        return this;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public Company addressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
        return this;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public Company addressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
        return this;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public String getCity() {
        return city;
    }

    public Company city(String city) {
        this.city = city;
        return this;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public Company country(String country) {
        this.country = country;
        return this;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public byte[] getImage() {
        return image;
    }

    public Company image(byte[] image) {
        this.image = image;
        return this;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getImageContentType() {
        return imageContentType;
    }

    public Company imageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
        return this;
    }

    public void setImageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
    }

    public Boolean isIsActive() {
        return isActive;
    }

    public Company isActive(Boolean isActive) {
        this.isActive = isActive;
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public String getApiKey() {
        return apiKey;
    }

    public Company apiKey(String apiKey) {
        this.apiKey = apiKey;
        return this;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public LocalDate getExpireOn() {
        return expireOn;
    }

    public Company expireOn(LocalDate expireOn) {
        this.expireOn = expireOn;
        return this;
    }

    public void setExpireOn(LocalDate expireOn) {
        this.expireOn = expireOn;
    }

    public LicenseType getLicenseType() {
        return licenseType;
    }

    public Company licenseType(LicenseType licenseType) {
        this.licenseType = licenseType;
        return this;
    }

    public void setLicenseType(LicenseType licenseType) {
        this.licenseType = licenseType;
    }

    public Set<ConfigurationItems> getConfigitems() {
        return configitems;
    }

    public Company configitems(Set<ConfigurationItems> configurationItems) {
        this.configitems = configurationItems;
        return this;
    }

    public Company addConfigitems(ConfigurationItems configurationItems) {
        this.configitems.add(configurationItems);
        configurationItems.getLocations().add(this);
        return this;
    }

    public Company removeConfigitems(ConfigurationItems configurationItems) {
        this.configitems.remove(configurationItems);
        configurationItems.getLocations().remove(this);
        return this;
    }

    public void setConfigitems(Set<ConfigurationItems> configurationItems) {
        this.configitems = configurationItems;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Company)) {
            return false;
        }
        return id != null && id.equals(((Company) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Company{" +
            "id=" + getId() +
            ", companyCode='" + getCompanyCode() + "'" +
            ", companyName='" + getCompanyName() + "'" +
            ", companyRegNumber='" + getCompanyRegNumber() + "'" +
            ", email='" + getEmail() + "'" +
            ", rating=" + getRating() +
            ", phone='" + getPhone() + "'" +
            ", addressLine1='" + getAddressLine1() + "'" +
            ", addressLine2='" + getAddressLine2() + "'" +
            ", city='" + getCity() + "'" +
            ", country='" + getCountry() + "'" +
            ", image='" + getImage() + "'" +
            ", imageContentType='" + getImageContentType() + "'" +
            ", isActive='" + isIsActive() + "'" +
            ", apiKey='" + getApiKey() + "'" +
            ", expireOn='" + getExpireOn() + "'" +
            "}";
    }
}