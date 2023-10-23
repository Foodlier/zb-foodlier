import { useEffect, useRef } from 'react'
import {
  loadPaymentWidget,
  PaymentWidgetInstance,
} from '@tosspayments/payment-widget-sdk'
import { useLocation } from 'react-router-dom'
import * as S from '../../styles/point/Charge.styled'

const Point = () => {
  const paymentWidgetRef = useRef<PaymentWidgetInstance | null>(null)
  const location = useLocation()
  console.log(location)

  const {
    amount,
    orderId: orderID,
    orderName: orderNAME,
    customerEmail: customerEMAIL,
    customerNickName: customerNAME,
  } = location.state
  const clientKey = 'test_ck_Z1aOwX7K8meEABdJQeW8yQxzvNPG'
  const customerKey = 'd-b'

  const pay = async () => {
    const paymentWidget = paymentWidgetRef.current
    try {
      const res = await paymentWidget?.requestPayment({
        orderId: orderID,
        orderName: orderNAME,
        customerName: customerNAME,
        customerEmail: customerEMAIL,
        successUrl: `${window.location.origin}/point/success`,
        failUrl: `${window.location.origin}/point/fail`,
      })
      console.log(res)
    } catch (err) {
      console.log(err)
    }
  }

  useEffect(() => {
    ;(async () => {
      const paymentWidget = await loadPaymentWidget(clientKey, customerKey)
      paymentWidget.renderPaymentMethods('#payment-widget', amount)
      paymentWidgetRef.current = paymentWidget
    })()
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [])

  return (
    <S.PaymentContainer>
      <S.PaymentWidget id="payment-widget" />
      <S.ChargeButton type="button" onClick={pay}>
        toss 결제하기
      </S.ChargeButton>
    </S.PaymentContainer>
  )
}

export default Point
