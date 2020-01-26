import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { DbToolSharedModule } from 'app/shared/shared.module';
import { ExUserComponent } from './ex-user.component';
import { ExUserDetailComponent } from './ex-user-detail.component';
import { ExUserUpdateComponent } from './ex-user-update.component';
import { ExUserDeleteDialogComponent } from './ex-user-delete-dialog.component';
import { exUserRoute } from './ex-user.route';

@NgModule({
  imports: [DbToolSharedModule, RouterModule.forChild(exUserRoute)],
  declarations: [ExUserComponent, ExUserDetailComponent, ExUserUpdateComponent, ExUserDeleteDialogComponent],
  entryComponents: [ExUserDeleteDialogComponent]
})
export class DbToolExUserModule {}
