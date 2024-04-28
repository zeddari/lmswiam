import { IDepense, NewDepense } from './depense.model';

export const sampleWithRequiredData: IDepense = {
  id: 9275,
  type: 'INTERNET',
  target: 'LOYER',
  frequency: 'DAILY',
  amount: 13749.08,
  ref: 'whoa not weedkiller',
};

export const sampleWithPartialData: IDepense = {
  id: 32218,
  type: 'INTERNAL_ACTIVITY',
  target: 'REDAL',
  frequency: 'YEARLY',
  amount: 4945.48,
  ref: 'technologist healthily oh',
  message: 'upside-down alfalfa',
};

export const sampleWithFullData: IDepense = {
  id: 13295,
  type: 'REGISTER',
  target: 'TEACHER',
  frequency: 'MONTHLY',
  amount: 17688.4,
  ref: 'nor',
  message: 'coolly',
};

export const sampleWithNewData: NewDepense = {
  type: 'OTHERS',
  target: 'LOYER',
  frequency: 'YEARLY',
  amount: 29728.06,
  ref: 'gee',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
