import { Component, OnInit, ElementRef } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { JhiDataUtils, JhiFileLoadError, JhiEventManager, JhiEventWithContent } from 'ng-jhipster';

import { IEmployee, Employee } from 'app/shared/model/employee.model';
import { EmployeeService } from './employee.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { IBranch } from 'app/shared/model/branch.model';
import { BranchService } from 'app/entities/branch/branch.service';
import { IEmployeeType } from 'app/shared/model/employee-type.model';
import { EmployeeTypeService } from 'app/entities/employee-type/employee-type.service';

type SelectableEntity = IBranch | IEmployeeType;

@Component({
  selector: 'jhi-employee-update',
  templateUrl: './employee-update.component.html'
})
export class EmployeeUpdateComponent implements OnInit {
  isSaving = false;

  branches: IBranch[] = [];

  employeetypes: IEmployeeType[] = [];

  editForm = this.fb.group({
    id: [],
    empNumber: [],
    firstName: [null, [Validators.required]],
    lastName: [],
    knownName: [],
    email: [null, [Validators.required, Validators.pattern('^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$')]],
    isActive: [],
    phone: [],
    addressLine1: [],
    addressLine2: [],
    city: [],
    country: [],
    imageURL: [],
    image: [],
    imageContentType: [],
    salary: [],
    designation: [],
    branch: [null, Validators.required],
    empType: [null, Validators.required]
  });

  constructor(
    protected dataUtils: JhiDataUtils,
    protected eventManager: JhiEventManager,
    protected employeeService: EmployeeService,
    protected branchService: BranchService,
    protected employeeTypeService: EmployeeTypeService,
    protected elementRef: ElementRef,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ employee }) => {
      this.updateForm(employee);

      this.branchService
        .query()
        .pipe(
          map((res: HttpResponse<IBranch[]>) => {
            return res.body ? res.body : [];
          })
        )
        .subscribe((resBody: IBranch[]) => (this.branches = resBody));

      this.employeeTypeService
        .query()
        .pipe(
          map((res: HttpResponse<IEmployeeType[]>) => {
            return res.body ? res.body : [];
          })
        )
        .subscribe((resBody: IEmployeeType[]) => (this.employeetypes = resBody));
    });
  }

  updateForm(employee: IEmployee): void {
    this.editForm.patchValue({
      id: employee.id,
      empNumber: employee.empNumber,
      firstName: employee.firstName,
      lastName: employee.lastName,
      knownName: employee.knownName,
      email: employee.email,
      isActive: employee.isActive,
      phone: employee.phone,
      addressLine1: employee.addressLine1,
      addressLine2: employee.addressLine2,
      city: employee.city,
      country: employee.country,
      imageURL: employee.imageURL,
      image: employee.image,
      imageContentType: employee.imageContentType,
      salary: employee.salary,
      designation: employee.designation,
      branch: employee.branch,
      empType: employee.empType
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(contentType: string, base64String: string): void {
    this.dataUtils.openFile(contentType, base64String);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe(null, (err: JhiFileLoadError) => {
      this.eventManager.broadcast(
        new JhiEventWithContent<AlertError>('dbToolApp.error', { message: err.message })
      );
    });
  }

  clearInputImage(field: string, fieldContentType: string, idInput: string): void {
    this.editForm.patchValue({
      [field]: null,
      [fieldContentType]: null
    });
    if (this.elementRef && idInput && this.elementRef.nativeElement.querySelector('#' + idInput)) {
      this.elementRef.nativeElement.querySelector('#' + idInput).value = null;
    }
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const employee = this.createFromForm();
    if (employee.id !== undefined) {
      this.subscribeToSaveResponse(this.employeeService.update(employee));
    } else {
      this.subscribeToSaveResponse(this.employeeService.create(employee));
    }
  }

  private createFromForm(): IEmployee {
    return {
      ...new Employee(),
      id: this.editForm.get(['id'])!.value,
      empNumber: this.editForm.get(['empNumber'])!.value,
      firstName: this.editForm.get(['firstName'])!.value,
      lastName: this.editForm.get(['lastName'])!.value,
      knownName: this.editForm.get(['knownName'])!.value,
      email: this.editForm.get(['email'])!.value,
      isActive: this.editForm.get(['isActive'])!.value,
      phone: this.editForm.get(['phone'])!.value,
      addressLine1: this.editForm.get(['addressLine1'])!.value,
      addressLine2: this.editForm.get(['addressLine2'])!.value,
      city: this.editForm.get(['city'])!.value,
      country: this.editForm.get(['country'])!.value,
      imageURL: this.editForm.get(['imageURL'])!.value,
      imageContentType: this.editForm.get(['imageContentType'])!.value,
      image: this.editForm.get(['image'])!.value,
      salary: this.editForm.get(['salary'])!.value,
      designation: this.editForm.get(['designation'])!.value,
      branch: this.editForm.get(['branch'])!.value,
      empType: this.editForm.get(['empType'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEmployee>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }

  trackById(index: number, item: SelectableEntity): any {
    return item.id;
  }
}
