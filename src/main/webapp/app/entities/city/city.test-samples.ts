import { ICity, NewCity } from './city.model';

export const sampleWithRequiredData: ICity = {
  id: 3923,
  nameAr: 'devise',
  nameLat: 'whoa uneven unlike',
};

export const sampleWithPartialData: ICity = {
  id: 21287,
  nameAr: 'meh fibroblast since',
  nameLat: 'along gleeful knavishly',
};

export const sampleWithFullData: ICity = {
  id: 12313,
  nameAr: 'really boo likewise',
  nameLat: 'riot spending',
};

export const sampleWithNewData: NewCity = {
  nameAr: 'smuggling seemingly mature',
  nameLat: 'lather vivacious',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
