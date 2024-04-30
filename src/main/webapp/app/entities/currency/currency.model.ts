export interface ICurrency {
  id: number;
  nameAr?: string | null;
  nameLat?: string | null;
  code?: string | null;
}

export type NewCurrency = Omit<ICurrency, 'id'> & { id: null };
