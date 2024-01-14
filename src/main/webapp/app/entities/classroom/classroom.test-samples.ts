import { IClassroom, NewClassroom } from './classroom.model';

export const sampleWithRequiredData: IClassroom = {
  id: 23147,
  nameAr: 'trailpatrol while',
  nameLat: 'prosecute',
};

export const sampleWithPartialData: IClassroom = {
  id: 30920,
  nameAr: 'excluding athwart',
  nameLat: 'honey',
  description: '../fake-data/blob/hipster.txt',
};

export const sampleWithFullData: IClassroom = {
  id: 2044,
  nameAr: 'marker anxiously shameful',
  nameLat: 'if instead rarely',
  description: '../fake-data/blob/hipster.txt',
};

export const sampleWithNewData: NewClassroom = {
  nameAr: 'than meager illumine',
  nameLat: 'fooey stall',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
