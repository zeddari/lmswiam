import dayjs from 'dayjs/esm';

import { ISurahs, NewSurahs } from './surahs.model';

export const sampleWithRequiredData: ISurahs = {
  id: 25883,
};

export const sampleWithPartialData: ISurahs = {
  id: 28537,
  number: 12265,
  type: '../fake-data/blob/hipster.txt',
  createdAt: dayjs('2024-01-09T16:35'),
  updatedAt: dayjs('2024-01-09T09:59'),
};

export const sampleWithFullData: ISurahs = {
  id: 15528,
  number: 215,
  nameAr: '../fake-data/blob/hipster.txt',
  nameEn: '../fake-data/blob/hipster.txt',
  nameEnTranslation: '../fake-data/blob/hipster.txt',
  type: '../fake-data/blob/hipster.txt',
  createdAt: dayjs('2024-01-09T03:01'),
  updatedAt: dayjs('2024-01-08T20:42'),
};

export const sampleWithNewData: NewSurahs = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
