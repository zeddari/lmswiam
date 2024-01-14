import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { TypeProjectDetailComponent } from './type-project-detail.component';

describe('TypeProject Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TypeProjectDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: TypeProjectDetailComponent,
              resolve: { typeProject: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(TypeProjectDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load typeProject on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', TypeProjectDetailComponent);

      // THEN
      expect(instance.typeProject).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
