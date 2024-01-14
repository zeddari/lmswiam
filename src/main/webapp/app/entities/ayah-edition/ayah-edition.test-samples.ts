import dayjs from 'dayjs/esm';

import { IAyahEdition, NewAyahEdition } from './ayah-edition.model';

export const sampleWithRequiredData: IAyahEdition = {
  id: 22851,
  data: 'brief majestic',
};

export const sampleWithPartialData: IAyahEdition = {
  id: 12930,
  editionId: 1120,
  data: 'sleuth but',
  createdAt: dayjs('2024-01-09T13:18'),
};

export const sampleWithFullData: IAyahEdition = {
  id: 21848,
  ayahId: 30770,
  editionId: 7218,
  data: 'blah',
  isAudio: false,
  createdAt: dayjs('2024-01-09T13:08'),
  updatedAt: dayjs('2024-01-08T21:58'),
};

export const sampleWithNewData: NewAyahEdition = {
  data: 'convert competent boo',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
