import { ISessionLink, NewSessionLink } from './session-link.model';

export const sampleWithRequiredData: ISessionLink = {
  id: 1919,
  title: 'including nanoparticle hideous',
};

export const sampleWithPartialData: ISessionLink = {
  id: 30130,
  title: 'usefully bah lionise',
  link: 'wide rightfully',
};

export const sampleWithFullData: ISessionLink = {
  id: 14781,
  provider: 'TELEGRAM',
  title: 'granular um',
  description: '../fake-data/blob/hipster.txt',
  link: 'chauvinist incidentally',
};

export const sampleWithNewData: NewSessionLink = {
  title: 'our behind',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
