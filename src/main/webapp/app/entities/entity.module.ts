import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'user-permissions',
        loadChildren: () => import('./user-permissions/user-permissions.module').then(m => m.DbToolUserPermissionsModule)
      },
      {
        path: 'user-group',
        loadChildren: () => import('./user-group/user-group.module').then(m => m.DbToolUserGroupModule)
      },
      {
        path: 'menu-items',
        loadChildren: () => import('./menu-items/menu-items.module').then(m => m.DbToolMenuItemsModule)
      },
      {
        path: 'employee-type',
        loadChildren: () => import('./employee-type/employee-type.module').then(m => m.DbToolEmployeeTypeModule)
      },
      {
        path: 'employee',
        loadChildren: () => import('./employee/employee.module').then(m => m.DbToolEmployeeModule)
      },
      {
        path: 'ex-user',
        loadChildren: () => import('./ex-user/ex-user.module').then(m => m.DbToolExUserModule)
      },
      {
        path: 'ex-user-auth',
        loadChildren: () => import('./ex-user-auth/ex-user-auth.module').then(m => m.DbToolExUserAuthModule)
      },
      {
        path: 'license-type',
        loadChildren: () => import('./license-type/license-type.module').then(m => m.DbToolLicenseTypeModule)
      },
      {
        path: 'company',
        loadChildren: () => import('./company/company.module').then(m => m.DbToolCompanyModule)
      },
      {
        path: 'core-configuration-items',
        loadChildren: () =>
          import('./core-configuration-items/core-configuration-items.module').then(m => m.DbToolCoreConfigurationItemsModule)
      },
      {
        path: 'configuration-items',
        loadChildren: () => import('./configuration-items/configuration-items.module').then(m => m.DbToolConfigurationItemsModule)
      },
      {
        path: 'bank',
        loadChildren: () => import('./bank/bank.module').then(m => m.DbToolBankModule)
      },
      {
        path: 'branch',
        loadChildren: () => import('./branch/branch.module').then(m => m.DbToolBranchModule)
      }
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ])
  ]
})
export class DbToolEntityModule {}
