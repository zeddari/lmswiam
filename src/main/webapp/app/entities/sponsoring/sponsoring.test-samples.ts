import dayjs from 'dayjs/esm';

import { ISponsoring, NewSponsoring } from './sponsoring.model';

export const sampleWithRequiredData: ISponsoring = {
  id: 19516,
  refKey: 'EXTERNAL_ACTIVITY',
  ref: 'skin yahoo',
  amount: 13165.01,
};

export const sampleWithPartialData: ISponsoring = {
  id: 19992,
  refKey: 'INTERNET',
  ref: 'basement polite',
  message: '../fake-data/blob/hipster.txt',
  amount: 6805.61,
  endDate: dayjs('2024-01-09'),
  isAlways: false,
};

export const sampleWithFullData: ISponsoring = {
  id: 9853,
  refKey: 'ELECTRICITY',
  ref: 'pro carefully biology',
  message: '../fake-data/blob/hipster.txt',
  amount: 3966.38,
  startDate: dayjs('2024-01-08'),
  endDate: dayjs('2024-01-09'),
  isAlways: false,
};

export const sampleWithNewData: NewSponsoring = {
  refKey: 'RENT',
  ref: 'safe',
  amount: 21946.36,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
