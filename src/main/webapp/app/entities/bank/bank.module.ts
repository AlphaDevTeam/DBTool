import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { DbToolSharedModule } from 'app/shared/shared.module';
import { BankComponent } from './bank.component';
import { BankDetailComponent } from './bank-detail.component';
import { BankUpdateComponent } from './bank-update.component';
import { BankDeleteDialogComponent } from './bank-delete-dialog.component';
import { bankRoute } from './bank.route';

@NgModule({
  imports: [DbToolSharedModule, RouterModule.forChild(bankRoute)],
  declarations: [BankComponent, BankDetailComponent, BankUpdateComponent, BankDeleteDialogComponent],
  entryComponents: [BankDeleteDialogComponent]
})
export class DbToolBankModule {}
