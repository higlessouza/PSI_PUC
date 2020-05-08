import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption, Search } from 'app/shared/util/request-util';
import { IPartida } from 'app/shared/model/partida.model';

type EntityResponseType = HttpResponse<IPartida>;
type EntityArrayResponseType = HttpResponse<IPartida[]>;

@Injectable({ providedIn: 'root' })
export class PartidaService {
  public resourceUrl = SERVER_API_URL + 'api/partidas';
  public resourceSearchUrl = SERVER_API_URL + 'api/_search/partidas';

  constructor(protected http: HttpClient) {}

  create(partida: IPartida): Observable<EntityResponseType> {
    return this.http.post<IPartida>(this.resourceUrl, partida, { observe: 'response' });
  }

  update(partida: IPartida): Observable<EntityResponseType> {
    return this.http.put<IPartida>(this.resourceUrl, partida, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPartida>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPartida[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: Search): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPartida[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }
}
