import { ISite, NewSite } from './site.model';

export const sampleWithRequiredData: ISite = {
  id: 2071,
  nameAr: 'decent',
  nameLat: 'dislodge moody',
};

export const sampleWithPartialData: ISite = {
  id: 29833,
  nameAr: 'or mortally',
  nameLat: 'anenst cavernous',
  localisation: 'uh-huh ha including',
};

export const sampleWithFullData: ISite = {
  id: 10790,
  nameAr: 'duh',
  nameLat: 'aw',
  description: '../fake-data/blob/hipster.txt',
  localisation: 'forecast',
};

export const sampleWithNewData: NewSite = {
  nameAr: 'nestling',
  nameLat: 'because if',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
