import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { QuizResultDetailComponent } from './quiz-result-detail.component';

describe('QuizResult Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [QuizResultDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: QuizResultDetailComponent,
              resolve: { quizResult: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(QuizResultDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load quizResult on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', QuizResultDetailComponent);

      // THEN
      expect(instance.quizResult).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
