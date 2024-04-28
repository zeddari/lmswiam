import dayjs from 'dayjs/esm';

import { IUserCustom, NewUserCustom } from './user-custom.model';

export const sampleWithRequiredData: IUserCustom = {
  id: 24405,
  firstName: 'Kenna',
  lastName: 'Bernhard',
  accountName: 'Checking Account',
  role: 'MANAGEMENT',
  accountStatus: 'ACTIVATED',
  phoneNumber1: 'lid',
  sex: 'MALE',
  birthdate: dayjs('2024-01-09'),
};

export const sampleWithPartialData: IUserCustom = {
  id: 24393,
  firstName: 'Sidney',
  lastName: 'Beatty',
  code: 'dock brr intensely',
  accountName: 'Home Loan Account',
  role: 'SUPER_MANAGER',
  accountStatus: 'BLOCKED',
  phoneNumber1: 'jail aw taco',
  phoneNumver2: 'unto',
  sex: 'FEMALE',
  birthdate: dayjs('2024-01-09'),
  photo: '../fake-data/blob/hipster.png',
  photoContentType: 'unknown',
  telegramUserCustomId: 'supreme down',
  telegramUserCustomName: 'well-made',
  biography: '../fake-data/blob/hipster.txt',
};

export const sampleWithFullData: IUserCustom = {
  id: 14550,
  firstName: 'Kallie',
  lastName: 'Daugherty',
  code: 'where blushing',
  accountName: 'Auto Loan Account',
  role: 'MANAGER',
  accountStatus: 'BLOCKED',
  phoneNumber1: 'geez till daily',
  phoneNumver2: 'fatally',
  sex: 'FEMALE',
  birthdate: dayjs('2024-01-08'),
  photo: '../fake-data/blob/hipster.png',
  photoContentType: 'unknown',
  address: '../fake-data/blob/hipster.txt',
  facebook: 'since lionise amongst',
  telegramUserCustomId: 'hollow macrame',
  telegramUserCustomName: 'likewise tedious despite',
  biography: '../fake-data/blob/hipster.txt',
  bankAccountDetails: '../fake-data/blob/hipster.txt',
};

export const sampleWithNewData: NewUserCustom = {
  firstName: 'Clovis',
  lastName: 'Zieme',
  accountName: 'Checking Account',
  role: 'SPONSOR',
  accountStatus: 'BLOCKED',
  phoneNumber1: 'gee',
  sex: 'MALE',
  birthdate: dayjs('2024-01-09'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
