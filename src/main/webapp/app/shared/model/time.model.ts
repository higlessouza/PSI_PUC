export interface ITime {
  id?: number;
  nome?: string;
  escudo?: string;
}

export class Time implements ITime {
  constructor(public id?: number, public nome?: string, public escudo?: string) {}
}
