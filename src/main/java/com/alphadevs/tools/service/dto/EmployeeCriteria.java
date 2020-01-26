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
 * Criteria class for the {@link com.alphadevs.tools.domain.Employee} entity. This class is used
 * in {@link com.alphadevs.tools.web.rest.EmployeeResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /employees?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class EmployeeCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter empNumber;

    private StringFilter firstName;

    private StringFilter lastName;

    private StringFilter knownName;

    private StringFilter email;

    private BooleanFilter isActive;

    private StringFilter phone;

    private StringFilter addressLine1;

    private StringFilter addressLine2;

    private StringFilter city;

    private StringFilter country;

    private StringFilter imageURL;

    private DoubleFilter salary;

    private StringFilter designation;

    private LongFilter branchId;

    private LongFilter empTypeId;

    public EmployeeCriteria(){
    }

    public EmployeeCriteria(EmployeeCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.empNumber = other.empNumber == null ? null : other.empNumber.copy();
        this.firstName = other.firstName == null ? null : other.firstName.copy();
        this.lastName = other.lastName == null ? null : other.lastName.copy();
        this.knownName = other.knownName == null ? null : other.knownName.copy();
        this.email = other.email == null ? null : other.email.copy();
        this.isActive = other.isActive == null ? null : other.isActive.copy();
        this.phone = other.phone == null ? null : other.phone.copy();
        this.addressLine1 = other.addressLine1 == null ? null : other.addressLine1.copy();
        this.addressLine2 = other.addressLine2 == null ? null : other.addressLine2.copy();
        this.city = other.city == null ? null : other.city.copy();
        this.country = other.country == null ? null : other.country.copy();
        this.imageURL = other.imageURL == null ? null : other.imageURL.copy();
        this.salary = other.salary == null ? null : other.salary.copy();
        this.designation = other.designation == null ? null : other.designation.copy();
        this.branchId = other.branchId == null ? null : other.branchId.copy();
        this.empTypeId = other.empTypeId == null ? null : other.empTypeId.copy();
    }

    @Override
    public EmployeeCriteria copy() {
        return new EmployeeCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getEmpNumber() {
        return empNumber;
    }

    public void setEmpNumber(StringFilter empNumber) {
        this.empNumber = empNumber;
    }

    public StringFilter getFirstName() {
        return firstName;
    }

    public void setFirstName(StringFilter firstName) {
        this.firstName = firstName;
    }

    public StringFilter getLastName() {
        return lastName;
    }

    public void setLastName(StringFilter lastName) {
        this.lastName = lastName;
    }

    public StringFilter getKnownName() {
        return knownName;
    }

    public void setKnownName(StringFilter knownName) {
        this.knownName = knownName;
    }

    public StringFilter getEmail() {
        return email;
    }

    public void setEmail(StringFilter email) {
        this.email = email;
    }

    public BooleanFilter getIsActive() {
        return isActive;
    }

    public void setIsActive(BooleanFilter isActive) {
        this.isActive = isActive;
    }

    public StringFilter getPhone() {
        return phone;
    }

    public void setPhone(StringFilter phone) {
        this.phone = phone;
    }

    public StringFilter getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(StringFilter addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public StringFilter getAddressLine2() {
        return addressLine2;
    }

    public void setAddressLine2(StringFilter addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public StringFilter getCity() {
        return city;
    }

    public void setCity(StringFilter city) {
        this.city = city;
    }

    public StringFilter getCountry() {
        return country;
    }

    public void setCountry(StringFilter country) {
        this.country = country;
    }

    public StringFilter getImageURL() {
        return imageURL;
    }

    public void setImageURL(StringFilter imageURL) {
        this.imageURL = imageURL;
    }

    public DoubleFilter getSalary() {
        return salary;
    }

    public void setSalary(DoubleFilter salary) {
        this.salary = salary;
    }

    public StringFilter getDesignation() {
        return designation;
    }

    public void setDesignation(StringFilter designation) {
        this.designation = designation;
    }

    public LongFilter getBranchId() {
        return branchId;
    }

    public void setBranchId(LongFilter branchId) {
        this.branchId = branchId;
    }

    public LongFilter getEmpTypeId() {
        return empTypeId;
    }

    public void setEmpTypeId(LongFilter empTypeId) {
        this.empTypeId = empTypeId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final EmployeeCriteria that = (EmployeeCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(empNumber, that.empNumber) &&
            Objects.equals(firstName, that.firstName) &&
            Objects.equals(lastName, that.lastName) &&
            Objects.equals(knownName, that.knownName) &&
            Objects.equals(email, that.email) &&
            Objects.equals(isActive, that.isActive) &&
            Objects.equals(phone, that.phone) &&
            Objects.equals(addressLine1, that.addressLine1) &&
            Objects.equals(addressLine2, that.addressLine2) &&
            Objects.equals(city, that.city) &&
            Objects.equals(country, that.country) &&
            Objects.equals(imageURL, that.imageURL) &&
            Objects.equals(salary, that.salary) &&
            Objects.equals(designation, that.designation) &&
            Objects.equals(branchId, that.branchId) &&
            Objects.equals(empTypeId, that.empTypeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        empNumber,
        firstName,
        lastName,
        knownName,
        email,
        isActive,
        phone,
        addressLine1,
        addressLine2,
        city,
        country,
        imageURL,
        salary,
        designation,
        branchId,
        empTypeId
        );
    }

    @Override
    public String toString() {
        return "EmployeeCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (empNumber != null ? "empNumber=" + empNumber + ", " : "") +
                (firstName != null ? "firstName=" + firstName + ", " : "") +
                (lastName != null ? "lastName=" + lastName + ", " : "") +
                (knownName != null ? "knownName=" + knownName + ", " : "") +
                (email != null ? "email=" + email + ", " : "") +
                (isActive != null ? "isActive=" + isActive + ", " : "") +
                (phone != null ? "phone=" + phone + ", " : "") +
                (addressLine1 != null ? "addressLine1=" + addressLine1 + ", " : "") +
                (addressLine2 != null ? "addressLine2=" + addressLine2 + ", " : "") +
                (city != null ? "city=" + city + ", " : "") +
                (country != null ? "country=" + country + ", " : "") +
                (imageURL != null ? "imageURL=" + imageURL + ", " : "") +
                (salary != null ? "salary=" + salary + ", " : "") +
                (designation != null ? "designation=" + designation + ", " : "") +
                (branchId != null ? "branchId=" + branchId + ", " : "") +
                (empTypeId != null ? "empTypeId=" + empTypeId + ", " : "") +
            "}";
    }

}
