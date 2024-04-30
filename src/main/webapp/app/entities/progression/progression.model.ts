import { ISite } from 'app/entities/site/site.model';
import { ISessionInstance } from 'app/entities/session-instance/session-instance.model';
import { IUserCustom } from 'app/entities/user-custom/user-custom.model';
import { Attendance } from 'app/entities/enumerations/attendance.model';
import { ProgressionMode } from 'app/entities/enumerations/progression-mode.model';
import { ExamType } from 'app/entities/enumerations/exam-type.model';
import { Riwayats } from 'app/entities/enumerations/riwayats.model';
import { Sourate } from 'app/entities/enumerations/sourate.model';
import { Tilawa } from 'app/entities/enumerations/tilawa.model';

export interface IProgression {
  id: number;
  attendance?: keyof typeof Attendance | null;
  justifRef?: string | null;
  lateArrival?: boolean | null;
  earlyDeparture?: boolean | null;
  progressionMode?: keyof typeof ProgressionMode | null;
  examType?: keyof typeof ExamType | null;
  riwaya?: keyof typeof Riwayats | null;
  fromSourate?: keyof typeof Sourate | null;
  toSourate?: keyof typeof Sourate | null;
  fromAyaNum?: number | null;
  toAyaNum?: number | null;
  fromAyaVerset?: string | null;
  toAyaVerset?: string | null;
  tilawaType?: keyof typeof Tilawa | null;
  taskDone?: boolean | null;
  tajweedScore?: number | null;
  hifdScore?: number | null;
  adaeScore?: number | null;
  observation?: string | null;
  site17?: Pick<ISite, 'id' | 'nameAr'> | null;
  sessionInstance?: Pick<ISessionInstance, 'id' | 'title'> | null;
  student?: Pick<IUserCustom, 'id' | 'firstName'> | null;
}

export type NewProgression = Omit<IProgression, 'id'> & { id: null };
