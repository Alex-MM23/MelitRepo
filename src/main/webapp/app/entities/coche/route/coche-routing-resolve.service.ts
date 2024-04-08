import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICoche } from '../coche.model';
import { CocheService } from '../service/coche.service';

const cocheResolve = (route: ActivatedRouteSnapshot): Observable<null | ICoche> => {
  const id = route.params['id'];
  if (id) {
    return inject(CocheService)
      .find(id)
      .pipe(
        mergeMap((coche: HttpResponse<ICoche>) => {
          if (coche.body) {
            return of(coche.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default cocheResolve;
