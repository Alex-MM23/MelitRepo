import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICoche, NewCoche } from '../coche.model';

export type PartialUpdateCoche = Partial<ICoche> & Pick<ICoche, 'id'>;

export type EntityResponseType = HttpResponse<ICoche>;
export type EntityArrayResponseType = HttpResponse<ICoche[]>;

@Injectable({ providedIn: 'root' })
export class CocheService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/coches');

  create(coche: NewCoche): Observable<EntityResponseType> {
    return this.http.post<ICoche>(this.resourceUrl, coche, { observe: 'response' });
  }

  update(coche: ICoche): Observable<EntityResponseType> {
    return this.http.put<ICoche>(`${this.resourceUrl}/${this.getCocheIdentifier(coche)}`, coche, { observe: 'response' });
  }

  partialUpdate(coche: PartialUpdateCoche): Observable<EntityResponseType> {
    return this.http.patch<ICoche>(`${this.resourceUrl}/${this.getCocheIdentifier(coche)}`, coche, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICoche>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICoche[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getCocheIdentifier(coche: Pick<ICoche, 'id'>): number {
    return coche.id;
  }

  compareCoche(o1: Pick<ICoche, 'id'> | null, o2: Pick<ICoche, 'id'> | null): boolean {
    return o1 && o2 ? this.getCocheIdentifier(o1) === this.getCocheIdentifier(o2) : o1 === o2;
  }

  addCocheToCollectionIfMissing<Type extends Pick<ICoche, 'id'>>(
    cocheCollection: Type[],
    ...cochesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const coches: Type[] = cochesToCheck.filter(isPresent);
    if (coches.length > 0) {
      const cocheCollectionIdentifiers = cocheCollection.map(cocheItem => this.getCocheIdentifier(cocheItem));
      const cochesToAdd = coches.filter(cocheItem => {
        const cocheIdentifier = this.getCocheIdentifier(cocheItem);
        if (cocheCollectionIdentifiers.includes(cocheIdentifier)) {
          return false;
        }
        cocheCollectionIdentifiers.push(cocheIdentifier);
        return true;
      });
      return [...cochesToAdd, ...cocheCollection];
    }
    return cocheCollection;
  }
}
