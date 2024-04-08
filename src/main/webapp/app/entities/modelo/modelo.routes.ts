import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { ModeloComponent } from './list/modelo.component';
import { ModeloDetailComponent } from './detail/modelo-detail.component';
import { ModeloUpdateComponent } from './update/modelo-update.component';
import ModeloResolve from './route/modelo-routing-resolve.service';

const modeloRoute: Routes = [
  {
    path: '',
    component: ModeloComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ModeloDetailComponent,
    resolve: {
      modelo: ModeloResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ModeloUpdateComponent,
    resolve: {
      modelo: ModeloResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ModeloUpdateComponent,
    resolve: {
      modelo: ModeloResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default modeloRoute;
