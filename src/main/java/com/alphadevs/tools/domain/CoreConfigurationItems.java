package com.alphadevs.tools.domain;
import io.swagger.annotations.ApiModel;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

/**
 * Core ConfigurationItems Entity.\n@author Mihindu Karunarathne.
 */
@ApiModel(description = "Core ConfigurationItems Entity.\n@author Mihindu Karunarathne.")
@Entity
@Table(name = "core_configuration_items")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class CoreConfigurationItems implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "config_code", nullable = false)
    private String configCode;

    @NotNull
    @Column(name = "config_description", nullable = false)
    private String configDescription;

    @Column(name = "is_active")
    private Boolean isActive;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getConfigCode() {
        return configCode;
    }

    public CoreConfigurationItems configCode(String configCode) {
        this.configCode = configCode;
        return this;
    }

    public void setConfigCode(String configCode) {
        this.configCode = configCode;
    }

    public String getConfigDescription() {
        return configDescription;
    }

    public CoreConfigurationItems configDescription(String configDescription) {
        this.configDescription = configDescription;
        return this;
    }

    public void setConfigDescription(String configDescription) {
        this.configDescription = configDescription;
    }

    public Boolean isIsActive() {
        return isActive;
    }

    public CoreConfigurationItems isActive(Boolean isActive) {
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
        if (!(o instanceof CoreConfigurationItems)) {
            return false;
        }
        return id != null && id.equals(((CoreConfigurationItems) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "CoreConfigurationItems{" +
            "id=" + getId() +
            ", configCode='" + getConfigCode() + "'" +
            ", configDescription='" + getConfigDescription() + "'" +
            ", isActive='" + isIsActive() + "'" +
            "}";
    }
}
