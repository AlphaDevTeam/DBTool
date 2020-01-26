import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { DbToolSharedModule } from 'app/shared/shared.module';
import { CoreConfigurationItemsComponent } from './core-configuration-items.component';
import { CoreConfigurationItemsDetailComponent } from './core-configuration-items-detail.component';
import { CoreConfigurationItemsUpdateComponent } from './core-configuration-items-update.component';
import { CoreConfigurationItemsDeleteDialogComponent } from './core-configuration-items-delete-dialog.component';
import { coreConfigurationItemsRoute } from './core-configuration-items.route';

@NgModule({
  imports: [DbToolSharedModule, RouterModule.forChild(coreConfigurationItemsRoute)],
  declarations: [
    CoreConfigurationItemsComponent,
    CoreConfigurationItemsDetailComponent,
    CoreConfigurationItemsUpdateComponent,
    CoreConfigurationItemsDeleteDialogComponent
  ],
  entryComponents: [CoreConfigurationItemsDeleteDialogComponent]
})
export class DbToolCoreConfigurationItemsModule {}
