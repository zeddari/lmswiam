import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { QuizResultComponent } from './list/quiz-result.component';
import { QuizResultDetailComponent } from './detail/quiz-result-detail.component';
import { QuizResultUpdateComponent } from './update/quiz-result-update.component';
import QuizResultResolve from './route/quiz-result-routing-resolve.service';

const quizResultRoute: Routes = [
  {
    path: '',
    component: QuizResultComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: QuizResultDetailComponent,
    resolve: {
      quizResult: QuizResultResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: QuizResultUpdateComponent,
    resolve: {
      quizResult: QuizResultResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: QuizResultUpdateComponent,
    resolve: {
      quizResult: QuizResultResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default quizResultRoute;
