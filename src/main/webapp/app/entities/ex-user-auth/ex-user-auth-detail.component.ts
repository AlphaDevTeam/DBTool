import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IExUserAuth } from 'app/shared/model/ex-user-auth.model';

@Component({
  selector: 'jhi-ex-user-auth-detail',
  templateUrl: './ex-user-auth-detail.component.html'
})
export class ExUserAuthDetailComponent implements OnInit {
  exUserAuth: IExUserAuth | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ exUserAuth }) => {
      this.exUserAuth = exUserAuth;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
