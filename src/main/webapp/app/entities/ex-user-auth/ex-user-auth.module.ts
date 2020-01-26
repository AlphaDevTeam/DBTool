import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { DbToolSharedModule } from 'app/shared/shared.module';
import { ExUserAuthComponent } from './ex-user-auth.component';
import { ExUserAuthDetailComponent } from './ex-user-auth-detail.component';
import { ExUserAuthUpdateComponent } from './ex-user-auth-update.component';
import { ExUserAuthDeleteDialogComponent } from './ex-user-auth-delete-dialog.component';
import { exUserAuthRoute } from './ex-user-auth.route';

@NgModule({
  imports: [DbToolSharedModule, RouterModule.forChild(exUserAuthRoute)],
  declarations: [ExUserAuthComponent, ExUserAuthDetailComponent, ExUserAuthUpdateComponent, ExUserAuthDeleteDialogComponent],
  entryComponents: [ExUserAuthDeleteDialogComponent]
})
export class DbToolExUserAuthModule {}
