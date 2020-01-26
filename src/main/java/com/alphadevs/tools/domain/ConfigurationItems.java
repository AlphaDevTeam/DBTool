package com.alphadevs.tools.domain;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * ConfigurationItems Entity.\n@author Mihindu Karunarathne.
 */
@ApiModel(description = "ConfigurationItems Entity.\n@author Mihindu Karunarathne.")
@Entity
@Table(name = "configuration_items")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ConfigurationItems implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "config_enabled")
    private Boolean configEnabled;

    @Column(name = "config_type_code")
    private String configTypeCode;

    @Column(name = "config_parameter")
    private String configParameter;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("configurationItems")
    private CoreConfigurationItems coreConfig;

    @ManyToMany(mappedBy = "configitems")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JsonIgnore
    private Set<Company> locations = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean isConfigEnabled() {
        return configEnabled;
    }

    public ConfigurationItems configEnabled(Boolean configEnabled) {
        this.configEnabled = configEnabled;
        return this;
    }

    public void setConfigEnabled(Boolean configEnabled) {
        this.configEnabled = configEnabled;
    }

    public String getConfigTypeCode() {
        return configTypeCode;
    }

    public ConfigurationItems configTypeCode(String configTypeCode) {
        this.configTypeCode = configTypeCode;
        return this;
    }

    public void setConfigTypeCode(String configTypeCode) {
        this.configTypeCode = configTypeCode;
    }

    public String getConfigParameter() {
        return configParameter;
    }

    public ConfigurationItems configParameter(String configParameter) {
        this.configParameter = configParameter;
        return this;
    }

    public void setConfigParameter(String configParameter) {
        this.configParameter = configParameter;
    }

    public CoreConfigurationItems getCoreConfig() {
        return coreConfig;
    }

    public ConfigurationItems coreConfig(CoreConfigurationItems coreConfigurationItems) {
        this.coreConfig = coreConfigurationItems;
        return this;
    }

    public void setCoreConfig(CoreConfigurationItems coreConfigurationItems) {
        this.coreConfig = coreConfigurationItems;
    }

    public Set<Company> getLocations() {
        return locations;
    }

    public ConfigurationItems locations(Set<Company> companies) {
        this.locations = companies;
        return this;
    }

    public ConfigurationItems addLocation(Company company) {
        this.locations.add(company);
        company.getConfigitems().add(this);
        return this;
    }

    public ConfigurationItems removeLocation(Company company) {
        this.locations.remove(company);
        company.getConfigitems().remove(this);
        return this;
    }

    public void setLocations(Set<Company> companies) {
        this.locations = companies;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ConfigurationItems)) {
            return false;
        }
        return id != null && id.equals(((ConfigurationItems) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "ConfigurationItems{" +
            "id=" + getId() +
            ", configEnabled='" + isConfigEnabled() + "'" +
            ", configTypeCode='" + getConfigTypeCode() + "'" +
            ", configParameter='" + getConfigParameter() + "'" +
            "}";
    }
}
