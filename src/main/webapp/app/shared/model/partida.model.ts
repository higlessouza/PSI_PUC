export interface IPartida {
  id?: number;
  mandanteID?: number;
  visitanteID?: number;
  mandanteGols?: number;
  visitanteGols?: number;
  local?: string;
  campeonato?: number;
}

export class Partida implements IPartida {
  constructor(
    public id?: number,
    public mandanteID?: number,
    public visitanteID?: number,
    public mandanteGols?: number,
    public visitanteGols?: number,
    public local?: string,
    public campeonato?: number
  ) {}
}
