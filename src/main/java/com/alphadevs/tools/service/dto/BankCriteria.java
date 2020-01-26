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
 * Criteria class for the {@link com.alphadevs.tools.domain.Bank} entity. This class is used
 * in {@link com.alphadevs.tools.web.rest.BankResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /banks?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class BankCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter bankCode;

    private StringFilter bankName;

    private BooleanFilter isActive;

    public BankCriteria(){
    }

    public BankCriteria(BankCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.bankCode = other.bankCode == null ? null : other.bankCode.copy();
        this.bankName = other.bankName == null ? null : other.bankName.copy();
        this.isActive = other.isActive == null ? null : other.isActive.copy();
    }

    @Override
    public BankCriteria copy() {
        return new BankCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getBankCode() {
        return bankCode;
    }

    public void setBankCode(StringFilter bankCode) {
        this.bankCode = bankCode;
    }

    public StringFilter getBankName() {
        return bankName;
    }

    public void setBankName(StringFilter bankName) {
        this.bankName = bankName;
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
        final BankCriteria that = (BankCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(bankCode, that.bankCode) &&
            Objects.equals(bankName, that.bankName) &&
            Objects.equals(isActive, that.isActive);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        bankCode,
        bankName,
        isActive
        );
    }

    @Override
    public String toString() {
        return "BankCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (bankCode != null ? "bankCode=" + bankCode + ", " : "") +
                (bankName != null ? "bankName=" + bankName + ", " : "") +
                (isActive != null ? "isActive=" + isActive + ", " : "") +
            "}";
    }

}
