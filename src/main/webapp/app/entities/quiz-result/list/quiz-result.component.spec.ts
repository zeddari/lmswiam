import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { QuizResultService } from '../service/quiz-result.service';

import { QuizResultComponent } from './quiz-result.component';

describe('QuizResult Management Component', () => {
  let comp: QuizResultComponent;
  let fixture: ComponentFixture<QuizResultComponent>;
  let service: QuizResultService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        RouterTestingModule.withRoutes([{ path: 'quiz-result', component: QuizResultComponent }]),
        HttpClientTestingModule,
        QuizResultComponent,
      ],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            data: of({
              defaultSort: 'id,asc',
            }),
            queryParamMap: of(
              jest.requireActual('@angular/router').convertToParamMap({
                page: '1',
                size: '1',
                sort: 'id,desc',
              }),
            ),
            snapshot: { queryParams: {} },
          },
        },
      ],
    })
      .overrideTemplate(QuizResultComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(QuizResultComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(QuizResultService);

    const headers = new HttpHeaders();
    jest.spyOn(service, 'query').mockReturnValue(
      of(
        new HttpResponse({
          body: [{ id: 123 }],
          headers,
        }),
      ),
    );
  });

  it('Should call load all on init', () => {
    // WHEN
    comp.ngOnInit();

    // THEN
    expect(service.query).toHaveBeenCalled();
    expect(comp.quizResults?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });

  describe('trackId', () => {
    it('Should forward to quizResultService', () => {
      const entity = { id: 123 };
      jest.spyOn(service, 'getQuizResultIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getQuizResultIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });
});
