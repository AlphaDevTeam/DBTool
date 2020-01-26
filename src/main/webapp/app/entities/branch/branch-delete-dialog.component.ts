import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IBranch } from 'app/shared/model/branch.model';
import { BranchService } from './branch.service';

@Component({
  templateUrl: './branch-delete-dialog.component.html'
})
export class BranchDeleteDialogComponent {
  branch?: IBranch;

  constructor(protected branchService: BranchService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  clear(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.branchService.delete(id).subscribe(() => {
      this.eventManager.broadcast('branchListModification');
      this.activeModal.close();
    });
  }
}
