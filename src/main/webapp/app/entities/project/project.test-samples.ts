import dayjs from 'dayjs/esm';

import { IProject, NewProject } from './project.model';

export const sampleWithRequiredData: IProject = {
  id: 16406,
  titleAr: 'pro',
  titleLat: 'zucchini the tile',
  budget: 7950.25,
};

export const sampleWithPartialData: IProject = {
  id: 30761,
  titleAr: 'conscientise a',
  titleLat: 'quickly than',
  description: '../fake-data/blob/hipster.txt',
  requirements: '../fake-data/blob/hipster.txt',
  imageLink: '../fake-data/blob/hipster.png',
  imageLinkContentType: 'unknown',
  videoLink: 'studious',
  budget: 30924.01,
  isActive: false,
  activateAt: dayjs('2024-01-09T06:21'),
  startDate: dayjs('2024-01-09'),
  endDate: dayjs('2024-01-09'),
};

export const sampleWithFullData: IProject = {
  id: 29956,
  titleAr: 'excepting',
  titleLat: 'before the',
  description: '../fake-data/blob/hipster.txt',
  goals: '../fake-data/blob/hipster.txt',
  requirements: '../fake-data/blob/hipster.txt',
  imageLink: '../fake-data/blob/hipster.png',
  imageLinkContentType: 'unknown',
  videoLink: 'down with',
  budget: 29946.31,
  isActive: false,
  activateAt: dayjs('2024-01-09T03:31'),
  startDate: dayjs('2024-01-08'),
  endDate: dayjs('2024-01-09'),
};

export const sampleWithNewData: NewProject = {
  titleAr: 'careful officially',
  titleLat: 'ew violently frightfully',
  budget: 10649.27,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
