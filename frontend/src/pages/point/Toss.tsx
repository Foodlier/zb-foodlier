import React, { useEffect, useRef } from 'react'
import {
  loadPaymentWidget,
  PaymentWidgetInstance,
} from '@tosspayments/payment-widget-sdk'

const Toss = props => {
  const paymentWidgetRef = useRef<PaymentWidgetInstance | null>(null)
  const clientKey = 'test_ck_Z1aOwX7K8meEABdJQeW8yQxzvNPG'
  const 테스트시크릿키 = 'test_sk_ex6BJGQOVD9G4x449GarW4w2zNbg'

  const price = 1
  const customerKey = 'sdlkfgsd_-_-gfsdvnx'
  const pay = async () => {
    const paymentWidget = paymentWidgetRef.current
    try {
      const res = await paymentWidget?.requestPayment({
        orderId: 'asdasd',
        orderName: '토스 티셔츠 외 2건',
        customerName: '김토스',
        customerEmail: 'rlaehq@gmail.com',
        customerMobilePhone: '01025145147',
        successUrl: `${window.location.origin}/success`,
        failUrl: `${window.location.origin}/fail`,
      })
      console.log(res)
    } catch (err) {
      console.log(err)
    }
  }

  useEffect(() => {
    ;(async () => {
      const paymentWidget = await loadPaymentWidget(clientKey, customerKey)

      paymentWidget.renderPaymentMethods('#payment-widget', price)

      paymentWidgetRef.current = paymentWidget
    })()
  }, [])

  return (
    <div>
      <div id="payment-widget" />
      <button type="button" onClick={pay}>
        결제하기
      </button>
    </div>
  )
}

export default Toss
