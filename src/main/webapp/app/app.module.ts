import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import './vendor';
import { DbToolSharedModule } from 'app/shared/shared.module';
import { DbToolCoreModule } from 'app/core/core.module';
import { DbToolAppRoutingModule } from './app-routing.module';
import { DbToolHomeModule } from './home/home.module';
import { DbToolEntityModule } from './entities/entity.module';
// jhipster-needle-angular-add-module-import JHipster will add new module here
import { MainComponent } from './layouts/main/main.component';
import { NavbarComponent } from './layouts/navbar/navbar.component';
import { FooterComponent } from './layouts/footer/footer.component';
import { PageRibbonComponent } from './layouts/profiles/page-ribbon.component';
import { ErrorComponent } from './layouts/error/error.component';

@NgModule({
  imports: [
    BrowserModule,
    DbToolSharedModule,
    DbToolCoreModule,
    DbToolHomeModule,
    // jhipster-needle-angular-add-module JHipster will add new module here
    DbToolEntityModule,
    DbToolAppRoutingModule
  ],
  declarations: [MainComponent, NavbarComponent, ErrorComponent, PageRibbonComponent, FooterComponent],
  bootstrap: [MainComponent]
})
export class DbToolAppModule {}
