import dayjs from 'dayjs/esm';

import { IEditions, NewEditions } from './editions.model';

export const sampleWithRequiredData: IEditions = {
  id: 18866,
};

export const sampleWithPartialData: IEditions = {
  id: 22892,
  language: '../fake-data/blob/hipster.txt',
  englishName: '../fake-data/blob/hipster.txt',
  createdAt: dayjs('2024-01-09T01:35'),
  updatedAt: dayjs('2024-01-09T01:52'),
};

export const sampleWithFullData: IEditions = {
  id: 25544,
  identifier: '../fake-data/blob/hipster.txt',
  language: '../fake-data/blob/hipster.txt',
  name: '../fake-data/blob/hipster.txt',
  englishName: '../fake-data/blob/hipster.txt',
  format: '../fake-data/blob/hipster.txt',
  type: '../fake-data/blob/hipster.txt',
  createdAt: dayjs('2024-01-08T18:58'),
  updatedAt: dayjs('2024-01-09T04:06'),
};

export const sampleWithNewData: NewEditions = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
