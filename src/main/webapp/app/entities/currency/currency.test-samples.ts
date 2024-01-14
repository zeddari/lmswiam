import { ICurrency, NewCurrency } from './currency.model';

export const sampleWithRequiredData: ICurrency = {
  id: 24647,
  nameAr: 'import',
  nameLat: 'that furthermore certificate',
  code: 'retrench c',
};

export const sampleWithPartialData: ICurrency = {
  id: 3980,
  nameAr: 'inasmuch anti unimpressively',
  nameLat: 'lavish pfft coarsen',
  code: 'furthermor',
};

export const sampleWithFullData: ICurrency = {
  id: 22987,
  nameAr: 'woefully cook undermine',
  nameLat: 'silently frenetically',
  code: 'fix barrin',
};

export const sampleWithNewData: NewCurrency = {
  nameAr: 'modulo lovingly ick',
  nameLat: 'because besides after',
  code: 'astronomy',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
