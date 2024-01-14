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
  videoLink: 'uh-huh',
  activateAt: dayjs('2024-01-09T08:09'),
};

export const sampleWithFullData: ICourse = {
  id: 30927,
  level: 'BEGINNER',
  titleAr: 'criticism humanise since',
  titleLat: 'reconstruct',
  description: '../fake-data/blob/hipster.txt',
  subTitles: '../fake-data/blob/hipster.txt',
  requirements: '../fake-data/blob/hipster.txt',
  options: '../fake-data/blob/hipster.txt',
  duration: 2437,
  imageLink: '../fake-data/blob/hipster.png',
  imageLinkContentType: 'unknown',
  videoLink: 'discrete',
  price: 1712.29,
  isActive: true,
  activateAt: dayjs('2024-01-09T00:47'),
  isConfirmed: false,
  confirmedAt: dayjs('2024-01-09T10:48'),
};

export const sampleWithNewData: NewCourse = {
  level: 'ADVANCED',
  titleAr: 'positively',
  titleLat: 'humongous but',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
