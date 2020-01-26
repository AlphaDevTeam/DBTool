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
 * Criteria class for the {@link com.alphadevs.tools.domain.CoreConfigurationItems} entity. This class is used
 * in {@link com.alphadevs.tools.web.rest.CoreConfigurationItemsResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /core-configuration-items?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class CoreConfigurationItemsCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter configCode;

    private StringFilter configDescription;

    private BooleanFilter isActive;

    public CoreConfigurationItemsCriteria(){
    }

    public CoreConfigurationItemsCriteria(CoreConfigurationItemsCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.configCode = other.configCode == null ? null : other.configCode.copy();
        this.configDescription = other.configDescription == null ? null : other.configDescription.copy();
        this.isActive = other.isActive == null ? null : other.isActive.copy();
    }

    @Override
    public CoreConfigurationItemsCriteria copy() {
        return new CoreConfigurationItemsCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getConfigCode() {
        return configCode;
    }

    public void setConfigCode(StringFilter configCode) {
        this.configCode = configCode;
    }

    public StringFilter getConfigDescription() {
        return configDescription;
    }

    public void setConfigDescription(StringFilter configDescription) {
        this.configDescription = configDescription;
    }

    public BooleanFilter getIsActive() {
        return isActive;
    }

    public void setIsActive(BooleanFilter isActive) {
        this.isActive = isActive;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final CoreConfigurationItemsCriteria that = (CoreConfigurationItemsCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(configCode, that.configCode) &&
            Objects.equals(configDescription, that.configDescription) &&
            Objects.equals(isActive, that.isActive);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        configCode,
        configDescription,
        isActive
        );
    }

    @Override
    public String toString() {
        return "CoreConfigurationItemsCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (configCode != null ? "configCode=" + configCode + ", " : "") +
                (configDescription != null ? "configDescription=" + configDescription + ", " : "") +
                (isActive != null ? "isActive=" + isActive + ", " : "") +
            "}";
    }

}
