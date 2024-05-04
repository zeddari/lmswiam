import { IUserCustom } from 'app/entities/user-custom/user-custom.model';

export interface IDepartement {
  id: number;
  nameAr?: string | null;
  nameLat?: string | null;
  description?: string | null;
  departements?: IDepartement[] | null;
  userCustoms?: IUserCustom[] | null;
  departement1?: IDepartement | null;
}

export type NewDepartement = Omit<IDepartement, 'id'> & { id: null };
