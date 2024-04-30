import dayjs from 'dayjs/esm';

import { ISession, NewSession } from './session.model';

export const sampleWithRequiredData: ISession = {
  id: 5396,
  sessionMode: 'ONLINE',
  sessionType: 'LECTURE',
  sessionJoinMode: 'DIRECT',
  title: 'alongside',
  sessionStartTime: dayjs('2024-01-09T07:43'),
  sessionEndTime: dayjs('2024-01-09T05:21'),
  sessionSize: 8,
  targetedGender: 'MEN',
  isPeriodic: false,
  isMinorAllowed: false,
  isActive: true,
};

export const sampleWithPartialData: ISession = {
  id: 22619,
  sessionMode: 'ONSITE',
  sessionType: 'LECTURE',
  sessionJoinMode: 'AUTHORIZED',
  title: 'slink but',
  periodStartDate: dayjs('2024-01-08'),
  periodeEndDate: dayjs('2024-01-09'),
  sessionStartTime: dayjs('2024-01-09T08:37'),
  sessionEndTime: dayjs('2024-01-08T23:31'),
  sessionSize: 41,
  targetedGender: 'MEN',
  wednesday: true,
  friday: false,
  saturday: true,
  sunday: true,
  isPeriodic: true,
  isMinorAllowed: true,
  isActive: false,
};

export const sampleWithFullData: ISession = {
  id: 24288,
  sessionMode: 'MIXED',
  sessionType: 'TRAINING',
  sessionJoinMode: 'DIRECT',
  title: 'highly when',
  description: '../fake-data/blob/hipster.txt',
  periodStartDate: dayjs('2024-01-08'),
  periodeEndDate: dayjs('2024-01-08'),
  sessionStartTime: dayjs('2024-01-09T05:39'),
  sessionEndTime: dayjs('2024-01-09T11:02'),
  sessionSize: 56,
  targetedGender: 'MEN',
  price: 114.78,
  thumbnail: '../fake-data/blob/hipster.png',
  thumbnailContentType: 'unknown',
  monday: false,
  tuesday: false,
  wednesday: true,
  thursday: false,
  friday: false,
  saturday: false,
  sunday: true,
  isPeriodic: true,
  isMinorAllowed: true,
  isActive: false,
};

export const sampleWithNewData: NewSession = {
  sessionMode: 'ONSITE',
  sessionType: 'TRAINING',
  sessionJoinMode: 'DIRECT',
  title: 'beneath qua boo',
  sessionStartTime: dayjs('2024-01-08T23:02'),
  sessionEndTime: dayjs('2024-01-08T20:30'),
  sessionSize: 55,
  targetedGender: 'WOMEN',
  isPeriodic: false,
  isMinorAllowed: false,
  isActive: true,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
