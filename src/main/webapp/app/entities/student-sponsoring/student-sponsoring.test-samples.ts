import dayjs from 'dayjs/esm';

import { IStudentSponsoring, NewStudentSponsoring } from './student-sponsoring.model';

export const sampleWithRequiredData: IStudentSponsoring = {
  id: 31824,
  ref: 'mmm apropos merrily',
  amount: 2080.05,
};

export const sampleWithPartialData: IStudentSponsoring = {
  id: 1153,
  ref: 'palatable astride',
  amount: 22722.21,
  isAlways: false,
};

export const sampleWithFullData: IStudentSponsoring = {
  id: 1291,
  ref: 'overconfidently hastily',
  message: '../fake-data/blob/hipster.txt',
  amount: 14226.76,
  startDate: dayjs('2024-01-09'),
  endDate: dayjs('2024-01-09'),
  isAlways: true,
};

export const sampleWithNewData: NewStudentSponsoring = {
  ref: 'reorganise orient',
  amount: 6076.16,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
