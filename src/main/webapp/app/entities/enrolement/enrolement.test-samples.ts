import dayjs from 'dayjs/esm';

import { IEnrolement, NewEnrolement } from './enrolement.model';

export const sampleWithRequiredData: IEnrolement = {
  id: 30288,
  ref: 'perfectly initiative',
  enrolmentStartTime: dayjs('2024-01-09T04:55'),
  enrolemntEndTime: dayjs('2024-01-09T12:00'),
  isActive: true,
};

export const sampleWithPartialData: IEnrolement = {
  id: 17358,
  ref: 'freely yowza while',
  enrolmentStartTime: dayjs('2024-01-09T12:00'),
  enrolemntEndTime: dayjs('2024-01-09T01:48'),
  isActive: true,
};

export const sampleWithFullData: IEnrolement = {
  id: 3845,
  ref: 'around screw near',
  enrolementType: 'ONELIFE',
  enrolmentStartTime: dayjs('2024-01-09T04:42'),
  enrolemntEndTime: dayjs('2024-01-09T02:29'),
  isActive: false,
  activatedAt: dayjs('2024-01-09T12:30'),
};

export const sampleWithNewData: NewEnrolement = {
  ref: 'however phew irritate',
  enrolmentStartTime: dayjs('2024-01-09T15:08'),
  enrolemntEndTime: dayjs('2024-01-08T21:52'),
  isActive: false,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
