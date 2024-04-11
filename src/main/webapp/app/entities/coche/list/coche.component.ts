import { Component, NgZone, inject, OnInit } from '@angular/core';
import { ActivatedRoute, Data, ParamMap, Router, RouterModule } from '@angular/router';
import { combineLatest, filter, Observable, Subscription, tap } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { sortStateSignal, SortDirective, SortByDirective, type SortState, SortService } from 'app/shared/sort';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { FormsModule } from '@angular/forms';
import { SORT, ITEM_DELETED_EVENT, DEFAULT_SORT_DATA } from 'app/config/navigation.constants';
import { ICoche } from '../coche.model';
import { EntityArrayResponseType, CocheService } from '../service/coche.service';
import { CocheDeleteDialogComponent } from '../delete/coche-delete-dialog.component';
import { faThumbsDown } from '@fortawesome/free-solid-svg-icons';

const arrPlates = ['BBB', 'BDR', 'BRT', 'CDC', 'CRC', 'DFF', 'DVB', 'FKC', 'FYY', 'GKH', 'GSR', 'HBG', 'HHT', 'HNK', 'HVF', 'JBY', 'JKZ','JVZ','KGN','KSS','LDR','LMC','LVV','MDD','MMN','MPL']; 

@Component({
  standalone: true,
  selector: 'jhi-coche',
  templateUrl: './coche.component.html',
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
  styleUrls: ['./coche.component.scss'],
})
export class CocheComponent implements OnInit {
  subscription: Subscription | null = null;
  coches?: ICoche[];
  isLoading = false;

  sortState = sortStateSignal({});

  public router = inject(Router);
  protected cocheService = inject(CocheService);
  protected activatedRoute = inject(ActivatedRoute);
  protected sortService = inject(SortService);
  protected modalService = inject(NgbModal);
  protected ngZone = inject(NgZone);

  trackId = (_index: number, item: ICoche): number => this.cocheService.getCocheIdentifier(item);

  ngOnInit(): void {
    this.subscription = combineLatest([this.activatedRoute.queryParamMap, this.activatedRoute.data])
      .pipe(
        tap(([params, data]) => this.fillComponentAttributeFromRoute(params, data)),
        tap(() => {
          if (!this.coches || this.coches.length === 0) {
            this.load();
          }
        }),
      )
      .subscribe();
  }

  delete(coche: ICoche): void {
    const modalRef = this.modalService.open(CocheDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.coche = coche;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed
      .pipe(
        filter(reason => reason === ITEM_DELETED_EVENT),
        tap(() => this.load()),
      )
      .subscribe();
  }

  load(): void {
    this.queryBackend().subscribe({
      next: (res: EntityArrayResponseType) => {
        this.onResponseSuccess(res);
      },
    });
  }

  navigateToWithComponentValues(event: SortState): void {
    this.handleNavigation(event);
  }

  protected fillComponentAttributeFromRoute(params: ParamMap, data: Data): void {
    this.sortState.set(this.sortService.parseSortParam(params.get(SORT) ?? data[DEFAULT_SORT_DATA]));
  }

  protected onResponseSuccess(response: EntityArrayResponseType): void {
    const dataFromBody = this.fillComponentAttributesFromResponseBody(response.body);
    this.coches = this.refineData(dataFromBody);
  }

  protected refineData(data: ICoche[]): ICoche[] {
    const { predicate, order } = this.sortState();
    for(let coche of data){
      if(coche.matricula != null){
        const letrasPlate = this.extractPlateLetters(coche.matricula);
        if(!/[AEIOUQÑ]/.test(letrasPlate)){
          const year = this.findYearByPlate(arrPlates, letrasPlate);
          coche.anio = year as number;
          coche.pegatina = this.pegatina(coche);
        }
      }
    }
    console.log(data);
    return predicate && order ? data.sort(this.sortService.startSort({ predicate, order })) : data;
  }

  protected pegatina(coche: ICoche): string{
    if(coche.anio !== undefined && coche.anio !== null){
      if(coche.anio < 2000 && coche.motor !== 'ELECTRICO'){
        return 'A';
      }else{
        if(coche.motor === 'DIESEL' || coche.motor === 'GASOLINA'){
          if(coche.anio >= 2000 && coche.anio <= 2005){
            return 'B';
          }
          if(coche.anio >= 2006 && coche.anio <= new Date().getFullYear()){
            return 'C';
          }
        }
      }
      if(coche.motor === 'ELECTRICO'){
        return '0';
      }
    }
    return '';
  }

  protected extractPlateLetters(str: string): string {
    return str.slice(-3);
  }

  protected findYearByPlate(arrYearPlate: string [], plate: string): number | null {
    const año = 2000;
    if(plate >= arrYearPlate[0]) {
      for(let i = 0; i < arrYearPlate.length; i++){
        if(plate >= arrPlates[i] && plate <= arrPlates[i + 1]){
          return año + i;
        }
        if(plate > arrPlates[arrYearPlate.length - 1]){
          return 2024;
        }
      }
    }else{
      return 1999;
    }
    return null;
  }

  protected fillComponentAttributesFromResponseBody(data: ICoche[] | null): ICoche[] {
    return data ?? [];
  }

  protected queryBackend(): Observable<EntityArrayResponseType> {
    this.isLoading = true;
    const queryObject: any = {
      sort: this.sortService.buildSortParam(this.sortState()),
    };
    return this.cocheService.query(queryObject).pipe(tap(() => (this.isLoading = false)));
  }

  protected handleNavigation(sortState: SortState): void {
    const queryParamsObj = {
      sort: this.sortService.buildSortParam(sortState),
    };

    this.ngZone.run(() => {
      this.router.navigate(['./'], {
        relativeTo: this.activatedRoute,
        queryParams: queryParamsObj,
      });
    });
  }
}
