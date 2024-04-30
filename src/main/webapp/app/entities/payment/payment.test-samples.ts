import dayjs from 'dayjs/esm';

import { IPayment, NewPayment } from './payment.model';

export const sampleWithRequiredData: IPayment = {
  id: 10713,
  amount: 24532.32,
  paymentMethod: 'OTHER',
  paiedBy: 'guest',
  paidAt: dayjs('2024-01-08T19:48'),
  type: 'REGISTER',
  validityStartTime: dayjs('2024-01-08T19:43'),
  validityEndTime: dayjs('2024-01-09T03:15'),
};

export const sampleWithPartialData: IPayment = {
  id: 30116,
  amount: 1303.18,
  paymentMethod: 'TRANSFER',
  paiedBy: 'towards modulo breakable',
  proof: '../fake-data/blob/hipster.png',
  proofContentType: 'unknown',
  paidAt: dayjs('2024-01-09T14:37'),
  type: 'REGISTER',
  validityStartTime: dayjs('2024-01-09T00:28'),
  validityEndTime: dayjs('2024-01-09T16:15'),
};

export const sampleWithFullData: IPayment = {
  id: 2802,
  amount: 9908.76,
  paymentMethod: 'TRANSFER',
  paiedBy: 'alert',
  proof: '../fake-data/blob/hipster.png',
  proofContentType: 'unknown',
  paidAt: dayjs('2024-01-08T21:09'),
  type: 'REGISTER',
  validityStartTime: dayjs('2024-01-09T12:58'),
  validityEndTime: dayjs('2024-01-09T11:52'),
  details: '../fake-data/blob/hipster.txt',
};

export const sampleWithNewData: NewPayment = {
  amount: 2806.53,
  paymentMethod: 'TRANSFER',
  paiedBy: 'wifi indeed given',
  paidAt: dayjs('2024-01-09T09:49'),
  type: 'ACTIVITY_FEES',
  validityStartTime: dayjs('2024-01-08T21:55'),
  validityEndTime: dayjs('2024-01-09T14:31'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
