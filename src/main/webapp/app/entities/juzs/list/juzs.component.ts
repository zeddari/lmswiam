import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Data, ParamMap, Router, RouterModule } from '@angular/router';
import { combineLatest, filter, Observable, switchMap, tap } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { SortDirective, SortByDirective } from 'app/shared/sort';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { FormsModule } from '@angular/forms';
import { ASC, DESC, SORT, ITEM_DELETED_EVENT, DEFAULT_SORT_DATA } from 'app/config/navigation.constants';
import { SortService } from 'app/shared/sort/sort.service';
import { IJuzs } from '../juzs.model';
import { EntityArrayResponseType, JuzsService } from '../service/juzs.service';
import { JuzsDeleteDialogComponent } from '../delete/juzs-delete-dialog.component';

@Component({
  standalone: true,
  selector: 'jhi-juzs',
  templateUrl: './juzs.component.html',
  imports: [
    RouterModule,
    FormsModule,
    SharedModule,
    SortDirective,
    SortByDirective,
    DurationPipe,
    FormatMediumDatetimePipe,
    FormatMediumDatePipe,
  ],
})
export class JuzsComponent implements OnInit {
  juzs?: IJuzs[];
  isLoading = false;

  predicate = 'id';
  ascending = true;
  currentSearch = '';

  constructor(
    protected juzsService: JuzsService,
    protected activatedRoute: ActivatedRoute,
    public router: Router,
    protected sortService: SortService,
    protected modalService: NgbModal,
  ) {}

  trackId = (_index: number, item: IJuzs): number => this.juzsService.getJuzsIdentifier(item);

  search(query: string): void {
    this.currentSearch = query;
    this.navigateToWithComponentValues();
  }

  ngOnInit(): void {
    this.load();
  }

  delete(juzs: IJuzs): void {
    const modalRef = this.modalService.open(JuzsDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.juzs = juzs;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed
      .pipe(
        filter(reason => reason === ITEM_DELETED_EVENT),
        switchMap(() => this.loadFromBackendWithRouteInformations()),
      )
      .subscribe({
        next: (res: EntityArrayResponseType) => {
          this.onResponseSuccess(res);
        },
      });
  }

  load(): void {
    this.loadFromBackendWithRouteInformations().subscribe({
      next: (res: EntityArrayResponseType) => {
        this.onResponseSuccess(res);
      },
    });
  }

  navigateToWithComponentValues(): void {
    this.handleNavigation(this.predicate, this.ascending, this.currentSearch);
  }

  protected loadFromBackendWithRouteInformations(): Observable<EntityArrayResponseType> {
    return combineLatest([this.activatedRoute.queryParamMap, this.activatedRoute.data]).pipe(
      tap(([params, data]) => this.fillComponentAttributeFromRoute(params, data)),
      switchMap(() => this.queryBackend(this.predicate, this.ascending, this.currentSearch)),
    );
  }

  protected fillComponentAttributeFromRoute(params: ParamMap, data: Data): void {
    const sort = (params.get(SORT) ?? data[DEFAULT_SORT_DATA]).split(',');
    this.predicate = sort[0];
    this.ascending = sort[1] === ASC;
    if (params.has('search') && params.get('search') !== '') {
      this.currentSearch = params.get('search') as string;
    }
  }

  protected onResponseSuccess(response: EntityArrayResponseType): void {
    const dataFromBody = this.fillComponentAttributesFromResponseBody(response.body);
    this.juzs = this.refineData(dataFromBody);
  }

  protected refineData(data: IJuzs[]): IJuzs[] {
    return data.sort(this.sortService.startSort(this.predicate, this.ascending ? 1 : -1));
  }

  protected fillComponentAttributesFromResponseBody(data: IJuzs[] | null): IJuzs[] {
    return data ?? [];
  }

  protected queryBackend(predicate?: string, ascending?: boolean, currentSearch?: string): Observable<EntityArrayResponseType> {
    this.isLoading = true;
    const queryObject: any = {
      query: currentSearch,
      sort: this.getSortQueryParam(predicate, ascending),
    };
    if (this.currentSearch && this.currentSearch !== '') {
      return this.juzsService.search(queryObject).pipe(tap(() => (this.isLoading = false)));
    } else {
      return this.juzsService.query(queryObject).pipe(tap(() => (this.isLoading = false)));
    }
  }

  protected handleNavigation(predicate?: string, ascending?: boolean, currentSearch?: string): void {
    const queryParamsObj = {
      search: currentSearch,
      sort: this.getSortQueryParam(predicate, ascending),
    };

    this.router.navigate(['./'], {
      relativeTo: this.activatedRoute,
      queryParams: queryParamsObj,
    });
  }

  protected getSortQueryParam(predicate = this.predicate, ascending = this.ascending): string[] {
    const ascendingQueryParam = ascending ? ASC : DESC;
    if (predicate === '') {
      return [];
    } else {
      return [predicate + ',' + ascendingQueryParam];
    }
  }
}
