import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { AyahEditionDetailComponent } from './ayah-edition-detail.component';

describe('AyahEdition Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AyahEditionDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: AyahEditionDetailComponent,
              resolve: { ayahEdition: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(AyahEditionDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load ayahEdition on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', AyahEditionDetailComponent);

      // THEN
      expect(instance.ayahEdition).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
