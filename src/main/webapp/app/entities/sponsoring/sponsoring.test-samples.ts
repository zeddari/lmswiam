import dayjs from 'dayjs/esm';

import { ISponsoring, NewSponsoring } from './sponsoring.model';

export const sampleWithRequiredData: ISponsoring = {
  id: 25850,
  ref: 'concuss for',
  amount: 16156.92,
};

export const sampleWithPartialData: ISponsoring = {
  id: 10599,
  ref: 'offer',
  message: '../fake-data/blob/hipster.txt',
  amount: 25132.44,
  startDate: dayjs('2024-01-09'),
  endDate: dayjs('2024-01-09'),
};

export const sampleWithFullData: ISponsoring = {
  id: 7694,
  ref: 'contraindicate screenwriting bravely',
  message: '../fake-data/blob/hipster.txt',
  amount: 996.65,
  startDate: dayjs('2024-01-08'),
  endDate: dayjs('2024-01-09'),
  isAlways: true,
};

export const sampleWithNewData: NewSponsoring = {
  ref: 'case which obedient',
  amount: 22974.71,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
