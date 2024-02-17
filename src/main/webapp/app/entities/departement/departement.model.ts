export interface IDepartement {
  id: number;
  nameAr?: string | null;
  nameLat?: string | null;
  description?: string | null;
  departement1?: Pick<IDepartement, 'id' | 'nameAr'> | null;
}

export type NewDepartement = Omit<IDepartement, 'id'> & { id: null };
