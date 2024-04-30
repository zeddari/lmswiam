import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IPayment, NewPayment } from '../payment.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IPayment for edit and NewPaymentFormGroupInput for create.
 */
type PaymentFormGroupInput = IPayment | PartialWithRequiredKeyOf<NewPayment>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IPayment | NewPayment> = Omit<T, 'paidAt' | 'validityStartTime' | 'validityEndTime'> & {
  paidAt?: string | null;
  validityStartTime?: string | null;
  validityEndTime?: string | null;
};

type PaymentFormRawValue = FormValueOf<IPayment>;

type NewPaymentFormRawValue = FormValueOf<NewPayment>;

type PaymentFormDefaults = Pick<NewPayment, 'id' | 'paidAt' | 'validityStartTime' | 'validityEndTime'>;

type PaymentFormGroupContent = {
  id: FormControl<PaymentFormRawValue['id'] | NewPayment['id']>;
  amount: FormControl<PaymentFormRawValue['amount']>;
  paymentMethod: FormControl<PaymentFormRawValue['paymentMethod']>;
  paiedBy: FormControl<PaymentFormRawValue['paiedBy']>;
  proof: FormControl<PaymentFormRawValue['proof']>;
  proofContentType: FormControl<PaymentFormRawValue['proofContentType']>;
  paidAt: FormControl<PaymentFormRawValue['paidAt']>;
  type: FormControl<PaymentFormRawValue['type']>;
  validityStartTime: FormControl<PaymentFormRawValue['validityStartTime']>;
  validityEndTime: FormControl<PaymentFormRawValue['validityEndTime']>;
  details: FormControl<PaymentFormRawValue['details']>;
  site9: FormControl<PaymentFormRawValue['site9']>;
  enrolment: FormControl<PaymentFormRawValue['enrolment']>;
  sponsoring: FormControl<PaymentFormRawValue['sponsoring']>;
  session: FormControl<PaymentFormRawValue['session']>;
  currency: FormControl<PaymentFormRawValue['currency']>;
};

export type PaymentFormGroup = FormGroup<PaymentFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class PaymentFormService {
  createPaymentFormGroup(payment: PaymentFormGroupInput = { id: null }): PaymentFormGroup {
    const paymentRawValue = this.convertPaymentToPaymentRawValue({
      ...this.getFormDefaults(),
      ...payment,
    });
    return new FormGroup<PaymentFormGroupContent>({
      id: new FormControl(
        { value: paymentRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      amount: new FormControl(paymentRawValue.amount, {
        validators: [Validators.required, Validators.min(0)],
      }),
      paymentMethod: new FormControl(paymentRawValue.paymentMethod, {
        validators: [Validators.required],
      }),
      paiedBy: new FormControl(paymentRawValue.paiedBy, {
        validators: [Validators.required],
      }),
      proof: new FormControl(paymentRawValue.proof),
      proofContentType: new FormControl(paymentRawValue.proofContentType),
      paidAt: new FormControl(paymentRawValue.paidAt, {
        validators: [Validators.required],
      }),
      type: new FormControl(paymentRawValue.type, {
        validators: [Validators.required],
      }),
      validityStartTime: new FormControl(paymentRawValue.validityStartTime, {
        validators: [Validators.required],
      }),
      validityEndTime: new FormControl(paymentRawValue.validityEndTime, {
        validators: [Validators.required],
      }),
      details: new FormControl(paymentRawValue.details),
      site9: new FormControl(paymentRawValue.site9),
      enrolment: new FormControl(paymentRawValue.enrolment),
      sponsoring: new FormControl(paymentRawValue.sponsoring),
      session: new FormControl(paymentRawValue.session),
      currency: new FormControl(paymentRawValue.currency),
    });
  }

  getPayment(form: PaymentFormGroup): IPayment | NewPayment {
    return this.convertPaymentRawValueToPayment(form.getRawValue() as PaymentFormRawValue | NewPaymentFormRawValue);
  }

  resetForm(form: PaymentFormGroup, payment: PaymentFormGroupInput): void {
    const paymentRawValue = this.convertPaymentToPaymentRawValue({ ...this.getFormDefaults(), ...payment });
    form.reset(
      {
        ...paymentRawValue,
        id: { value: paymentRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): PaymentFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      paidAt: currentTime,
      validityStartTime: currentTime,
      validityEndTime: currentTime,
    };
  }

  private convertPaymentRawValueToPayment(rawPayment: PaymentFormRawValue | NewPaymentFormRawValue): IPayment | NewPayment {
    return {
      ...rawPayment,
      paidAt: dayjs(rawPayment.paidAt, DATE_TIME_FORMAT),
      validityStartTime: dayjs(rawPayment.validityStartTime, DATE_TIME_FORMAT),
      validityEndTime: dayjs(rawPayment.validityEndTime, DATE_TIME_FORMAT),
    };
  }

  private convertPaymentToPaymentRawValue(
    payment: IPayment | (Partial<NewPayment> & PaymentFormDefaults),
  ): PaymentFormRawValue | PartialWithRequiredKeyOf<NewPaymentFormRawValue> {
    return {
      ...payment,
      paidAt: payment.paidAt ? payment.paidAt.format(DATE_TIME_FORMAT) : undefined,
      validityStartTime: payment.validityStartTime ? payment.validityStartTime.format(DATE_TIME_FORMAT) : undefined,
      validityEndTime: payment.validityEndTime ? payment.validityEndTime.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
