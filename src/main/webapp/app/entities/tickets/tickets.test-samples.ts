import dayjs from 'dayjs/esm';

import { ITickets, NewTickets } from './tickets.model';

export const sampleWithRequiredData: ITickets = {
  id: 16591,
  subject: 'INFORMATION',
  title: 'because yahoo foolishly',
  reference: 'get commercialise',
  dateTicket: dayjs('2024-01-09T00:13'),
  processed: 'PROCESSING',
};

export const sampleWithPartialData: ITickets = {
  id: 18587,
  subject: 'LEAVE',
  title: 'inside brr yowza',
  reference: 'partially off gah',
  description: '../fake-data/blob/hipster.txt',
  justifDoc: '../fake-data/blob/hipster.png',
  justifDocContentType: 'unknown',
  dateTicket: dayjs('2024-01-08T23:46'),
  processed: 'PROCESSED',
  toDate: dayjs('2024-01-09T03:51'),
};

export const sampleWithFullData: ITickets = {
  id: 18380,
  subject: 'LEAVE',
  title: 'granular',
  reference: 'regarding for van',
  description: '../fake-data/blob/hipster.txt',
  justifDoc: '../fake-data/blob/hipster.png',
  justifDocContentType: 'unknown',
  dateTicket: dayjs('2024-01-09T17:07'),
  dateProcess: dayjs('2024-01-08T23:19'),
  processed: 'PROCESSING',
  from: dayjs('2024-01-09T02:16'),
  toDate: dayjs('2024-01-09T02:37'),
  decisionDetail: '../fake-data/blob/hipster.txt',
};

export const sampleWithNewData: NewTickets = {
  subject: 'INFORMATION',
  title: 'astonishing ew',
  reference: 'whoever venom',
  dateTicket: dayjs('2024-01-09T16:46'),
  processed: 'PENDING',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
