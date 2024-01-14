import { ILanguage, NewLanguage } from './language.model';

export const sampleWithRequiredData: ILanguage = {
  id: 18624,
  nameAr: 'phooey inasmuch hopelessly',
  nameLat: 'hefty remedy hm',
};

export const sampleWithPartialData: ILanguage = {
  id: 12841,
  nameAr: 'present',
  nameLat: 'dictate commonly',
  code: 'stamp besi',
};

export const sampleWithFullData: ILanguage = {
  id: 32017,
  nameAr: 'asphalt trim adept',
  nameLat: 'successfully inasmuch',
  code: 'planet',
};

export const sampleWithNewData: NewLanguage = {
  nameAr: 'throughout round',
  nameLat: 'toy including',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
