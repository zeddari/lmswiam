import dayjs from 'dayjs/esm';

import { IAyahs, NewAyahs } from './ayahs.model';

export const sampleWithRequiredData: IAyahs = {
  id: 4609,
  textdesc: '../fake-data/blob/hipster.txt',
};

export const sampleWithPartialData: IAyahs = {
  id: 19123,
  number: 28942,
  textdesc: '../fake-data/blob/hipster.txt',
  numberInSurah: 20542,
  hizbId: 31527,
  sajda: false,
  updatedAt: dayjs('2024-01-09T14:31'),
};

export const sampleWithFullData: IAyahs = {
  id: 1778,
  number: 22404,
  textdesc: '../fake-data/blob/hipster.txt',
  numberInSurah: 20894,
  page: 33,
  surahId: 29494,
  hizbId: 14590,
  juzId: 7260,
  sajda: false,
  createdAt: dayjs('2024-01-09T08:48'),
  updatedAt: dayjs('2024-01-09T07:56'),
};

export const sampleWithNewData: NewAyahs = {
  textdesc: '../fake-data/blob/hipster.txt',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
