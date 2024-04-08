import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'authority',
    data: { pageTitle: 'appApp.adminAuthority.home.title' },
    loadChildren: () => import('./admin/authority/authority.routes'),
  },
  {
    path: 'venta',
    data: { pageTitle: 'appApp.venta.home.title' },
    loadChildren: () => import('./venta/venta.routes'),
  },
  {
    path: 'coche',
    data: { pageTitle: 'appApp.coche.home.title' },
    loadChildren: () => import('./coche/coche.routes'),
  },
  {
    path: 'empleado',
    data: { pageTitle: 'appApp.empleado.home.title' },
    loadChildren: () => import('./empleado/empleado.routes'),
  },
  {
    path: 'cliente',
    data: { pageTitle: 'appApp.cliente.home.title' },
    loadChildren: () => import('./cliente/cliente.routes'),
  },
  {
    path: 'marca',
    data: { pageTitle: 'appApp.marca.home.title' },
    loadChildren: () => import('./marca/marca.routes'),
  },
  {
    path: 'modelo',
    data: { pageTitle: 'appApp.modelo.home.title' },
    loadChildren: () => import('./modelo/modelo.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
