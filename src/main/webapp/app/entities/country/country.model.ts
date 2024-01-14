import { IUserCustom } from 'app/entities/user-custom/user-custom.model';

export interface ICountry {
  id: number;
  nameAr?: string | null;
  nameLat?: string | null;
  code?: string | null;
  userCustoms?: IUserCustom[] | null;
}

export type NewCountry = Omit<ICountry, 'id'> & { id: null };
