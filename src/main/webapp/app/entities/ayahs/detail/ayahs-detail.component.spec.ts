import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { AyahsDetailComponent } from './ayahs-detail.component';

describe('Ayahs Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AyahsDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: AyahsDetailComponent,
              resolve: { ayahs: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(AyahsDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load ayahs on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', AyahsDetailComponent);

      // THEN
      expect(instance.ayahs).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
