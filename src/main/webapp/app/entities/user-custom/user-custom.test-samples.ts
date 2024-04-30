import dayjs from 'dayjs/esm';

import { IUserCustom, NewUserCustom } from './user-custom.model';

export const sampleWithRequiredData: IUserCustom = {
  id: 20725,
  firstName: 'Kory',
  lastName: 'Rodriguez',
  accountName: 'Home Loan Account',
  role: 'MANAGEMENT',
  accountStatus: 'ACTIVATED',
  phoneNumber1: 'drat',
  sex: 'FEMALE',
  birthdate: dayjs('2024-01-08'),
};

export const sampleWithPartialData: IUserCustom = {
  id: 13979,
  firstName: 'Hal',
  lastName: 'Rodriguez-Towne',
  accountName: 'Investment Account',
  role: 'STUDENT',
  accountStatus: 'DELETED',
  phoneNumber1: 'kindheartedly wisely',
  phoneNumver2: 'than seemingly',
  sex: 'MALE',
  birthdate: dayjs('2024-01-09'),
  photo: '../fake-data/blob/hipster.png',
  photoContentType: 'unknown',
  address: '../fake-data/blob/hipster.txt',
  facebook: 'barring fake unto',
  telegramUserCustomId: 'elegantly because',
  bankAccountDetails: '../fake-data/blob/hipster.txt',
};

export const sampleWithFullData: IUserCustom = {
  id: 30381,
  firstName: 'Grace',
  lastName: 'Tillman',
  code: 'oddly',
  accountName: 'Credit Card Account',
  role: 'INSTRUCTOR',
  accountStatus: 'WAITING_ADMIN',
  phoneNumber1: 'personify',
  phoneNumver2: 'till meanwhile adventurous',
  sex: 'MALE',
  birthdate: dayjs('2024-01-08'),
  photo: '../fake-data/blob/hipster.png',
  photoContentType: 'unknown',
  address: '../fake-data/blob/hipster.txt',
  facebook: 'certainly fatally',
  telegramUserCustomId: 'of amongst instead',
  telegramUserCustomName: 'high',
  biography: '../fake-data/blob/hipster.txt',
  bankAccountDetails: '../fake-data/blob/hipster.txt',
};

export const sampleWithNewData: NewUserCustom = {
  firstName: 'Terry',
  lastName: 'Franecki',
  accountName: 'Checking Account',
  role: 'INSTRUCTOR',
  accountStatus: 'BLOCKED',
  phoneNumber1: 'kindheartedly yahoo when',
  sex: 'FEMALE',
  birthdate: dayjs('2024-01-09'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
