import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IBank, Bank } from 'app/shared/model/bank.model';
import { BankService } from './bank.service';
import { BankComponent } from './bank.component';
import { BankDetailComponent } from './bank-detail.component';
import { BankUpdateComponent } from './bank-update.component';

@Injectable({ providedIn: 'root' })
export class BankResolve implements Resolve<IBank> {
  constructor(private service: BankService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IBank> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((bank: HttpResponse<Bank>) => {
          if (bank.body) {
            return of(bank.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Bank());
  }
}

export const bankRoute: Routes = [
  {
    path: '',
    component: BankComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'Banks'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: BankDetailComponent,
    resolve: {
      bank: BankResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Banks'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: BankUpdateComponent,
    resolve: {
      bank: BankResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Banks'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: BankUpdateComponent,
    resolve: {
      bank: BankResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Banks'
    },
    canActivate: [UserRouteAccessService]
  }
];
