import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { AyahEditionService } from '../service/ayah-edition.service';

import { AyahEditionComponent } from './ayah-edition.component';

describe('AyahEdition Management Component', () => {
  let comp: AyahEditionComponent;
  let fixture: ComponentFixture<AyahEditionComponent>;
  let service: AyahEditionService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        RouterTestingModule.withRoutes([{ path: 'ayah-edition', component: AyahEditionComponent }]),
        HttpClientTestingModule,
        AyahEditionComponent,
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
      .overrideTemplate(AyahEditionComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AyahEditionComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(AyahEditionService);

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
    expect(comp.ayahEditions?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });

  describe('trackId', () => {
    it('Should forward to ayahEditionService', () => {
      const entity = { id: 123 };
      jest.spyOn(service, 'getAyahEditionIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getAyahEditionIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });
});
