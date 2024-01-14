import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { JuzsDetailComponent } from './juzs-detail.component';

describe('Juzs Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [JuzsDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: JuzsDetailComponent,
              resolve: { juzs: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(JuzsDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load juzs on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', JuzsDetailComponent);

      // THEN
      expect(instance.juzs).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
