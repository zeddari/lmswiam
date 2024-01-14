import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { TypeProjectService } from '../service/type-project.service';

import { TypeProjectComponent } from './type-project.component';

describe('TypeProject Management Component', () => {
  let comp: TypeProjectComponent;
  let fixture: ComponentFixture<TypeProjectComponent>;
  let service: TypeProjectService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        RouterTestingModule.withRoutes([{ path: 'type-project', component: TypeProjectComponent }]),
        HttpClientTestingModule,
        TypeProjectComponent,
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
      .overrideTemplate(TypeProjectComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TypeProjectComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(TypeProjectService);

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
    expect(comp.typeProjects?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });

  describe('trackId', () => {
    it('Should forward to typeProjectService', () => {
      const entity = { id: 123 };
      jest.spyOn(service, 'getTypeProjectIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getTypeProjectIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });
});
