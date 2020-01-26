import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { DbToolSharedModule } from 'app/shared/shared.module';
import { ConfigurationItemsComponent } from './configuration-items.component';
import { ConfigurationItemsDetailComponent } from './configuration-items-detail.component';
import { ConfigurationItemsUpdateComponent } from './configuration-items-update.component';
import { ConfigurationItemsDeleteDialogComponent } from './configuration-items-delete-dialog.component';
import { configurationItemsRoute } from './configuration-items.route';

@NgModule({
  imports: [DbToolSharedModule, RouterModule.forChild(configurationItemsRoute)],
  declarations: [
    ConfigurationItemsComponent,
    ConfigurationItemsDetailComponent,
    ConfigurationItemsUpdateComponent,
    ConfigurationItemsDeleteDialogComponent
  ],
  entryComponents: [ConfigurationItemsDeleteDialogComponent]
})
export class DbToolConfigurationItemsModule {}
