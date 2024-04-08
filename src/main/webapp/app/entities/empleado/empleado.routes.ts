import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { EmpleadoComponent } from './list/empleado.component';
import { EmpleadoDetailComponent } from './detail/empleado-detail.component';
import { EmpleadoUpdateComponent } from './update/empleado-update.component';
import EmpleadoResolve from './route/empleado-routing-resolve.service';

const empleadoRoute: Routes = [
  {
    path: '',
    component: EmpleadoComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: EmpleadoDetailComponent,
    resolve: {
      empleado: EmpleadoResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: EmpleadoUpdateComponent,
    resolve: {
      empleado: EmpleadoResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: EmpleadoUpdateComponent,
    resolve: {
      empleado: EmpleadoResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default empleadoRoute;
