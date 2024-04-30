import { ICity } from 'app/entities/city/city.model';

export interface ISite {
  id: number;
  nameAr?: string | null;
  nameLat?: string | null;
  description?: string | null;
  localisation?: string | null;
  city?: Pick<ICity, 'id' | 'nameAr'> | null;
}

export type NewSite = Omit<ISite, 'id'> & { id: null };
