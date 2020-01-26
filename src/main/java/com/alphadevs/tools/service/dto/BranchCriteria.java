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
 * Criteria class for the {@link com.alphadevs.tools.domain.Branch} entity. This class is used
 * in {@link com.alphadevs.tools.web.rest.BranchResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /branches?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class BranchCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter branchCode;

    private StringFilter branchName;

    private BooleanFilter isActive;

    private LongFilter bankId;

    private LongFilter usersId;

    public BranchCriteria(){
    }

    public BranchCriteria(BranchCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.branchCode = other.branchCode == null ? null : other.branchCode.copy();
        this.branchName = other.branchName == null ? null : other.branchName.copy();
        this.isActive = other.isActive == null ? null : other.isActive.copy();
        this.bankId = other.bankId == null ? null : other.bankId.copy();
        this.usersId = other.usersId == null ? null : other.usersId.copy();
    }

    @Override
    public BranchCriteria copy() {
        return new BranchCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getBranchCode() {
        return branchCode;
    }

    public void setBranchCode(StringFilter branchCode) {
        this.branchCode = branchCode;
    }

    public StringFilter getBranchName() {
        return branchName;
    }

    public void setBranchName(StringFilter branchName) {
        this.branchName = branchName;
    }

    public BooleanFilter getIsActive() {
        return isActive;
    }

    public void setIsActive(BooleanFilter isActive) {
        this.isActive = isActive;
    }

    public LongFilter getBankId() {
        return bankId;
    }

    public void setBankId(LongFilter bankId) {
        this.bankId = bankId;
    }

    public LongFilter getUsersId() {
        return usersId;
    }

    public void setUsersId(LongFilter usersId) {
        this.usersId = usersId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final BranchCriteria that = (BranchCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(branchCode, that.branchCode) &&
            Objects.equals(branchName, that.branchName) &&
            Objects.equals(isActive, that.isActive) &&
            Objects.equals(bankId, that.bankId) &&
            Objects.equals(usersId, that.usersId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        branchCode,
        branchName,
        isActive,
        bankId,
        usersId
        );
    }

    @Override
    public String toString() {
        return "BranchCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (branchCode != null ? "branchCode=" + branchCode + ", " : "") +
                (branchName != null ? "branchName=" + branchName + ", " : "") +
                (isActive != null ? "isActive=" + isActive + ", " : "") +
                (bankId != null ? "bankId=" + bankId + ", " : "") +
                (usersId != null ? "usersId=" + usersId + ", " : "") +
            "}";
    }

}
