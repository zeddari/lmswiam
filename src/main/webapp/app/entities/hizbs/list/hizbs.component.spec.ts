import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { HizbsService } from '../service/hizbs.service';

import { HizbsComponent } from './hizbs.component';

describe('Hizbs Management Component', () => {
  let comp: HizbsComponent;
  let fixture: ComponentFixture<HizbsComponent>;
  let service: HizbsService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [RouterTestingModule.withRoutes([{ path: 'hizbs', component: HizbsComponent }]), HttpClientTestingModule, HizbsComponent],
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
      .overrideTemplate(HizbsComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(HizbsComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(HizbsService);

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
    expect(comp.hizbs?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });

  describe('trackId', () => {
    it('Should forward to hizbsService', () => {
      const entity = { id: 123 };
      jest.spyOn(service, 'getHizbsIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getHizbsIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });
});
