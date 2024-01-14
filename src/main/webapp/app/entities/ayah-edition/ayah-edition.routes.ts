import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { AyahEditionComponent } from './list/ayah-edition.component';
import { AyahEditionDetailComponent } from './detail/ayah-edition-detail.component';
import { AyahEditionUpdateComponent } from './update/ayah-edition-update.component';
import AyahEditionResolve from './route/ayah-edition-routing-resolve.service';

const ayahEditionRoute: Routes = [
  {
    path: '',
    component: AyahEditionComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AyahEditionDetailComponent,
    resolve: {
      ayahEdition: AyahEditionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AyahEditionUpdateComponent,
    resolve: {
      ayahEdition: AyahEditionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AyahEditionUpdateComponent,
    resolve: {
      ayahEdition: AyahEditionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default ayahEditionRoute;
