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
  videoLink: '../fake-data/blob/hipster.txt',
  budget: 3027.73,
  isActive: false,
  activateAt: dayjs('2024-01-09T10:54'),
  startDate: dayjs('2024-01-09'),
  endDate: dayjs('2024-01-09'),
};

export const sampleWithFullData: IProject = {
  id: 29490,
  titleAr: 'usable wetly',
  titleLat: 'hopelessly following',
  description: '../fake-data/blob/hipster.txt',
  goals: '../fake-data/blob/hipster.txt',
  requirements: '../fake-data/blob/hipster.txt',
  imageLink: '../fake-data/blob/hipster.png',
  imageLinkContentType: 'unknown',
  videoLink: '../fake-data/blob/hipster.txt',
  budget: 26388.16,
  isActive: false,
  activateAt: dayjs('2024-01-08T18:22'),
  startDate: dayjs('2024-01-09'),
  endDate: dayjs('2024-01-09'),
};

export const sampleWithNewData: NewProject = {
  titleAr: 'hm',
  titleLat: 'male sore',
  budget: 3566.43,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
