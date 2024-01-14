import { ICertificate, NewCertificate } from './certificate.model';

export const sampleWithRequiredData: ICertificate = {
  id: 24436,
  title: 'yum thanks',
  miqdar: 10,
  observation: '../fake-data/blob/hipster.txt',
};

export const sampleWithPartialData: ICertificate = {
  id: 5894,
  certificateType: 'IJAZA',
  title: 'strong than predominate',
  miqdar: 49,
  observation: '../fake-data/blob/hipster.txt',
};

export const sampleWithFullData: ICertificate = {
  id: 15373,
  certificateType: 'HIFDH',
  title: 'given after consequently',
  riwaya: 'HISHAMS_NARRATION_ON_THE_AUTHORITY_OF_IBN_AMER_VIA_AL_SHATIBIYYAH',
  miqdar: 3,
  observation: '../fake-data/blob/hipster.txt',
};

export const sampleWithNewData: NewCertificate = {
  title: 'alternative ugh viciously',
  miqdar: 1,
  observation: '../fake-data/blob/hipster.txt',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
