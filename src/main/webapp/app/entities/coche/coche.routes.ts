import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { CocheComponent } from './list/coche.component';
import { CocheDetailComponent } from './detail/coche-detail.component';
import { CocheUpdateComponent } from './update/coche-update.component';
import CocheResolve from './route/coche-routing-resolve.service';

const cocheRoute: Routes = [
  {
    path: '',
    component: CocheComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CocheDetailComponent,
    resolve: {
      coche: CocheResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CocheUpdateComponent,
    resolve: {
      coche: CocheResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CocheUpdateComponent,
    resolve: {
      coche: CocheResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default cocheRoute;
