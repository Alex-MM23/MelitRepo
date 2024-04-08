import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { MarcaComponent } from './list/marca.component';
import { MarcaDetailComponent } from './detail/marca-detail.component';
import { MarcaUpdateComponent } from './update/marca-update.component';
import MarcaResolve from './route/marca-routing-resolve.service';

const marcaRoute: Routes = [
  {
    path: '',
    component: MarcaComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: MarcaDetailComponent,
    resolve: {
      marca: MarcaResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: MarcaUpdateComponent,
    resolve: {
      marca: MarcaResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: MarcaUpdateComponent,
    resolve: {
      marca: MarcaResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default marcaRoute;
