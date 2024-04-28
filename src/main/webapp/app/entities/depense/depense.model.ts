import { IUserCustom } from 'app/entities/user-custom/user-custom.model';
import { DepenseType } from 'app/entities/enumerations/depense-type.model';
import { DepenseTarget } from 'app/entities/enumerations/depense-target.model';
import { DepenseFrequency } from 'app/entities/enumerations/depense-frequency.model';

export interface IDepense {
  id: number;
  type?: keyof typeof DepenseType | null;
  target?: keyof typeof DepenseTarget | null;
  frequency?: keyof typeof DepenseFrequency | null;
  amount?: number | null;
  ref?: string | null;
  message?: string | null;
  resource?: IUserCustom | null;
}

export type NewDepense = Omit<IDepense, 'id'> & { id: null };
