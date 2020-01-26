import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IBranch } from 'app/shared/model/branch.model';

type EntityResponseType = HttpResponse<IBranch>;
type EntityArrayResponseType = HttpResponse<IBranch[]>;

@Injectable({ providedIn: 'root' })
export class BranchService {
  public resourceUrl = SERVER_API_URL + 'api/branches';

  constructor(protected http: HttpClient) {}

  create(branch: IBranch): Observable<EntityResponseType> {
    return this.http.post<IBranch>(this.resourceUrl, branch, { observe: 'response' });
  }

  update(branch: IBranch): Observable<EntityResponseType> {
    return this.http.put<IBranch>(this.resourceUrl, branch, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IBranch>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IBranch[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
