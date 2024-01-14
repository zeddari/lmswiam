import dayjs from 'dayjs/esm';

import { IHizbs, NewHizbs } from './hizbs.model';

export const sampleWithRequiredData: IHizbs = {
  id: 25256,
};

export const sampleWithPartialData: IHizbs = {
  id: 180,
  createdAt: dayjs('2024-01-09T09:27'),
  updatedAt: dayjs('2024-01-09T15:03'),
};

export const sampleWithFullData: IHizbs = {
  id: 5079,
  createdAt: dayjs('2024-01-08T19:22'),
  updatedAt: dayjs('2024-01-08T21:47'),
};

export const sampleWithNewData: NewHizbs = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
