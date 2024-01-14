import dayjs from 'dayjs/esm';

import { IAyahs, NewAyahs } from './ayahs.model';

export const sampleWithRequiredData: IAyahs = {
  id: 4609,
  textdesc: 'rat crushing',
};

export const sampleWithPartialData: IAyahs = {
  id: 14165,
  number: 5764,
  textdesc: 'quizzically solidify',
  surahId: 22276,
  juzId: 20729,
  sajda: false,
  updatedAt: dayjs('2024-01-09T15:01'),
};

export const sampleWithFullData: IAyahs = {
  id: 27312,
  number: 9096,
  textdesc: 'klutzy notable rebellion',
  numberInSurah: 19563,
  page: 16504,
  surahId: 24764,
  hizbId: 21922,
  juzId: 27056,
  sajda: false,
  createdAt: dayjs('2024-01-09T11:05'),
  updatedAt: dayjs('2024-01-09T11:09'),
};

export const sampleWithNewData: NewAyahs = {
  textdesc: 'parliament drink meh',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
