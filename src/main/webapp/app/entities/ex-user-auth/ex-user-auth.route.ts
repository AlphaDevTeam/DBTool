import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IExUserAuth, ExUserAuth } from 'app/shared/model/ex-user-auth.model';
import { ExUserAuthService } from './ex-user-auth.service';
import { ExUserAuthComponent } from './ex-user-auth.component';
import { ExUserAuthDetailComponent } from './ex-user-auth-detail.component';
import { ExUserAuthUpdateComponent } from './ex-user-auth-update.component';

@Injectable({ providedIn: 'root' })
export class ExUserAuthResolve implements Resolve<IExUserAuth> {
  constructor(private service: ExUserAuthService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IExUserAuth> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((exUserAuth: HttpResponse<ExUserAuth>) => {
          if (exUserAuth.body) {
            return of(exUserAuth.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ExUserAuth());
  }
}

export const exUserAuthRoute: Routes = [
  {
    path: '',
    component: ExUserAuthComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'ExUserAuths'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: ExUserAuthDetailComponent,
    resolve: {
      exUserAuth: ExUserAuthResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'ExUserAuths'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: ExUserAuthUpdateComponent,
    resolve: {
      exUserAuth: ExUserAuthResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'ExUserAuths'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: ExUserAuthUpdateComponent,
    resolve: {
      exUserAuth: ExUserAuthResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'ExUserAuths'
    },
    canActivate: [UserRouteAccessService]
  }
];
