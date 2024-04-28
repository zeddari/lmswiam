import { ISite } from 'app/entities/site/site.model';
import { IUserCustom } from 'app/entities/user-custom/user-custom.model';
import { IGroup } from 'app/entities/group/group.model';
import { ITopic } from 'app/entities/topic/topic.model';
import { CertificateType } from 'app/entities/enumerations/certificate-type.model';
import { Riwayats } from 'app/entities/enumerations/riwayats.model';

export interface ICertificate {
  id: number;
  certificateType?: keyof typeof CertificateType | null;
  title?: string | null;
  riwaya?: keyof typeof Riwayats | null;
  miqdar?: number | null;
  observation?: string | null;
  site19?: ISite | null;
  userCustom6?: IUserCustom | null;
  comitte?: IGroup | null;
  topic4?: ITopic | null;
}

export type NewCertificate = Omit<ICertificate, 'id'> & { id: null };
