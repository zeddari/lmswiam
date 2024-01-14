import dayjs from 'dayjs/esm';

import { ISession, NewSession } from './session.model';

export const sampleWithRequiredData: ISession = {
  id: 4584,
  sessionMode: 'MIXED',
  sessionType: 'TRAINING',
  sessionJoinMode: 'DIRECT',
  title: 'about',
  sessionStartTime: dayjs('2024-01-09T07:33'),
  sessionEndTime: dayjs('2024-01-08T18:46'),
  sessionSize: 10,
  targetedGender: 'WOMEN',
  isPeriodic: false,
  isMinorAllowed: true,
  isActive: true,
};

export const sampleWithPartialData: ISession = {
  id: 4474,
  sessionMode: 'ONLINE',
  sessionType: 'HALAQA',
  sessionJoinMode: 'AUTHORIZED',
  title: 'collaborate and',
  periodeEndDate: dayjs('2024-01-09'),
  sessionStartTime: dayjs('2024-01-09T01:31'),
  sessionEndTime: dayjs('2024-01-09T09:51'),
  sessionSize: 99,
  targetedGender: 'WOMEN',
  thumbnail: '../fake-data/blob/hipster.png',
  thumbnailContentType: 'unknown',
  monday: true,
  saturday: false,
  isPeriodic: true,
  isMinorAllowed: true,
  isActive: true,
};

export const sampleWithFullData: ISession = {
  id: 20865,
  sessionMode: 'ONLINE',
  sessionType: 'HALAQA',
  sessionJoinMode: 'DIRECT',
  title: 'thankfully',
  description: '../fake-data/blob/hipster.txt',
  periodStartDate: dayjs('2024-01-09'),
  periodeEndDate: dayjs('2024-01-08'),
  sessionStartTime: dayjs('2024-01-09T15:15'),
  sessionEndTime: dayjs('2024-01-08T18:28'),
  sessionSize: 34,
  targetedGender: 'MEN',
  price: 2988.79,
  thumbnail: '../fake-data/blob/hipster.png',
  thumbnailContentType: 'unknown',
  monday: false,
  tuesday: false,
  wednesday: true,
  thursday: true,
  friday: false,
  saturday: false,
  sunday: false,
  isPeriodic: false,
  isMinorAllowed: false,
  isActive: true,
};

export const sampleWithNewData: NewSession = {
  sessionMode: 'ONSITE',
  sessionType: 'HALAQA',
  sessionJoinMode: 'DIRECT',
  title: 'alongside exotic',
  sessionStartTime: dayjs('2024-01-08T23:31'),
  sessionEndTime: dayjs('2024-01-09T13:50'),
  sessionSize: 97,
  targetedGender: 'WOMEN',
  isPeriodic: false,
  isMinorAllowed: true,
  isActive: false,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
