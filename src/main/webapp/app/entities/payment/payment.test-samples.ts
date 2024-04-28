import dayjs from 'dayjs/esm';

import { IPayment, NewPayment } from './payment.model';

export const sampleWithRequiredData: IPayment = {
  id: 24533,
  amount: 29087.73,
  paymentMethod: 'TRANSFER',
  paiedBy: 'vivaciously',
  paidAt: dayjs('2024-01-09T12:14'),
  type: 'KHATM_ACTIVITY',
  side: 'OUT',
  validityStartTime: dayjs('2024-01-09T10:50'),
  validityEndTime: dayjs('2024-01-09T01:16'),
};

export const sampleWithPartialData: IPayment = {
  id: 12549,
  amount: 27132.63,
  paymentMethod: 'CHECK',
  paiedBy: 'joyously',
  paidAt: dayjs('2024-01-09T13:10'),
  type: 'MONTHLY_FEES',
  side: 'IN',
  validityStartTime: dayjs('2024-01-09T15:05'),
  validityEndTime: dayjs('2024-01-09T04:23'),
  details: '../fake-data/blob/hipster.txt',
};

export const sampleWithFullData: IPayment = {
  id: 7194,
  amount: 30432.25,
  paymentMethod: 'OTHER',
  paiedBy: 'because failing extremely',
  proof: '../fake-data/blob/hipster.png',
  proofContentType: 'unknown',
  paidAt: dayjs('2024-01-09T11:52'),
  type: 'MONTHLY_FEES',
  side: 'IN',
  validityStartTime: dayjs('2024-01-08T21:26'),
  validityEndTime: dayjs('2024-01-09T15:16'),
  details: '../fake-data/blob/hipster.txt',
};

export const sampleWithNewData: NewPayment = {
  amount: 15074.41,
  paymentMethod: 'TRANSFER',
  paiedBy: 'at',
  paidAt: dayjs('2024-01-09T05:53'),
  type: 'SALARY',
  side: 'IN',
  validityStartTime: dayjs('2024-01-09T07:07'),
  validityEndTime: dayjs('2024-01-09T01:32'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
