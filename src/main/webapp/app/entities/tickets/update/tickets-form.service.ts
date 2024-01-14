import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ITickets, NewTickets } from '../tickets.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ITickets for edit and NewTicketsFormGroupInput for create.
 */
type TicketsFormGroupInput = ITickets | PartialWithRequiredKeyOf<NewTickets>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends ITickets | NewTickets> = Omit<T, 'dateTicket' | 'dateProcess' | 'from' | 'toDate'> & {
  dateTicket?: string | null;
  dateProcess?: string | null;
  from?: string | null;
  toDate?: string | null;
};

type TicketsFormRawValue = FormValueOf<ITickets>;

type NewTicketsFormRawValue = FormValueOf<NewTickets>;

type TicketsFormDefaults = Pick<NewTickets, 'id' | 'dateTicket' | 'dateProcess' | 'from' | 'toDate'>;

type TicketsFormGroupContent = {
  id: FormControl<TicketsFormRawValue['id'] | NewTickets['id']>;
  subject: FormControl<TicketsFormRawValue['subject']>;
  title: FormControl<TicketsFormRawValue['title']>;
  reference: FormControl<TicketsFormRawValue['reference']>;
  description: FormControl<TicketsFormRawValue['description']>;
  justifDoc: FormControl<TicketsFormRawValue['justifDoc']>;
  justifDocContentType: FormControl<TicketsFormRawValue['justifDocContentType']>;
  dateTicket: FormControl<TicketsFormRawValue['dateTicket']>;
  dateProcess: FormControl<TicketsFormRawValue['dateProcess']>;
  processed: FormControl<TicketsFormRawValue['processed']>;
  from: FormControl<TicketsFormRawValue['from']>;
  toDate: FormControl<TicketsFormRawValue['toDate']>;
  decisionDetail: FormControl<TicketsFormRawValue['decisionDetail']>;
  userCustom5: FormControl<TicketsFormRawValue['userCustom5']>;
};

export type TicketsFormGroup = FormGroup<TicketsFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class TicketsFormService {
  createTicketsFormGroup(tickets: TicketsFormGroupInput = { id: null }): TicketsFormGroup {
    const ticketsRawValue = this.convertTicketsToTicketsRawValue({
      ...this.getFormDefaults(),
      ...tickets,
    });
    return new FormGroup<TicketsFormGroupContent>({
      id: new FormControl(
        { value: ticketsRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      subject: new FormControl(ticketsRawValue.subject, {
        validators: [Validators.required],
      }),
      title: new FormControl(ticketsRawValue.title, {
        validators: [Validators.required, Validators.maxLength(100)],
      }),
      reference: new FormControl(ticketsRawValue.reference, {
        validators: [Validators.required, Validators.maxLength(100)],
      }),
      description: new FormControl(ticketsRawValue.description),
      justifDoc: new FormControl(ticketsRawValue.justifDoc),
      justifDocContentType: new FormControl(ticketsRawValue.justifDocContentType),
      dateTicket: new FormControl(ticketsRawValue.dateTicket, {
        validators: [Validators.required],
      }),
      dateProcess: new FormControl(ticketsRawValue.dateProcess),
      processed: new FormControl(ticketsRawValue.processed, {
        validators: [Validators.required],
      }),
      from: new FormControl(ticketsRawValue.from),
      toDate: new FormControl(ticketsRawValue.toDate),
      decisionDetail: new FormControl(ticketsRawValue.decisionDetail),
      userCustom5: new FormControl(ticketsRawValue.userCustom5),
    });
  }

  getTickets(form: TicketsFormGroup): ITickets | NewTickets {
    return this.convertTicketsRawValueToTickets(form.getRawValue() as TicketsFormRawValue | NewTicketsFormRawValue);
  }

  resetForm(form: TicketsFormGroup, tickets: TicketsFormGroupInput): void {
    const ticketsRawValue = this.convertTicketsToTicketsRawValue({ ...this.getFormDefaults(), ...tickets });
    form.reset(
      {
        ...ticketsRawValue,
        id: { value: ticketsRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): TicketsFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      dateTicket: currentTime,
      dateProcess: currentTime,
      from: currentTime,
      toDate: currentTime,
    };
  }

  private convertTicketsRawValueToTickets(rawTickets: TicketsFormRawValue | NewTicketsFormRawValue): ITickets | NewTickets {
    return {
      ...rawTickets,
      dateTicket: dayjs(rawTickets.dateTicket, DATE_TIME_FORMAT),
      dateProcess: dayjs(rawTickets.dateProcess, DATE_TIME_FORMAT),
      from: dayjs(rawTickets.from, DATE_TIME_FORMAT),
      toDate: dayjs(rawTickets.toDate, DATE_TIME_FORMAT),
    };
  }

  private convertTicketsToTicketsRawValue(
    tickets: ITickets | (Partial<NewTickets> & TicketsFormDefaults),
  ): TicketsFormRawValue | PartialWithRequiredKeyOf<NewTicketsFormRawValue> {
    return {
      ...tickets,
      dateTicket: tickets.dateTicket ? tickets.dateTicket.format(DATE_TIME_FORMAT) : undefined,
      dateProcess: tickets.dateProcess ? tickets.dateProcess.format(DATE_TIME_FORMAT) : undefined,
      from: tickets.from ? tickets.from.format(DATE_TIME_FORMAT) : undefined,
      toDate: tickets.toDate ? tickets.toDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
