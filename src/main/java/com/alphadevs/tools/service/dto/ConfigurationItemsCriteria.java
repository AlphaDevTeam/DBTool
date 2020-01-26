package com.alphadevs.tools.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.alphadevs.tools.domain.ConfigurationItems} entity. This class is used
 * in {@link com.alphadevs.tools.web.rest.ConfigurationItemsResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /configuration-items?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ConfigurationItemsCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private BooleanFilter configEnabled;

    private StringFilter configTypeCode;

    private StringFilter configParameter;

    private LongFilter coreConfigId;

    private LongFilter locationId;

    public ConfigurationItemsCriteria(){
    }

    public ConfigurationItemsCriteria(ConfigurationItemsCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.configEnabled = other.configEnabled == null ? null : other.configEnabled.copy();
        this.configTypeCode = other.configTypeCode == null ? null : other.configTypeCode.copy();
        this.configParameter = other.configParameter == null ? null : other.configParameter.copy();
        this.coreConfigId = other.coreConfigId == null ? null : other.coreConfigId.copy();
        this.locationId = other.locationId == null ? null : other.locationId.copy();
    }

    @Override
    public ConfigurationItemsCriteria copy() {
        return new ConfigurationItemsCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public BooleanFilter getConfigEnabled() {
        return configEnabled;
    }

    public void setConfigEnabled(BooleanFilter configEnabled) {
        this.configEnabled = configEnabled;
    }

    public StringFilter getConfigTypeCode() {
        return configTypeCode;
    }

    public void setConfigTypeCode(StringFilter configTypeCode) {
        this.configTypeCode = configTypeCode;
    }

    public StringFilter getConfigParameter() {
        return configParameter;
    }

    public void setConfigParameter(StringFilter configParameter) {
        this.configParameter = configParameter;
    }

    public LongFilter getCoreConfigId() {
        return coreConfigId;
    }

    public void setCoreConfigId(LongFilter coreConfigId) {
        this.coreConfigId = coreConfigId;
    }

    public LongFilter getLocationId() {
        return locationId;
    }

    public void setLocationId(LongFilter locationId) {
        this.locationId = locationId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ConfigurationItemsCriteria that = (ConfigurationItemsCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(configEnabled, that.configEnabled) &&
            Objects.equals(configTypeCode, that.configTypeCode) &&
            Objects.equals(configParameter, that.configParameter) &&
            Objects.equals(coreConfigId, that.coreConfigId) &&
            Objects.equals(locationId, that.locationId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        configEnabled,
        configTypeCode,
        configParameter,
        coreConfigId,
        locationId
        );
    }

    @Override
    public String toString() {
        return "ConfigurationItemsCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (configEnabled != null ? "configEnabled=" + configEnabled + ", " : "") +
                (configTypeCode != null ? "configTypeCode=" + configTypeCode + ", " : "") +
                (configParameter != null ? "configParameter=" + configParameter + ", " : "") +
                (coreConfigId != null ? "coreConfigId=" + coreConfigId + ", " : "") +
                (locationId != null ? "locationId=" + locationId + ", " : "") +
            "}";
    }

}
