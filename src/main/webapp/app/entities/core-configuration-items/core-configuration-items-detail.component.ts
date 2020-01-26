import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICoreConfigurationItems } from 'app/shared/model/core-configuration-items.model';

@Component({
  selector: 'jhi-core-configuration-items-detail',
  templateUrl: './core-configuration-items-detail.component.html'
})
export class CoreConfigurationItemsDetailComponent implements OnInit {
  coreConfigurationItems: ICoreConfigurationItems | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ coreConfigurationItems }) => {
      this.coreConfigurationItems = coreConfigurationItems;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
