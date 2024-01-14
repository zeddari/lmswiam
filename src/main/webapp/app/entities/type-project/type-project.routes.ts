import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { TypeProjectComponent } from './list/type-project.component';
import { TypeProjectDetailComponent } from './detail/type-project-detail.component';
import { TypeProjectUpdateComponent } from './update/type-project-update.component';
import TypeProjectResolve from './route/type-project-routing-resolve.service';

const typeProjectRoute: Routes = [
  {
    path: '',
    component: TypeProjectComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TypeProjectDetailComponent,
    resolve: {
      typeProject: TypeProjectResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TypeProjectUpdateComponent,
    resolve: {
      typeProject: TypeProjectResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TypeProjectUpdateComponent,
    resolve: {
      typeProject: TypeProjectResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default typeProjectRoute;
