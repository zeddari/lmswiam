import { ICountry, NewCountry } from './country.model';

export const sampleWithRequiredData: ICountry = {
  id: 20601,
  nameAr: 'neat',
  nameLat: 'fuzzy bogus',
};

export const sampleWithPartialData: ICountry = {
  id: 6768,
  nameAr: 'condominium',
  nameLat: 'whimsical by failing',
};

export const sampleWithFullData: ICountry = {
  id: 11906,
  nameAr: 'consequently',
  nameLat: 'viciously dreamily',
  code: 'less disbe',
};

export const sampleWithNewData: NewCountry = {
  nameAr: 'mull',
  nameLat: 'amid selfishly besides',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
