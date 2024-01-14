import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { NationalityService } from '../service/nationality.service';

import { NationalityComponent } from './nationality.component';

describe('Nationality Management Component', () => {
  let comp: NationalityComponent;
  let fixture: ComponentFixture<NationalityComponent>;
  let service: NationalityService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        RouterTestingModule.withRoutes([{ path: 'nationality', component: NationalityComponent }]),
        HttpClientTestingModule,
        NationalityComponent,
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
      .overrideTemplate(NationalityComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(NationalityComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(NationalityService);

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
    expect(comp.nationalities?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });

  describe('trackId', () => {
    it('Should forward to nationalityService', () => {
      const entity = { id: 123 };
      jest.spyOn(service, 'getNationalityIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getNationalityIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });
});
