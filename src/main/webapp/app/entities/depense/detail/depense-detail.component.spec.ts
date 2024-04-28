import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { DepenseDetailComponent } from './depense-detail.component';

describe('Depense Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DepenseDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: DepenseDetailComponent,
              resolve: { depense: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(DepenseDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load depense on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', DepenseDetailComponent);

      // THEN
      expect(instance.depense).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
