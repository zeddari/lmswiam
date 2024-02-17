export interface INationality {
  id: number;
  nameAr?: string | null;
  nameLat?: string | null;
}

export type NewNationality = Omit<INationality, 'id'> & { id: null };
