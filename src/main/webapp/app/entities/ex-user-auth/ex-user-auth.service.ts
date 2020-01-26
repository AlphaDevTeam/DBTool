import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IExUserAuth } from 'app/shared/model/ex-user-auth.model';

type EntityResponseType = HttpResponse<IExUserAuth>;
type EntityArrayResponseType = HttpResponse<IExUserAuth[]>;

@Injectable({ providedIn: 'root' })
export class ExUserAuthService {
  public resourceUrl = SERVER_API_URL + 'api/ex-user-auths';

  constructor(protected http: HttpClient) {}

  create(exUserAuth: IExUserAuth): Observable<EntityResponseType> {
    return this.http.post<IExUserAuth>(this.resourceUrl, exUserAuth, { observe: 'response' });
  }

  update(exUserAuth: IExUserAuth): Observable<EntityResponseType> {
    return this.http.put<IExUserAuth>(this.resourceUrl, exUserAuth, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IExUserAuth>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IExUserAuth[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
