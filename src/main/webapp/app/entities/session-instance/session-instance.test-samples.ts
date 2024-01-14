import dayjs from 'dayjs/esm';

import { ISessionInstance, NewSessionInstance } from './session-instance.model';

export const sampleWithRequiredData: ISessionInstance = {
  id: 6261,
  title: 'until ideal uncomfortable',
  sessionDate: dayjs('2024-01-09'),
  startTime: dayjs('2024-01-09T16:00'),
  duration: 10656,
  attendance: 'ABSENT',
  isActive: false,
};

export const sampleWithPartialData: ISessionInstance = {
  id: 15078,
  title: 'repeat hard-to-find lest',
  sessionDate: dayjs('2024-01-09'),
  startTime: dayjs('2024-01-09T10:27'),
  duration: 3762,
  attendance: 'PRESENT',
  justifRef: 'thoroughly unimpressively whoever',
  isActive: false,
};

export const sampleWithFullData: ISessionInstance = {
  id: 21124,
  title: 'million',
  sessionDate: dayjs('2024-01-09'),
  startTime: dayjs('2024-01-09T11:01'),
  duration: 29998,
  info: '../fake-data/blob/hipster.txt',
  attendance: 'PRESENT',
  justifRef: 'gracefully furnish sweetly',
  isActive: false,
};

export const sampleWithNewData: NewSessionInstance = {
  title: 'clam excepting',
  sessionDate: dayjs('2024-01-08'),
  startTime: dayjs('2024-01-09T08:03'),
  duration: 32052,
  attendance: 'ABSENT',
  isActive: true,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
