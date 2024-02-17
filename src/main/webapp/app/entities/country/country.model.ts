export interface ICountry {
  id: number;
  nameAr?: string | null;
  nameLat?: string | null;
  code?: string | null;
}

export type NewCountry = Omit<ICountry, 'id'> & { id: null };
