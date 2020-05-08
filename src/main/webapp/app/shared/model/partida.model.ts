export interface IPartida {
  id?: number;
  mandanteID?: number;
  visitanteID?: number;
  golsVisitante?: number;
  golsMandante?: number;
  local?: string;
  campeonato?: number;
}

export class Partida implements IPartida {
  constructor(
    public id?: number,
    public mandanteID?: number,
    public visitanteID?: number,
    public golsVisitante?: number,
    public golsMandante?: number,
    public local?: string,
    public campeonato?: number
  ) {}
}
