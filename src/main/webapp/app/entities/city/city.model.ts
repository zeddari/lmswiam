export interface ICity {
  id: number;
  nameAr?: string | null;
  nameLat?: string | null;
}

export type NewCity = Omit<ICity, 'id'> & { id: null };
