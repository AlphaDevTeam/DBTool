import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IBank } from 'app/shared/model/bank.model';
import { BankService } from './bank.service';

@Component({
  templateUrl: './bank-delete-dialog.component.html'
})
export class BankDeleteDialogComponent {
  bank?: IBank;

  constructor(protected bankService: BankService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  clear(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.bankService.delete(id).subscribe(() => {
      this.eventManager.broadcast('bankListModification');
      this.activeModal.close();
    });
  }
}
