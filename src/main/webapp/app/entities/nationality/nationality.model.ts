import { IUserCustom } from 'app/entities/user-custom/user-custom.model';

export interface INationality {
  id: number;
  nameAr?: string | null;
  nameLat?: string | null;
  userCustoms?: IUserCustom[] | null;
}

export type NewNationality = Omit<INationality, 'id'> & { id: null };
