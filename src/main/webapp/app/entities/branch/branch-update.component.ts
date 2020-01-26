import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { IBranch, Branch } from 'app/shared/model/branch.model';
import { BranchService } from './branch.service';
import { IBank } from 'app/shared/model/bank.model';
import { BankService } from 'app/entities/bank/bank.service';

@Component({
  selector: 'jhi-branch-update',
  templateUrl: './branch-update.component.html'
})
export class BranchUpdateComponent implements OnInit {
  isSaving = false;

  banks: IBank[] = [];

  editForm = this.fb.group({
    id: [],
    branchCode: [null, [Validators.required]],
    branchName: [null, [Validators.required]],
    isActive: [],
    bank: [null, Validators.required]
  });

  constructor(
    protected branchService: BranchService,
    protected bankService: BankService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ branch }) => {
      this.updateForm(branch);

      this.bankService
        .query()
        .pipe(
          map((res: HttpResponse<IBank[]>) => {
            return res.body ? res.body : [];
          })
        )
        .subscribe((resBody: IBank[]) => (this.banks = resBody));
    });
  }

  updateForm(branch: IBranch): void {
    this.editForm.patchValue({
      id: branch.id,
      branchCode: branch.branchCode,
      branchName: branch.branchName,
      isActive: branch.isActive,
      bank: branch.bank
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const branch = this.createFromForm();
    if (branch.id !== undefined) {
      this.subscribeToSaveResponse(this.branchService.update(branch));
    } else {
      this.subscribeToSaveResponse(this.branchService.create(branch));
    }
  }

  private createFromForm(): IBranch {
    return {
      ...new Branch(),
      id: this.editForm.get(['id'])!.value,
      branchCode: this.editForm.get(['branchCode'])!.value,
      branchName: this.editForm.get(['branchName'])!.value,
      isActive: this.editForm.get(['isActive'])!.value,
      bank: this.editForm.get(['bank'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IBranch>>): void {
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

  trackById(index: number, item: IBank): any {
    return item.id;
  }
}
