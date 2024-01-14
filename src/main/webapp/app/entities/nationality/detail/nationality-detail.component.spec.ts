import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { NationalityDetailComponent } from './nationality-detail.component';

describe('Nationality Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [NationalityDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: NationalityDetailComponent,
              resolve: { nationality: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(NationalityDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load nationality on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', NationalityDetailComponent);

      // THEN
      expect(instance.nationality).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
