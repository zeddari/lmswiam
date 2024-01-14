import { IDepartement, NewDepartement } from './departement.model';

export const sampleWithRequiredData: IDepartement = {
  id: 17148,
  nameAr: 'anger eek',
  nameLat: 'zowie mid silver',
};

export const sampleWithPartialData: IDepartement = {
  id: 7282,
  nameAr: 'platform snappy oh',
  nameLat: 'drat',
};

export const sampleWithFullData: IDepartement = {
  id: 15490,
  nameAr: 'regarding atop',
  nameLat: 'hidden',
  description: '../fake-data/blob/hipster.txt',
};

export const sampleWithNewData: NewDepartement = {
  nameAr: 'geez irritably elated',
  nameLat: 'plus gee',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
