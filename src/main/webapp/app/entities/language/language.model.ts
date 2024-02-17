import { IUserCustom } from 'app/entities/user-custom/user-custom.model';

export interface ILanguage {
  id: number;
  nameAr?: string | null;
  nameLat?: string | null;
  code?: string | null;
  userCustom8s?: Pick<IUserCustom, 'id' | 'firstName'>[] | null;
}

export type NewLanguage = Omit<ILanguage, 'id'> & { id: null };
