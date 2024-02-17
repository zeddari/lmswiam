import { ISite, NewSite } from './site.model';

export const sampleWithRequiredData: ISite = {
  id: 22911,
  nameAr: 'mortally mochi',
  nameLat: 'cavernous',
};

export const sampleWithPartialData: ISite = {
  id: 4488,
  nameAr: 'lob detrain',
  nameLat: 'pfft intently',
};

export const sampleWithFullData: ISite = {
  id: 14992,
  nameAr: 'mortally',
  nameLat: 'micronutrient conspirator',
  description: '../fake-data/blob/hipster.txt',
  localisation: 'badly gracefully',
};

export const sampleWithNewData: NewSite = {
  nameAr: 'geez',
  nameLat: 'thankfully vivaciously',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
