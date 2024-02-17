import dayjs from 'dayjs/esm';

import { ICourse, NewCourse } from './course.model';

export const sampleWithRequiredData: ICourse = {
  id: 13022,
  level: 'ADVANCED',
  titleAr: 'past before',
  titleLat: 'anti',
};

export const sampleWithPartialData: ICourse = {
  id: 15227,
  level: 'EXPERT',
  titleAr: 'enforce adored',
  titleLat: 'worthwhile',
  description: '../fake-data/blob/hipster.txt',
  requirements: '../fake-data/blob/hipster.txt',
  imageLink: '../fake-data/blob/hipster.png',
  imageLinkContentType: 'unknown',
  videoLink: '../fake-data/blob/hipster.txt',
  activateAt: dayjs('2024-01-09T11:19'),
};

export const sampleWithFullData: ICourse = {
  id: 22252,
  level: 'INTERMEDIATE',
  titleAr: 'affect lazily',
  titleLat: 'humanise',
  description: '../fake-data/blob/hipster.txt',
  subTitles: '../fake-data/blob/hipster.txt',
  requirements: '../fake-data/blob/hipster.txt',
  options: '../fake-data/blob/hipster.txt',
  duration: 31895,
  imageLink: '../fake-data/blob/hipster.png',
  imageLinkContentType: 'unknown',
  videoLink: '../fake-data/blob/hipster.txt',
  price: 27371.99,
  isActive: false,
  activateAt: dayjs('2024-01-08T20:17'),
  isConfirmed: true,
  confirmedAt: dayjs('2024-01-09T17:31'),
};

export const sampleWithNewData: NewCourse = {
  level: 'ADVANCED',
  titleAr: 'reconstruct',
  titleLat: 'thaw',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
