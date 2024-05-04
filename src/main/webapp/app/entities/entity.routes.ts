import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'departement',
    data: { pageTitle: 'lmswiamApp.departement.home.title' },
    loadChildren: () => import('./departement/departement.routes'),
  },
  {
    path: 'country',
    data: { pageTitle: 'lmswiamApp.country.home.title' },
    loadChildren: () => import('./country/country.routes'),
  },
  {
    path: 'nationality',
    data: { pageTitle: 'lmswiamApp.nationality.home.title' },
    loadChildren: () => import('./nationality/nationality.routes'),
  },
  {
    path: 'language',
    data: { pageTitle: 'lmswiamApp.language.home.title' },
    loadChildren: () => import('./language/language.routes'),
  },
  {
    path: 'job',
    data: { pageTitle: 'lmswiamApp.job.home.title' },
    loadChildren: () => import('./job/job.routes'),
  },
  {
    path: 'diploma',
    data: { pageTitle: 'lmswiamApp.diploma.home.title' },
    loadChildren: () => import('./diploma/diploma.routes'),
  },
  {
    path: 'user-custom',
    data: { pageTitle: 'lmswiamApp.userCustom.home.title' },
    loadChildren: () => import('./user-custom/user-custom.routes'),
  },
  {
    path: 'topic',
    data: { pageTitle: 'lmswiamApp.topic.home.title' },
    loadChildren: () => import('./topic/topic.routes'),
  },
  {
    path: 'course',
    data: { pageTitle: 'lmswiamApp.course.home.title' },
    loadChildren: () => import('./course/course.routes'),
  },
  {
    path: 'part',
    data: { pageTitle: 'lmswiamApp.part.home.title' },
    loadChildren: () => import('./part/part.routes'),
  },
  {
    path: 'review',
    data: { pageTitle: 'lmswiamApp.review.home.title' },
    loadChildren: () => import('./review/review.routes'),
  },
  {
    path: 'enrolement',
    data: { pageTitle: 'lmswiamApp.enrolement.home.title' },
    loadChildren: () => import('./enrolement/enrolement.routes'),
  },
  {
    path: 'question',
    data: { pageTitle: 'lmswiamApp.question.home.title' },
    loadChildren: () => import('./question/question.routes'),
  },
  {
    path: 'answer',
    data: { pageTitle: 'lmswiamApp.answer.home.title' },
    loadChildren: () => import('./answer/answer.routes'),
  },
  {
    path: 'quiz',
    data: { pageTitle: 'lmswiamApp.quiz.home.title' },
    loadChildren: () => import('./quiz/quiz.routes'),
  },
  {
    path: 'quiz-result',
    data: { pageTitle: 'lmswiamApp.quizResult.home.title' },
    loadChildren: () => import('./quiz-result/quiz-result.routes'),
  },
  {
    path: 'certificate',
    data: { pageTitle: 'lmswiamApp.certificate.home.title' },
    loadChildren: () => import('./certificate/certificate.routes'),
  },
  {
    path: 'site',
    data: { pageTitle: 'lmswiamApp.site.home.title' },
    loadChildren: () => import('./site/site.routes'),
  },
  {
    path: 'city',
    data: { pageTitle: 'lmswiamApp.city.home.title' },
    loadChildren: () => import('./city/city.routes'),
  },
  {
    path: 'classroom',
    data: { pageTitle: 'lmswiamApp.classroom.home.title' },
    loadChildren: () => import('./classroom/classroom.routes'),
  },
  {
    path: 'group',
    data: { pageTitle: 'lmswiamApp.group.home.title' },
    loadChildren: () => import('./group/group.routes'),
  },
  {
    path: 'session-link',
    data: { pageTitle: 'lmswiamApp.sessionLink.home.title' },
    loadChildren: () => import('./session-link/session-link.routes'),
  },
  {
    path: 'session',
    data: { pageTitle: 'lmswiamApp.session.home.title' },
    loadChildren: () => import('./session/session.routes'),
  },
  {
    path: 'session-instance',
    data: { pageTitle: 'lmswiamApp.sessionInstance.home.title' },
    loadChildren: () => import('./session-instance/session-instance.routes'),
  },
  {
    path: 'progression',
    data: { pageTitle: 'lmswiamApp.progression.home.title' },
    loadChildren: () => import('./progression/progression.routes'),
  },
  {
    path: 'tickets',
    data: { pageTitle: 'lmswiamApp.tickets.home.title' },
    loadChildren: () => import('./tickets/tickets.routes'),
  },
  {
    path: 'type-project',
    data: { pageTitle: 'lmswiamApp.typeProject.home.title' },
    loadChildren: () => import('./type-project/type-project.routes'),
  },
  {
    path: 'project',
    data: { pageTitle: 'lmswiamApp.project.home.title' },
    loadChildren: () => import('./project/project.routes'),
  },
  {
    path: 'sponsoring',
    data: { pageTitle: 'lmswiamApp.sponsoring.home.title' },
    loadChildren: () => import('./sponsoring/sponsoring.routes'),
  },
  {
    path: 'currency',
    data: { pageTitle: 'lmswiamApp.currency.home.title' },
    loadChildren: () => import('./currency/currency.routes'),
  },
  {
    path: 'payment',
    data: { pageTitle: 'lmswiamApp.payment.home.title' },
    loadChildren: () => import('./payment/payment.routes'),
  },
  {
    path: 'ayahs',
    data: { pageTitle: 'lmswiamApp.ayahs.home.title' },
    loadChildren: () => import('./ayahs/ayahs.routes'),
  },
  {
    path: 'ayah-edition',
    data: { pageTitle: 'lmswiamApp.ayahEdition.home.title' },
    loadChildren: () => import('./ayah-edition/ayah-edition.routes'),
  },
  {
    path: 'hizbs',
    data: { pageTitle: 'lmswiamApp.hizbs.home.title' },
    loadChildren: () => import('./hizbs/hizbs.routes'),
  },
  {
    path: 'juzs',
    data: { pageTitle: 'lmswiamApp.juzs.home.title' },
    loadChildren: () => import('./juzs/juzs.routes'),
  },
  {
    path: 'surahs',
    data: { pageTitle: 'lmswiamApp.surahs.home.title' },
    loadChildren: () => import('./surahs/surahs.routes'),
  },
  {
    path: 'editions',
    data: { pageTitle: 'lmswiamApp.editions.home.title' },
    loadChildren: () => import('./editions/editions.routes'),
  },
  {
    path: 'comments',
    data: { pageTitle: 'lmswiamApp.comments.home.title' },
    loadChildren: () => import('./comments/comments.routes'),
  },
  {
    path: 'depense',
    data: { pageTitle: 'lmswiamApp.depense.home.title' },
    loadChildren: () => import('./depense/depense.routes'),
  },
  {
    path: 'student-sponsoring',
    data: { pageTitle: 'lmswiamApp.studentSponsoring.home.title' },
    loadChildren: () => import('./student-sponsoring/student-sponsoring.routes'),
  },
  {
    path: 'session-courses',
    data: { pageTitle: 'lmswiamApp.sessionCourses.home.title' },
    loadChildren: () => import('./session-courses/session-courses.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
