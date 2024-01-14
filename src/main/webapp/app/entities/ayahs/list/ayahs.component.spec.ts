import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { AyahsService } from '../service/ayahs.service';

import { AyahsComponent } from './ayahs.component';

describe('Ayahs Management Component', () => {
  let comp: AyahsComponent;
  let fixture: ComponentFixture<AyahsComponent>;
  let service: AyahsService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [RouterTestingModule.withRoutes([{ path: 'ayahs', component: AyahsComponent }]), HttpClientTestingModule, AyahsComponent],
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
      .overrideTemplate(AyahsComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AyahsComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(AyahsService);

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
    expect(comp.ayahs?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });

  describe('trackId', () => {
    it('Should forward to ayahsService', () => {
      const entity = { id: 123 };
      jest.spyOn(service, 'getAyahsIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getAyahsIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });
});
