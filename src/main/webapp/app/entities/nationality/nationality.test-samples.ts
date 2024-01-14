import { INationality, NewNationality } from './nationality.model';

export const sampleWithRequiredData: INationality = {
  id: 9964,
  nameAr: 'absent pish brew',
  nameLat: 'for near',
};

export const sampleWithPartialData: INationality = {
  id: 26650,
  nameAr: 'zowie celebrated',
  nameLat: 'so beside',
};

export const sampleWithFullData: INationality = {
  id: 18380,
  nameAr: 'bravely',
  nameLat: 'till',
};

export const sampleWithNewData: NewNationality = {
  nameAr: 'er chicken whoa',
  nameLat: 'ferret yowza wearily',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
