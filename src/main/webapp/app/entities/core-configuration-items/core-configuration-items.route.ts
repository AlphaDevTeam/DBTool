import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { ICoreConfigurationItems, CoreConfigurationItems } from 'app/shared/model/core-configuration-items.model';
import { CoreConfigurationItemsService } from './core-configuration-items.service';
import { CoreConfigurationItemsComponent } from './core-configuration-items.component';
import { CoreConfigurationItemsDetailComponent } from './core-configuration-items-detail.component';
import { CoreConfigurationItemsUpdateComponent } from './core-configuration-items-update.component';

@Injectable({ providedIn: 'root' })
export class CoreConfigurationItemsResolve implements Resolve<ICoreConfigurationItems> {
  constructor(private service: CoreConfigurationItemsService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICoreConfigurationItems> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((coreConfigurationItems: HttpResponse<CoreConfigurationItems>) => {
          if (coreConfigurationItems.body) {
            return of(coreConfigurationItems.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new CoreConfigurationItems());
  }
}

export const coreConfigurationItemsRoute: Routes = [
  {
    path: '',
    component: CoreConfigurationItemsComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'CoreConfigurationItems'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: CoreConfigurationItemsDetailComponent,
    resolve: {
      coreConfigurationItems: CoreConfigurationItemsResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'CoreConfigurationItems'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: CoreConfigurationItemsUpdateComponent,
    resolve: {
      coreConfigurationItems: CoreConfigurationItemsResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'CoreConfigurationItems'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: CoreConfigurationItemsUpdateComponent,
    resolve: {
      coreConfigurationItems: CoreConfigurationItemsResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'CoreConfigurationItems'
    },
    canActivate: [UserRouteAccessService]
  }
];
