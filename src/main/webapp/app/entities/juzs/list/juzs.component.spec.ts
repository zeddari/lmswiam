import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { JuzsService } from '../service/juzs.service';

import { JuzsComponent } from './juzs.component';

describe('Juzs Management Component', () => {
  let comp: JuzsComponent;
  let fixture: ComponentFixture<JuzsComponent>;
  let service: JuzsService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [RouterTestingModule.withRoutes([{ path: 'juzs', component: JuzsComponent }]), HttpClientTestingModule, JuzsComponent],
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
      .overrideTemplate(JuzsComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(JuzsComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(JuzsService);

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
    expect(comp.juzs?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });

  describe('trackId', () => {
    it('Should forward to juzsService', () => {
      const entity = { id: 123 };
      jest.spyOn(service, 'getJuzsIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getJuzsIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });
});
