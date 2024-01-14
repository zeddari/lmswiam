import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { EditionsService } from '../service/editions.service';

import { EditionsComponent } from './editions.component';

describe('Editions Management Component', () => {
  let comp: EditionsComponent;
  let fixture: ComponentFixture<EditionsComponent>;
  let service: EditionsService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        RouterTestingModule.withRoutes([{ path: 'editions', component: EditionsComponent }]),
        HttpClientTestingModule,
        EditionsComponent,
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
      .overrideTemplate(EditionsComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(EditionsComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(EditionsService);

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
    expect(comp.editions?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });

  describe('trackId', () => {
    it('Should forward to editionsService', () => {
      const entity = { id: 123 };
      jest.spyOn(service, 'getEditionsIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getEditionsIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });
});
