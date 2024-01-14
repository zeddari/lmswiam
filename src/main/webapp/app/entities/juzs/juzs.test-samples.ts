import dayjs from 'dayjs/esm';

import { IJuzs, NewJuzs } from './juzs.model';

export const sampleWithRequiredData: IJuzs = {
  id: 6556,
};

export const sampleWithPartialData: IJuzs = {
  id: 25739,
};

export const sampleWithFullData: IJuzs = {
  id: 1943,
  createdAt: dayjs('2024-01-09T12:35'),
  updatedAt: dayjs('2024-01-08T22:41'),
};

export const sampleWithNewData: NewJuzs = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
