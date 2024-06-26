import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { VentaComponent } from './list/venta.component';
import { VentaDetailComponent } from './detail/venta-detail.component';
import { VentaUpdateComponent } from './update/venta-update.component';
import VentaResolve from './route/venta-routing-resolve.service';

const ventaRoute: Routes = [
  {
    path: '',
    component: VentaComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: VentaDetailComponent,
    resolve: {
      venta: VentaResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: VentaUpdateComponent,
    resolve: {
      venta: VentaResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: VentaUpdateComponent,
    resolve: {
      venta: VentaResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default ventaRoute;
