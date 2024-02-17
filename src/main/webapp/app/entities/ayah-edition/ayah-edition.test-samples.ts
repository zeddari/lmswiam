import dayjs from 'dayjs/esm';

import { IAyahEdition, NewAyahEdition } from './ayah-edition.model';

export const sampleWithRequiredData: IAyahEdition = {
  id: 22851,
  data: '../fake-data/blob/hipster.txt',
};

export const sampleWithPartialData: IAyahEdition = {
  id: 17101,
  ayahId: 19805,
  data: '../fake-data/blob/hipster.txt',
  isAudio: true,
  updatedAt: dayjs('2024-01-09T01:43'),
};

export const sampleWithFullData: IAyahEdition = {
  id: 32626,
  ayahId: 14853,
  editionId: 30714,
  data: '../fake-data/blob/hipster.txt',
  isAudio: false,
  createdAt: dayjs('2024-01-09T04:42'),
  updatedAt: dayjs('2024-01-09T06:02'),
};

export const sampleWithNewData: NewAyahEdition = {
  data: '../fake-data/blob/hipster.txt',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
