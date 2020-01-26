package com.alphadevs.tools.domain;
import io.swagger.annotations.ApiModel;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

/**
 * Bank Entity.\n@author Mihindu Karunarathne.
 */
@ApiModel(description = "Bank Entity.\n@author Mihindu Karunarathne.")
@Entity
@Table(name = "bank")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Bank implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "bank_code", nullable = false)
    private String bankCode;

    @NotNull
    @Column(name = "bank_name", nullable = false)
    private String bankName;

    @Column(name = "is_active")
    private Boolean isActive;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBankCode() {
        return bankCode;
    }

    public Bank bankCode(String bankCode) {
        this.bankCode = bankCode;
        return this;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getBankName() {
        return bankName;
    }

    public Bank bankName(String bankName) {
        this.bankName = bankName;
        return this;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public Boolean isIsActive() {
        return isActive;
    }

    public Bank isActive(Boolean isActive) {
        this.isActive = isActive;
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Bank)) {
            return false;
        }
        return id != null && id.equals(((Bank) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Bank{" +
            "id=" + getId() +
            ", bankCode='" + getBankCode() + "'" +
            ", bankName='" + getBankName() + "'" +
            ", isActive='" + isIsActive() + "'" +
            "}";
    }
}
