import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { HizbsDetailComponent } from './hizbs-detail.component';

describe('Hizbs Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [HizbsDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: HizbsDetailComponent,
              resolve: { hizbs: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(HizbsDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load hizbs on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', HizbsDetailComponent);

      // THEN
      expect(instance.hizbs).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
