import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { StudentSponsoringService } from '../service/student-sponsoring.service';

import { StudentSponsoringComponent } from './student-sponsoring.component';

describe('StudentSponsoring Management Component', () => {
  let comp: StudentSponsoringComponent;
  let fixture: ComponentFixture<StudentSponsoringComponent>;
  let service: StudentSponsoringService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        RouterTestingModule.withRoutes([{ path: 'student-sponsoring', component: StudentSponsoringComponent }]),
        HttpClientTestingModule,
        StudentSponsoringComponent,
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
      .overrideTemplate(StudentSponsoringComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(StudentSponsoringComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(StudentSponsoringService);

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
    expect(comp.studentSponsorings?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });

  describe('trackId', () => {
    it('Should forward to studentSponsoringService', () => {
      const entity = { id: 123 };
      jest.spyOn(service, 'getStudentSponsoringIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getStudentSponsoringIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });
});
